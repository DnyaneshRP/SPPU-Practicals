import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.AlreadyBoundException;

public class RMIServer {
    public static void main(String[] args) {
        try {

            //System.setProperty("java.rmi.server.hostname", "192.168.1.100"); // <-- replace with server's IP

            // Create and export registry on default port 1099
            Registry registry = LocateRegistry.createRegistry(1099);
            
            // Create remote object
            AdderRemote adder = new AdderRemote();
            
            // Bind remote object in registry with name "Adder"
            registry.bind("Adder", adder);
            
            System.out.println("RMI Server is running...");
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}

