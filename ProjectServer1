import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProjectServer1 extends UnicastRemoteObject implements RemoteInterface{
    DataOutputStream outstream;
    DataInputStream instream;
    public ProjectServer1() throws RemoteException, IOException {
        Socket sock = new Socket("localhost", 5556);
        outstream = new DataOutputStream(sock.getOutputStream());
        instream = new DataInputStream (sock.getInputStream()); 
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            
            // RMI στην εξορισμού port 1099
            Registry registry = LocateRegistry.createRegistry(1099);
            // Αντικειμένο για απομακρυσμένες κλήσεις
            RemoteInterface remote = new ProjectServer1();
            registry.rebind("RemoteTrips", remote);

            System.out.println("Εκκίνηση RMI server.");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(ProjectServer1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // Αναζήτηση ταξιδιού, δέχεται πόλεις αναχώρισης και προορισμού, ημερομηνίες αναχώρησης και επιστροφής καθώς και εισιτήρια
    // επιστρέφει στοιχεία πτήσεων χωρισμένα με ,
    @Override
    public ArrayList<String> searchTrip(String polianaxorisis, String poliproorismou, String anaxorisi, String epistrofi, int eisitiria) throws RemoteException {
        ArrayList<String> trips = new ArrayList();
        String message;
        try {
            outstream.writeUTF("SEARCHTRIP");
            outstream.writeUTF(polianaxorisis+","+poliproorismou+","+anaxorisi+","+epistrofi+","+eisitiria);
            message = instream.readUTF();
            while(!message.equals("DONE")){
                
                trips.add(message);
                message = instream.readUTF();
            }
             
        } catch (IOException ex) {
            Logger.getLogger(ProjectServer1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return trips;
    }
    // Κράτηση ταξιδιού, δέχεται κωδικούς πτήσεις αναχώρησης και επιστροφής καθώς και εισιτήρια
    // επιστρέφει τιμή ταξιδιού αν γίνει η κράτηση αλλιώς 0.0
    @Override
    public double bookTrip(int ptisi_anaxorisis, int ptisi_epistrofis, int eisitiria, String fullname) throws RemoteException {
        String message = "0";
        try {

        outstream.writeUTF("BOOKTRIP");
        outstream.writeUTF(ptisi_anaxorisis+","+ptisi_epistrofis+","+eisitiria+","+fullname);
        message = instream.readUTF();

        
    } catch (IOException ex) {
        Logger.getLogger(ProjectServer1.class.getName()).log(Level.SEVERE, null, ex);
    }
        return Double.valueOf(message);
    }
    
}

