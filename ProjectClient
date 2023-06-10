import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.JOptionPane;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class ProjectClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        RemoteInterface remote = (RemoteInterface) registry.lookup("RemoteTrips");
        String fullname = JOptionPane.showInputDialog(null, "Insert your fullname :");
        if (fullname != null) {
            new Gui("Client",fullname,remote);
        }
        
    }
    
}

