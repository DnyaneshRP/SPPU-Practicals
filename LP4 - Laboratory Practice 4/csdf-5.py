
import logging
from datetime import datetime, timedelta
from collections import defaultdict

# Configure logging to save events in a file
logging.basicConfig(
    filename="security_log.txt",
    level=logging.INFO,
    format="%(asctime)s - %(message)s",
    force=True
)

# Sample security events for testing
events = [
    {"time": "2026-10-20 10:00:00", "ip": "192.168.1.10",
     "event": "LOGIN_FAILED"},
    {"time": "2026-10-20 10:01:00", "ip": "192.168.1.10",
     "event": "LOGIN_FAILED"},
    {"time": "2026-10-20 10:02:00", "ip": "192.168.1.10",
     "event": "LOGIN_FAILED"},
    {"time": "2026-10-20 10:03:00", "ip": "192.168.1.10",
     "event": "LOGIN_SUCCESS"},
    {"time": "2026-10-20 10:04:00", "ip": "192.168.1.20",
     "event": "LOGIN_SUCCESS"},
]

# Capture and store log events
def capture_logs(events):
    print("\n--- Log Capturing ---")
    for event in events:
        message = (
            f"IP={event['ip']} | EVENT={event['event']}"
        )
        logging.info(message)
        print(message)

    print("\nLogs saved in security_log.txt")


# Correlate events to identify suspicious activity
def correlate_events(events):
    print("\n--- Event Correlation ---")

    failed_logins = defaultdict(list)

    for event in events:
        event_time = datetime.strptime(
            event["time"], "%Y-%m-%d %H:%M:%S"
        )
        ip = event["ip"]

        if event["event"] == "LOGIN_FAILED":
            failed_logins[ip].append(event_time)

        elif event["event"] == "LOGIN_SUCCESS":
            recent_failures = [
                t for t in failed_logins[ip]
                if timedelta(0) <= event_time - t <= timedelta(minutes=5)
            ]

            if len(recent_failures) >= 3:
                print(
                    f"ALERT: Suspicious login from {ip} "
                    f"after {len(recent_failures)} failed attempts!"
                )

    # Detect repeated failed logins within five minutes
    for ip, times in failed_logins.items():
        for current_time in times:
            count = sum(
                1 for t in times
                if timedelta(0) <= current_time - t <= timedelta(minutes=5)
            )

            if count >= 3:
                print(
                    f"WARNING: {count} failed login attempts "
                    f"detected from IP {ip} within five minutes."
                )
                break

    print("\nEvent correlation completed.")


# Main program
if __name__ == "__main__":
    capture_logs(events)
    correlate_events(events)
