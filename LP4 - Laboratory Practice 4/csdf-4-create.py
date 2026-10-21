
from pathlib import Path

# Create a sample PDF-like file with a recognizable signature
sample_pdf = b"%PDF-1.4\nThis is a CSDF test file.\n%%EOF"

# Create a disk image containing the test file
Path("sample.img").write_bytes(
    b"\x00" * 1024 + sample_pdf + b"\x00" * 512
)

print("Sample disk image created:", Path("sample.img").resolve())
