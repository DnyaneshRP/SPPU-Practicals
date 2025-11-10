import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * A simplified NTP client to demonstrate clock synchronization.
 * This client sends a request to an NTP server and prints the server's time,
 * the local time, and the difference between them. It also shows the
 * T1, T2, T3, T4 timestamps, RTT, and clock offset.
 */
public class NTPClient {

    // NTP server to connect to. pool.ntp.org is a good choice.
    private static final String NTP_SERVER = "pool.ntp.org";
    // NTP packet size is 48 bytes.
    private static final int NTP_PACKET_SIZE = 48;
    // The port for NTP is 123.
    private static final int NTP_PORT = 123;
    // NTP timestamp starts from 1/1/1900.
    // Unix epoch starts from 1/1/1970.
    // This is the difference in seconds.
    private static final long NTP_UNIX_OFFSET = 2208988800L;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket()) {
            // Set a timeout for the socket to avoid waiting indefinitely.
            socket.setSoTimeout(10000); // 10 seconds

            InetAddress serverAddress = InetAddress.getByName(NTP_SERVER);
            byte[] buffer = new byte[NTP_PACKET_SIZE];

            // Initialize the NTP request packet.
            // The first byte sets the Leap Indicator (LI=0), Version Number (VN=3),
            // and Mode (Mode=3 for client).
            // LI = 00, VN = 011, Mode = 011 -> 00011011 in binary, which is 27 decimal or 0x1B hex.
            buffer[0] = 0x1B;

            DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length, serverAddress, NTP_PORT);

            // T1: Client's timestamp when the request departs.
            long t1 = System.currentTimeMillis();
            System.out.println("Sending NTP request to " + NTP_SERVER);
            socket.send(requestPacket);

            // Create a packet to receive the server's response.
            DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(responsePacket);

            // T4: Client's timestamp when the response arrives.
            long t4 = System.currentTimeMillis();
            System.out.println("Received NTP response.\n");


            // Extract timestamps from the NTP response packet.
            // These are in NTP format (seconds since 1900).
            long t2_ntp = getTimestamp(buffer, 32); // T2: Server's receive timestamp
            long t3_ntp = getTimestamp(buffer, 40); // T3: Server's transmit timestamp

            // Convert NTP timestamps to Unix epoch (milliseconds since 1970).
            long t2 = (t2_ntp - NTP_UNIX_OFFSET) * 1000;
            long t3 = (t3_ntp - NTP_UNIX_OFFSET) * 1000;

            // --- NTP Calculations ---
            // Round-Trip Time (RTT) or delay (delta)
            // delta = (T4 - T1) - (T3 - T2)
            long rtt = (t4 - t1) - (t3 - t2);

            // Clock Offset (theta)
            // theta = ((T2 - T1) + (T3 - T4)) / 2
            long offset = ((t2 - t1) + (t3 - t4)) / 2;


            // Format the dates for display.
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss.SSS z");
            sdf.setTimeZone(TimeZone.getDefault());

            System.out.println("--- NTP Timestamp Details ---");
            System.out.println("T1 (Client Request Time):   " + sdf.format(new Date(t1)) + " (" + t1 + " ms)");
            System.out.println("T2 (Server Receive Time):   " + sdf.format(new Date(t2)) + " (" + t2 + " ms)");
            System.out.println("T3 (Server Transmit Time):  " + sdf.format(new Date(t3)) + " (" + t3 + " ms)");
            System.out.println("T4 (Client Receive Time):   " + sdf.format(new Date(t4)) + " (" + t4 + " ms)");

            System.out.println("\n--- Clock Synchronization Calculations ---");
            System.out.println("Round-Trip Time (RTT): " + rtt + " ms");
            System.out.println("Clock Offset:          " + offset + " ms");
            
            long correctedTime = System.currentTimeMillis() + offset;
            System.out.println("\n--- Corrected Time ---");
            System.out.println("NTP Server Time (T3):     " + sdf.format(new Date(t3)));
            System.out.println("Local Clock Time (T4):    " + sdf.format(new Date(t4)));
            System.out.println("Corrected Local Time:     " + sdf.format(new Date(correctedTime)));

            if (offset > 0) {
                 System.out.println("\nYour local clock is ahead of the NTP server by " + offset + " ms.");
            } else {
                 System.out.println("\nYour local clock is behind the NTP server by " + (-offset) + " ms.");
            }

        } catch (Exception e) {
            System.err.println("Error communicating with NTP server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper method to extract a 64-bit NTP timestamp from the packet buffer.
     * This implementation reads the first 32 bits (the integer part).
     *
     * @param buffer The byte array containing the NTP packet.
     * @param offset The starting offset of the 8-byte timestamp.
     * @return The timestamp value as a long.
     */
    private static long getTimestamp(byte[] buffer, int offset) {
        long seconds = 0;
        for (int i = 0; i < 4; i++) {
            seconds = (seconds << 8) | (buffer[offset + i] & 0xFF);
        }
        // We are ignoring the fraction part for this simple client.
        return seconds;
    }
}


