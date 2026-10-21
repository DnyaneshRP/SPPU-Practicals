
import os
import struct
from pathlib import Path
from datetime import datetime

SECTOR_SIZE = 512
OUTPUT_DIR = Path("recovered_files")


def recover_deleted_files():
    print("\n--- Deleted File Recovery ---")
    image_path = input("Enter disk image path (.img/.dd): ").strip().strip('"')

    if not os.path.isfile(image_path):
        print("Error: Disk image not found.")
        return

    try:
        # Read-only access to the evidence image
        with open(image_path, "rb") as image:
            data = image.read()

        OUTPUT_DIR.mkdir(parents=True, exist_ok=True)
        recovered = 0

        signatures = [
            ("JPG", b"\xff\xd8\xff", b"\xff\xd9"),
            ("PNG", b"\x89PNG\r\n\x1a\n", b"IEND\xae\x42\x60\x82"),
            ("PDF", b"%PDF-", b"%%EOF")
        ]

        for extension, header, footer in signatures:
            position = 0

            while True:
                start = data.find(header, position)
                if start == -1:
                    break

                end = data.find(footer, start + len(header))
                if end == -1:
                    position = start + len(header)
                    continue

                end += len(footer)
                content = data[start:end]

                recovered += 1
                filename = (
                    f"recovered_{recovered}_{datetime.now():%Y%m%d%H%M%S}."
                    f"{extension.lower()}"
                )
                output_path = OUTPUT_DIR / filename

                # Avoid overwriting an existing recovered file
                counter = 1
                while output_path.exists():
                    output_path = OUTPUT_DIR / (
                        f"recovered_{recovered}_{counter}.{extension.lower()}"
                    )
                    counter += 1

                with open(output_path, "xb") as output:
                    output.write(content)

                print(f"Recovered: {output_path}")
                position = end

        if recovered == 0:
            print("No matching file signatures found.")

        print(f"\nRecovery completed. Files found: {recovered}")
        print(f"Output folder: {OUTPUT_DIR.resolve()}")

    except (OSError, MemoryError) as error:
        print("Recovery error:", error)


def analyze_partitions():
    print("\n--- Deleted Partition Analysis (MBR) ---")
    image_path = input("Enter disk image path (.img/.dd): ").strip().strip('"')

    if not os.path.isfile(image_path):
        print("Error: Disk image not found.")
        return

    try:
        with open(image_path, "rb") as image:
            boot_sector = image.read(SECTOR_SIZE)
            image.seek(0, os.SEEK_END)
            total_sectors = image.tell() // SECTOR_SIZE

        if len(boot_sector) < SECTOR_SIZE:
            print("Invalid or incomplete disk image.")
            return

        if boot_sector[510:512] != b"\x55\xaa":
            print("Valid MBR signature not found.")
            print("The image may use GPT or another format.")
            return

        partitions = []

        for i in range(4):
            offset = 446 + (i * 16)
            entry = boot_sector[offset:offset + 16]

            status = entry[0]
            partition_type = entry[4]
            start_sector, sector_count = struct.unpack_from(
                "<II", entry, 8
            )

            if partition_type != 0 and sector_count > 0:
                partitions.append(
                    (start_sector, start_sector + sector_count,
                     partition_type, status)
                )

        partitions.sort()

        print(f"\nDisk image size: {total_sectors} sectors")
        print(f"Valid MBR signature: Yes")

        if not partitions:
            print("No active partition entries found in the MBR.")
            print("Deleted partitions cannot be confirmed from this alone.")
            return

        print("\nPartition entries found:")
        for number, (start, end, ptype, status) in enumerate(partitions, 1):
            print(
                f"Partition {number}: Start={start}, "
                f"Length={end - start} sectors, "
                f"Type=0x{ptype:02X}, "
                f"Status=0x{status:02X}"
            )

        # Identify unallocated gaps between recorded partitions
        print("\nUnallocated gaps in the recorded partition layout:")
        previous_end = 1
        gaps_found = False

        for start, end, _, _ in partitions:
            if start > previous_end:
                print(
                    f"Gap: sectors {previous_end} to {start - 1} "
                    f"({start - previous_end} sectors)"
                )
                gaps_found = True

            previous_end = max(previous_end, end)

        if previous_end < total_sectors:
            print(
                f"Gap: sectors {previous_end} to {total_sectors - 1} "
                f"({total_sectors - previous_end} sectors)"
            )
            gaps_found = True

        if not gaps_found:
            print("No unallocated gaps detected.")

        print(
            "\nNote: Gaps may be normal free space. "
            "This program does not prove that a partition was deleted."
        )

    except (OSError, struct.error) as error:
        print("Partition analysis error:", error)


def main():
    while True:
        print("\n====================================")
        print(" COMPUTER FORENSIC RECOVERY TOOL")
        print("====================================")
        print("1. Recover files from a disk image")
        print("2. Analyze deleted/missing partitions")
        print("3. Exit")

        choice = input("Enter your choice: ").strip()

        if choice == "1":
            recover_deleted_files()
        elif choice == "2":
            analyze_partitions()
        elif choice == "3":
            print("Exiting forensic recovery tool.")
            break
        else:
            print("Invalid choice. Please enter 1, 2, or 3.")


if __name__ == "__main__":
    main()
