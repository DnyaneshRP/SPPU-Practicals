import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class AdderRemote extends UnicastRemoteObject implements Adder {

    protected AdderRemote() throws RemoteException {
        super();  // Export the remote object
    }

    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}

