
import re

# Sample email header
email_header = """
From: sender@example.com
To: receiver@example.com
Date: Sun, 18 Oct 2026 10:30:00 +0000
Subject: Suspicious Email
Return-Path: <sender@example.com>
Message-ID: <12345@example.com>
Received: from mail.example.com (192.0.2.10)
    by mx.receiver.com;
Received: from unknown.example.net (198.51.100.25)
    by mail.example.com;
"""

# Function to extract header information
def analyze_email(header):
    print("========== EMAIL HEADER ANALYSIS ==========")

    fields = [
        "From",
        "To",
        "Date",
        "Subject",
        "Return-Path",
        "Message-ID"
    ]

    # Extract basic header fields
    for field in fields:
        match = re.search(
            rf"^{re.escape(field)}:\s*(.+)$",
            header,
            re.MULTILINE | re.IGNORECASE
        )

        if match:
            print(f"{field}: {match.group(1)}")
        else:
            print(f"{field}: Not found")

    # Extract Received fields
    print("\n========== EMAIL ROUTING INFORMATION ==========")

    received_headers = re.findall(
        r"^Received:\s*(.*(?:\n[ \t]+.*)*)",
        header,
        re.MULTILINE | re.IGNORECASE
    )

    if received_headers:
        for i, received in enumerate(received_headers, 1):
            print(f"Hop {i}: {' '.join(received.split())}")
    else:
        print("No Received fields found")

    # Extract IPv4 addresses
    print("\n========== IP ADDRESS ANALYSIS ==========")

    ip_addresses = re.findall(
        r"\b(?:\d{1,3}\.){3}\d{1,3}\b",
        header
    )

    # Validate IPv4 octets
    valid_ips = []
    for ip in ip_addresses:
        if all(0 <= int(part) <= 255 for part in ip.split(".")):
            if ip not in valid_ips:
                valid_ips.append(ip)

    if valid_ips:
        for ip in valid_ips:
            print("IP Address:", ip)
    else:
        print("No IP addresses found")

    print("\n========== INVESTIGATION SUMMARY ==========")
    print("Header analysis completed.")
    print("Verify sender identity and IP addresses")
    print("using trusted mail-server logs.")

# Execute the program
analyze_email(email_header)