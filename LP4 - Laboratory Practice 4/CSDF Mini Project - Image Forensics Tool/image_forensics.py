import tkinter as tk
from tkinter import ttk, filedialog, messagebox
from tkinter.scrolledtext import ScrolledText

from PIL import Image
from PIL import ImageTk
from PIL import ImageChops
from PIL import ImageEnhance

import hashlib
import exifread
import numpy as np
import os
import datetime

from math import log2

import matplotlib.pyplot as plt

from reportlab.pdfgen import canvas

class ImageForensicWorkbench:

    def __init__(self, root):

        self.root = root
        self.root.title("Digital Image Forensic Investigation Workbench")
        self.root.geometry("1400x850")

        self.image_path = None
        self.photo = None
        self.ela_file = None

        self.create_header()
        self.create_toolbar()
        self.create_workspace()  # NEW


    def create_header(self):

        header = tk.Frame(
            self.root,
            bg="#0B3D91",
            height=60
        )

        header.pack(fill="x")

        title = tk.Label(
            header,
            text="DIGITAL IMAGE FORENSIC INVESTIGATION",
            bg="#0B3D91",
            fg="white",
            font=("Segoe UI", 18, "bold")
        )

        title.pack(pady=10)


    def create_workspace(self):

        # Entire workspace
        self.workspace = tk.Frame(self.root)
        self.workspace.pack(fill="both", expand=True)

        # Vertical split
        self.paned_vertical = tk.PanedWindow(
            self.workspace,
            orient=tk.VERTICAL,
            sashrelief="raised"
        )

        self.paned_vertical.pack(
            fill="both",
            expand=True
        )

        # ============================
        # TOP SECTION
        # ============================

        self.top_frame = tk.Frame(
            self.paned_vertical,
            height=300
        )

        self.paned_vertical.add(
            self.top_frame,
            minsize=250
        )

        # ============================
        # BOTTOM SECTION
        # ============================

        self.bottom_frame = tk.Frame(
            self.paned_vertical
        )

        self.paned_vertical.add(
            self.bottom_frame,
            minsize=250
        )

        # ============================
        # HORIZONTAL SPLIT FOR TOP
        # ============================

        self.paned_horizontal = tk.PanedWindow(
            self.top_frame,
            orient=tk.HORIZONTAL,
            sashrelief="raised"
        )

        self.paned_horizontal.pack(
            fill="both",
            expand=True
        )

        # LEFT PANEL

        self.left_panel = tk.LabelFrame(
            self.paned_horizontal,
            text="Investigation Summary"
        )

        self.paned_horizontal.add(
            self.left_panel,
            minsize=350
        )

        # RIGHT PANEL

        self.right_panel = tk.LabelFrame(
            self.paned_horizontal,
            text="Selected Image"
        )

        self.paned_horizontal.add(
            self.right_panel
        )

        # SUMMARY TEXT

        self.summary_text = ScrolledText(
            self.left_panel,
            font=("Consolas",10)
        )

        self.summary_text.pack(
            fill="both",
            expand=True,
            padx=5,
            pady=5
        )

        # IMAGE PREVIEW

        self.image_label = tk.Label(
            self.right_panel,
            bg="white"
        )

        self.image_label.pack(
            fill="both",
            expand=True,
            padx=5,
            pady=5
        )

        # NOTEBOOK INSIDE BOTTOM FRAME

        self.create_tabs()
    
    def create_toolbar(self):

        toolbar = tk.Frame(self.root)

        toolbar.pack(
            fill="x",
            padx=5,
            pady=5
        )

        ttk.Button(
            toolbar,
            text="Open Image",
            command=self.load_image
        ).pack(side="left", padx=5)

        ttk.Button(
            toolbar,
            text="Analyze",
            command=self.analyze_image
        ).pack(side="left", padx=5)

        ttk.Button(
            toolbar,
            text="Histogram",
            command=self.show_histogram
        ).pack(side="left", padx=5)

        ttk.Button(
            toolbar,
            text="Export PDF",
            command=self.export_pdf
        ).pack(side="left", padx=5)


    def create_main_layout(self):

        self.main_frame = tk.Frame(self.root)

        self.main_frame.pack(
            fill="both",
            expand=True
        )

        self.left_panel = tk.LabelFrame(
            self.main_frame,
            text="Investigation Summary",
            width=450
        )

        self.left_panel.pack(
            side="left",
            fill="both",
            padx=5,
            pady=5
        )

        self.right_panel = tk.LabelFrame(
            self.main_frame,
            text="Image Preview"
        )

        self.right_panel.pack(
            side="right",
            fill="both",
            expand=True,
            padx=5,
            pady=5
        )

        self.summary_text = ScrolledText(
            self.left_panel,
            font=("Consolas", 10)
        )

        self.summary_text.pack(
            fill="both",
            expand=True
        )

        self.image_label = tk.Label(
            self.right_panel,
            bg="white"
        )

        self.image_label.pack(
            expand=True
        )


    def create_tabs(self):
        self.notebook = ttk.Notebook(
            self.bottom_frame
        )

        self.notebook.pack(
            fill="both",
            expand=True
        )

        # Tabs

        self.tab_file = ttk.Frame(self.notebook)
        self.tab_metadata = ttk.Frame(self.notebook)
        self.tab_analysis = ttk.Frame(self.notebook)
        self.tab_ela = ttk.Frame(self.notebook)
        self.tab_report = ttk.Frame(self.notebook)

        self.notebook.add(
            self.tab_file,
            text="File Details"
        )

        self.notebook.add(
            self.tab_metadata,
            text="Metadata"
        )

        self.notebook.add(
            self.tab_analysis,
            text="Analysis"
        )

        self.notebook.add(
            self.tab_ela,
            text="ELA View"
        )

        self.notebook.add(
            self.tab_report,
            text="Report"
        )

        # File Tab

        self.file_text = ScrolledText(
            self.tab_file
        )

        self.file_text.pack(
            fill="both",
            expand=True
        )

        # Metadata Tab

        self.metadata_text = ScrolledText(
            self.tab_metadata
        )

        self.metadata_text.pack(
            fill="both",
            expand=True
        )

        # Analysis Tab

        self.analysis_text = ScrolledText(
            self.tab_analysis
        )

        self.analysis_text.pack(
            fill="both",
            expand=True
        )

        # ELA Tab

        self.ela_image_label = tk.Label(
            self.tab_ela,
            bg="white"
        )

        self.ela_image_label.pack(
            fill="both",
            expand=True
        )

        # Report Tab

        self.report_text = ScrolledText(
            self.tab_report
        )

        self.report_text.pack(
            fill="both",
            expand=True
        )




    def load_image(self):

        self.image_path = filedialog.askopenfilename(

            filetypes=[
                ("Images", "*.jpg *.jpeg *.png *.bmp")
            ]

        )

        if not self.image_path:
            return

        img = Image.open(self.image_path)

        preview = img.copy()

        preview.thumbnail((700,700))

        self.photo = ImageTk.PhotoImage(preview)

        self.image_label.config(
            image=self.photo
        )

        self.image_label.image = self.photo

        self.display_file_info()




    def display_file_info(self):

        img = Image.open(self.image_path)

        size = os.path.getsize(self.image_path)

        created = datetime.datetime.fromtimestamp(
            os.path.getctime(self.image_path)
        )

        modified = datetime.datetime.fromtimestamp(
            os.path.getmtime(self.image_path)
        )

        info = f"""

FILE DETAILS
================================

Filename :
{os.path.basename(self.image_path)}

Format :
{img.format}

Dimensions :
{img.size[0]} x {img.size[1]}

Color Mode :
{img.mode}

File Size :
{round(size/1024,2)} KB

Created :
{created}

Modified :
{modified}

"""

        self.summary_text.delete(
            "1.0",
            tk.END
        )

        self.summary_text.insert(
            tk.END,
            info
        )

        self.file_text.delete(
            "1.0",
            tk.END
        )

        self.file_text.insert(
            tk.END,
            info
        )



    def calculate_hash(self, algo):

        h = hashlib.new(algo)

        with open(
            self.image_path,
            "rb"
        ) as f:

            while True:

                chunk = f.read(4096)

                if not chunk:
                    break

                h.update(chunk)

        return h.hexdigest()




    def entropy(self, image):

        grayscale = image.convert("L")

        img = np.array(grayscale)

        histogram, _ = np.histogram(
            img,
            bins=256,
            range=(0,256)
        )

        histogram = histogram / histogram.sum()

        entropy = 0

        for p in histogram:

            if p > 0:
                entropy -= p * log2(p)

        return round(entropy,4)




    def generate_ela(self):

        original = Image.open(
            self.image_path
        ).convert("RGB")

        temp_name = "temp_ela.jpg"

        original.save(
            temp_name,
            "JPEG",
            quality=90
        )

        compressed = Image.open(
            temp_name
        )

        diff = ImageChops.difference(
            original,
            compressed
        )

        extrema = diff.getextrema()

        max_diff = max(
            ex[1]
            for ex in extrema
        )

        if max_diff == 0:
            max_diff = 1

        scale = 255.0 / max_diff

        diff = ImageEnhance.Brightness(
            diff
        ).enhance(scale)

        self.ela_file = "ela_output.png"

        diff.save(self.ela_file)

        ela_img = diff.copy()

        ela_img.thumbnail(
            (900,600)
        )

        ela_photo = ImageTk.PhotoImage(
            ela_img
        )

        self.ela_image_label.config(
            image=ela_photo
        )

        self.ela_image_label.image = ela_photo

        os.remove(temp_name)

        return self.ela_file




    def extract_metadata(self):

        self.metadata_text.delete(
            "1.0",
            tk.END
        )

        try:

            with open(
                self.image_path,
                "rb"
            ) as f:

                tags = exifread.process_file(f)

            if tags:

                for key,value in tags.items():

                    self.metadata_text.insert(
                        tk.END,
                        f"{key} : {value}\n"
                    )

            else:

                self.metadata_text.insert(
                    tk.END,
                    "No metadata found."

                )

        except Exception as e:

            self.metadata_text.insert(
                tk.END,
                str(e)
            )



    def analyze_image(self):

        if not self.image_path:

            messagebox.showerror(
                "Error",
                "Open image first"
            )

            return

        image = Image.open(
            self.image_path
        )

        md5 = self.calculate_hash(
            "md5"
        )

        sha256 = self.calculate_hash(
            "sha256"
        )

        entropy_value = self.entropy(
            image
        )

        self.generate_ela()
        self.extract_metadata()

        analysis = f"""

FORENSIC ANALYSIS
=================================

MD5 HASH
---------------------------------
{md5}

SHA256 HASH
---------------------------------
{sha256}

ENTROPY
---------------------------------
{entropy_value}

ELA
---------------------------------
ELA Image Generated Successfully

FILE INTEGRITY
---------------------------------
Digital Fingerprint Generated

"""

        self.analysis_text.delete(
            "1.0",
            tk.END
        )

        self.analysis_text.insert(
            tk.END,
            analysis
        )

        self.generate_report(
            md5,
            sha256,
            entropy_value
        )



    def generate_report(
            self,
            md5,
            sha256,
            entropy_value):

        case_id = datetime.datetime.now().strftime(
            "IMG-%Y%m%d-%H%M%S"
        )

        self.final_report = f"""

DIGITAL IMAGE FORENSIC REPORT

==================================

CASE ID:
{case_id}

FILE:
{os.path.basename(self.image_path)}

MD5:
{md5}

SHA256:
{sha256}

Entropy:
{entropy_value}

Metadata:
Extracted Successfully

ELA:
Generated Successfully

Generated On:
{datetime.datetime.now()}

==================================

"""

        self.report_text.delete(
            "1.0",
            tk.END
        )

        self.report_text.insert(
            tk.END,
            self.final_report
        )



    def show_histogram(self):

        if not self.image_path:
            return

        image = Image.open(
            self.image_path
        )

        r,g,b = image.convert(
            "RGB"
        ).split()

        plt.figure(
            figsize=(10,6)
        )

        plt.hist(
            np.array(r).flatten(),
            bins=256,
            color="red",
            alpha=0.5
        )

        plt.hist(
            np.array(g).flatten(),
            bins=256,
            color="green",
            alpha=0.5
        )

        plt.hist(
            np.array(b).flatten(),
            bins=256,
            color="blue",
            alpha=0.5
        )

        plt.title(
            "RGB Histogram"
        )

        plt.show()



    def export_pdf(self):

        if not hasattr(
            self,
            "final_report"
        ):
            return

        pdf = canvas.Canvas(
            "Forensic_Report.pdf"
        )

        text = pdf.beginText(
            40,
            800
        )

        for line in self.final_report.split(
            "\n"
        ):
            text.textLine(line)

        pdf.drawText(text)

        pdf.save()

        messagebox.showinfo(
            "Success",
            "PDF Report Exported"
        )


root = tk.Tk()

app = ImageForensicWorkbench(
    root
)

root.mainloop()