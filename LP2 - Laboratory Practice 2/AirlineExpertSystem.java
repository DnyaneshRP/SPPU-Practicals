import java.util.*;

class Flight {
    String flightId;
    String source;
    String destination;
    int departureTime;
    int capacity;
    String weather;
    boolean delayed;
    String flightType;
    int fuelLevel;
    String emergencyStatus;
    String atcStatus;

    public Flight(String id, String src, String dest, int time,
                  int cap, String weather, String type, int fuel,
                  String emergency, String atc) {

        this.flightId = id;
        this.source = src;
        this.destination = dest;
        this.departureTime = time;
        this.capacity = cap;
        this.weather = weather;
        this.flightType = type;
        this.fuelLevel = fuel;
        this.emergencyStatus = emergency;
        this.atcStatus = atc;
        this.delayed = false;
    }
}

class Cargo {
    String cargoId;
    String source;
    String destination;
    int weight;
    String type;
    int priority;
    String handling;
    String assignedFlight = "None";

    public Cargo(String id, String src, String dest, int weight, String type) {
        this.cargoId = id;
        this.source = src;
        this.destination = dest;
        this.weight = weight;
        this.type = type;
        this.priority = setPriority();
        this.handling = setHandling();
    }

    private int setPriority() {
        if (type.equalsIgnoreCase("perishable") || type.equalsIgnoreCase("medical"))
            return 1;
        if (type.equalsIgnoreCase("fragile"))
            return 2;
        return 3;
    }

    private String setHandling() {
        if (type.equalsIgnoreCase("perishable"))
            return "Keep Refrigerated";
        if (type.equalsIgnoreCase("medical"))
            return "Handle Urgently";
        if (type.equalsIgnoreCase("fragile"))
            return "Handle with Care";
        return "Standard Handling";
    }
}

public class AirlineExpertSystem {

    static List<Flight> flights = new ArrayList<>();
    static List<Cargo> cargos = new ArrayList<>();
    static List<String> conflicts = new ArrayList<>();

    public static void checkWeather(Flight f) {
        if (f.weather.equalsIgnoreCase("storm")) {
            f.delayed = true;
        }
    }

    public static void checkConflict() {
        conflicts.clear();

        for (int i = 0; i < flights.size(); i++) {
            for (int j = i + 1; j < flights.size(); j++) {

                if (flights.get(i).departureTime == flights.get(j).departureTime) {

                    String msg = "❌ Conflict: " +
                            flights.get(i).flightId + " & " +
                            flights.get(j).flightId;

                    conflicts.add(msg);
                }
            }
        }
    }

    public static void sortCargo() {
        cargos.sort(Comparator.comparingInt(c -> c.priority));
    }

    public static void matchCargoToFlights() {

        for (Cargo c : cargos) {

            for (Flight f : flights) {

                if (!f.flightType.equalsIgnoreCase("cargo"))
                    continue;

                if (!f.atcStatus.equalsIgnoreCase("cleared"))
                    continue;

                boolean emergency = f.emergencyStatus.equalsIgnoreCase("yes");

                if (f.source.equalsIgnoreCase(c.source) &&
                    f.destination.equalsIgnoreCase(c.destination) &&
                    f.capacity >= c.weight &&
                    !f.delayed &&
                    (f.fuelLevel > 30 || emergency)) {

                    f.capacity -= c.weight;
                    c.assignedFlight = f.flightId;
                    break;
                }
            }
        }
    }

    // ✈️ FLIGHT DISPLAY (COMPACT + SEPARATOR)
    public static void displayFlights() {
        System.out.println("\n================================ FLIGHT DETAILS ================================");

        System.out.printf("%-6s %-10s %-10s %-5s %-9s %-9s %-5s %-9s %-10s   |   %-10s\n",
                "ID", "SOURCE", "DEST", "TIME", "CAPACITY", "TYPE",
                "FUEL", "EMERGENCY", "ATC", "STATUS");

        for (Flight f : flights) {

            String status = f.delayed ? "DELAYED" : "ON TIME";

            System.out.printf("%-6s %-10s %-10s %-5d %-9d %-9s %-5d %-9s %-10s   |   %-10s\n",
                    f.flightId, f.source, f.destination,
                    f.departureTime, f.capacity,
                    f.flightType, f.fuelLevel,
                    f.emergencyStatus, f.atcStatus,
                    status);
        }
    }

    // 📦 CARGO DISPLAY (COMPACT + SEPARATOR)
    public static void displayCargo() {
      System.out.println("\n================================= CARGO DETAILS =================================");

      System.out.printf("%-6s %-10s %-10s %-8s %-10s   |   %-12s %-18s %-10s\n",
              "ID", "SOURCE", "DEST", "WEIGHT", "TYPE",
              "PRIORITY(1-3)", "HANDLING", "FLIGHT");

      for (Cargo c : cargos) {
          System.out.printf("%-6s %-10s %-10s %-8d %-10s   |   %-12d %-18s %-10s\n",
                  c.cargoId, c.source, c.destination,
                  c.weight, c.type,
                  c.priority, c.handling, c.assignedFlight);
      }
    }

    public static void runExpertSystem() {
        System.out.println("\nRunning Expert System...\n");

        for (Flight f : flights) {
            checkWeather(f);
        }

        checkConflict();
        sortCargo();
        matchCargoToFlights();

        displayFlights();
        displayCargo();

        if (!conflicts.isEmpty()) {
            System.out.println("\n========== CONFLICTS ==========");
            for (String c : conflicts) {
                System.out.println(c);
            }
        }

        System.out.println("\nProcessing Complete!");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n==== Airline Expert System ====");
            System.out.println("1. Add Flight");
            System.out.println("2. Add Cargo");
            System.out.println("3. Run Expert System");
            System.out.println("4. Exit");

            System.out.print("\nEnter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Flight ID: ");
                    String id = sc.next();

                    System.out.print("Source: ");
                    String src = sc.next();

                    System.out.print("Destination: ");
                    String dest = sc.next();

                    System.out.print("Departure Time: ");
                    int time = sc.nextInt();

                    System.out.print("Capacity (kg): ");
                    int cap = sc.nextInt();

                    System.out.print("Flight Type (cargo/passenger): ");
                    String type = sc.next();

                    System.out.print("Fuel Level (%): ");
                    int fuel = sc.nextInt();

                    System.out.print("Weather (clear/storm): ");
                    String weather = sc.next();

                    System.out.print("Emergency Status (yes/no): ");
                    String emergency = sc.next();

                    System.out.print("ATC Status (cleared/not_cleared): ");
                    String atc = sc.next();

                    flights.add(new Flight(id, src, dest, time, cap, weather, type, fuel, emergency, atc));
                    break;

                case 2:
                    System.out.print("Cargo ID: ");
                    String cid = sc.next();

                    System.out.print("Source: ");
                    String csrc = sc.next();

                    System.out.print("Destination: ");
                    String cdest = sc.next();

                    System.out.print("Weight (kg): ");
                    int weight = sc.nextInt();

                    System.out.print("Type (perishable/medical/fragile/normal): ");
                    String ctype = sc.next();

                    cargos.add(new Cargo(cid, csrc, cdest, weight, ctype));
                    break;

                case 3:
                    runExpertSystem();
                    break;

                case 4:
                    System.exit(0);
            }
        }
    }
}
