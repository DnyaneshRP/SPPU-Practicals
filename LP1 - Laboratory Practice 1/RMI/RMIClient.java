import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
    public static void main(String[] args) {
        try {
            // Locate registry at localhost(/server ip address) and default port 
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            
            // Lookup remote object "Adder"
            Adder adder = (Adder) registry.lookup("Adder");
            
            // Invoke remote method
            int result = adder.add(5, 3);
            System.out.println("Result of 5 + 3 = " + result);
            
        } catch (Exception e) {
            e.printStackTrace();
            
        }
    }
}

