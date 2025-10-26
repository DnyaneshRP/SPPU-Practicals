import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Represents a message sent between processes in the distributed system.
 */
class Message {
    final int timestamp;
    final String content;
    final int senderId; // ADDED: Store the ID of the sending process

    public Message(int timestamp, String content, int senderId) { // MODIFIED
        this.timestamp = timestamp;
        this.content = content;
        this.senderId = senderId; // ADDED
    }
}

/**
 * Represents a process in our simulated distributed system.
 * Each process has its own logical clock and communicates with others.
 */
class Process implements Runnable {
    private final int id;
    private int clock;
    private final List<Message> messageQueue = new ArrayList<>();
    private final Object lock = new Object();

    public Process(int id) {
        this.id = id;
        this.clock = 0;
    }

    /**
     * Simulates a local event within the process.
     * The clock is incremented.
     */
    public void localEvent() {
        synchronized (lock) {
            clock++;
            System.out.printf("Process %d: Local event. Clock is now %d\n", id, clock);
        }
    }

    /**
     * Sends a message to another process.
     * The process increments its clock and sends the message with the new timestamp.
     *
     * @param destination The process to send the message to.
     * @param content The content of the message.
     */
    public void sendMessage(Process destination, String content) {
        synchronized (lock) {
            clock++;
            // MODIFIED: Pass the sender's ID (this.id) when creating the message
            Message message = new Message(clock, content, id); 
            // Modified to not show the message content
            System.out.printf("Process %d: Sending message to Process %d. Clock is %d\n",
                id, destination.id, clock);
            destination.receiveMessage(message);
        }
    }

    /**
     * Receives a message from another process.
     * This is where Lamport's algorithm is applied.
     *
     * @param message The message being received.
     */
    public void receiveMessage(Message message) {
        synchronized (lock) {
            // Lamport's algorithm: update local clock to max(local_clock, message_clock) + 1
            clock = Math.max(clock, message.timestamp) + 1;
            messageQueue.add(message);
            // MODIFIED: Now shows the sender's ID (message.senderId)
            System.out.printf("Process %d: Received message from P%d (T=%d). Updated clock to %d\n",
                id, message.senderId, message.timestamp, clock); 
        }
    }

    @Override
    public void run() {
        // This is just for demonstration if we want to run processes in parallel.
        // The main simulation logic is in the LamportsClock class for clarity.
    }

    public int getId() {
        return id;
    }

    public int getClock() {
        synchronized (lock) {
            return clock;
        }
    }
}

/**
 * Main class to set up and run the Lamport's Clock simulation.
 */
public class LamportsClock {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("\n--- Lamport's Logical Clock Simulation ---\n\n");

        // Create three processes for our distributed system simulation
        Process p1 = new Process(1);
        Process p2 = new Process(2);
        Process p3 = new Process(3);

        // --- Simulation Scenario ---

        // 1. Local event at P1
        p1.localEvent();
        printClocks(p1, p2, p3);

        // 2. P1 sends a message to P2
        p1.sendMessage(p2, "Hello from P1");
        printClocks(p1, p2, p3);

        // 3. Local event at P3
        p3.localEvent();
        printClocks(p1, p2, p3);
        
        // 4. P2 sends a message to P3
        p2.sendMessage(p3, "Reply from P2");
        printClocks(p1, p2, p3);

        // 5. P3 sends a message to P1
        p3.sendMessage(p1, "Hi P1, from P3");
        printClocks(p1, p2, p3);

        // 6. Another local event at P2
        p2.localEvent();
        printClocks(p1, p2, p3);
        

        System.out.print("\nFinal Clock Values: ");
        printClocksInline(p1, p2, p3); // New simplified final output

    }
    
    /**
     * Helper method to print the current state of all clocks.
     */
    private static void printClocks(Process... processes) {
        System.out.print(">>> Current Clocks: ");
        for (Process p : processes) {
            System.out.printf("[P%d: %d] ", p.getId(), p.getClock());
        }
        System.out.println("\n" + "-".repeat(60));
    }

    /**
     * Helper method to print final clock states on a single line.
     */
    private static void printClocksInline(Process... processes) {
        for (Process p : processes) {
            System.out.printf("[P%d: %d] ", p.getId(), p.getClock());
        }
        System.out.println();
    }
}

