
from collections import defaultdict, deque
from datetime import datetime, timedelta

# Security configuration
MAX_ATTEMPTS = 5
TIME_WINDOW = timedelta(seconds=60)

# Simulated Wi-Fi security logs
failed_attempts = defaultdict(deque)
blocked_devices = set()

# Protected resource
PRINTER_IP = "192.168.1.100"


def detect_intrusion(mac_address):
    """Record and detect repeated failed authentication attempts."""

    if mac_address in blocked_devices:
        print(f"[BLOCKED] Device {mac_address} is already blocked.")
        return

    now = datetime.now()
    attempts = failed_attempts[mac_address]

    # Keep only attempts within the time window
    while attempts and now - attempts[0] > TIME_WINDOW:
        attempts.popleft()

    attempts.append(now)

    print(f"[ALERT] Failed Wi-Fi login from {mac_address}")
    print(f"Failed attempts in last 60 seconds: {len(attempts)}")

    if len(attempts) >= MAX_ATTEMPTS:
        blocked_devices.add(mac_address)
        print("\n!!! INTRUSION DETECTED !!!")
        print(f"Suspicious device: {mac_address}")
        print("Action: Device added to blocklist.")
        print("Printer access: DENIED\n")


def check_printer_access(mac_address):
    """Simulate checking access to the IP printer."""

    if mac_address in blocked_devices:
        print(f"ACCESS DENIED: {mac_address}")
        print(f"Cannot access printer at {PRINTER_IP}")
    else:
        print(f"ACCESS ALLOWED for {mac_address}")
        print(f"Printer resource: {PRINTER_IP}")


def main():
    print("=== Wi-Fi Intrusion Detection System ===")
    print("IEEE 802.11 Network Simulation")
    print("Printer IP:", PRINTER_IP)

    while True:
        print("\n1. Simulate failed Wi-Fi login")
        print("2. Check printer access")
        print("3. Show blocked devices")
        print("4. Exit")

        choice = input("Enter choice: ").strip()

        if choice == "1":
            mac = input("Enter device MAC address: ").strip().lower()

            if not mac:
                print("Invalid MAC address.")
                continue

            detect_intrusion(mac)

        elif choice == "2":
            mac = input("Enter device MAC address: ").strip().lower()
            check_printer_access(mac)

        elif choice == "3":
            print("\nBlocked devices:")
            if blocked_devices:
                for device in sorted(blocked_devices):
                    print(device)
            else:
                print("No devices blocked.")

        elif choice == "4":
            print("Exiting intrusion detection system.")
            break

        else:
            print("Invalid choice. Try again.")


if __name__ == "__main__":
    main()
