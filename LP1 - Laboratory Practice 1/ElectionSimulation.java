import java.util.*;

public class ElectionSimulation {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Enter process IDs separated by space (e.g. 1 2 3 4 5):");
        String[] input = sc.nextLine().split("\\s+");
        List<Integer> processes = new ArrayList<>();
        Map<Integer, Boolean> status = new HashMap<>();

        for (String s : input) processes.add(Integer.parseInt(s));

        // Input status using y/n
        for (int p : processes) {
            System.out.print("Is process " + p + " active? (y/n): ");
            char c = sc.next().toLowerCase().charAt(0);
            status.put(p, c == 'y');
        }

        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Election Simulation Menu ---");
            System.out.println("1. Ring Election");
            System.out.println("2. Bully Election");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    ringElection(processes, status);
                    break;
                case 2:
                    bullyElection(processes, status);
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    static void ringElection(List<Integer> processes, Map<Integer, Boolean> status) {
        System.out.print("Enter initiator process ID: ");
        int initiator = sc.nextInt();

        if (!status.getOrDefault(initiator, false)) {
            System.out.println("Initiator is inactive. Cannot start election.\n");
            return;
        }

        System.out.println("\n--- Ring Election Started ---\n");

        int n = processes.size();
        int startIndex = processes.indexOf(initiator);
        List<Integer> electionMsg = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int p = processes.get((startIndex + i) % n);
            if (status.get(p)) {
                electionMsg.add(p);
                System.out.println("Process " + p + " adds itself to election message: " + electionMsg + "\n");
            } else {
                System.out.println("Process " + p + " is inactive, skipped.\n");
            }
        }

        int newCoordinator = Collections.max(electionMsg);
        System.out.println("NEW COORDINATOR IS: " + newCoordinator + "\n");
    }

    static void bullyElection(List<Integer> processes, Map<Integer, Boolean> status) {
        System.out.print("Enter initiator process ID: ");
        int initiator = sc.nextInt();

        if (!status.getOrDefault(initiator, false)) {
            System.out.println("Initiator is inactive. Cannot start election.\n");
            return;
        }

        System.out.println("\n--- Bully Election Started ---\n");

        // Sort processes in ascending order
        Collections.sort(processes);

        int newCoordinator = -1;
        int startIndex = processes.indexOf(initiator);

        for (int i = startIndex; i < processes.size(); i++) {
            int current = processes.get(i);
            if (!status.get(current)) continue;

            List<Integer> higher = new ArrayList<>();
            for (int p : processes) {
                if (p > current && status.get(p)) higher.add(p);
            }

            if (!higher.isEmpty()) {
                System.out.println("Process " + current + " sends ELECTION message to " + formatList(higher));
                System.out.println("Process " + current + " received OK from " + formatList(higher) + "\n");
            } else {
                System.out.println("No higher-up process than " + current + "\n");
                newCoordinator = current; // highest active process
            }
        }

        System.out.println("NEW COORDINATOR IS: " + newCoordinator + "\n");
    }

    static String formatList(List<Integer> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append("Process ").append(list.get(i));
            if (i != list.size() - 1) sb.append(",  ");
        }
        sb.append("]");
        return sb.toString();
    }
}

