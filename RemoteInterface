import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

// Περιέχονται οι μέθοδοι που καλούνται από τον client. υπάρχει αντίγραφο και στον client
public interface RemoteInterface extends Remote {
    ArrayList<String> searchTrip(String polianaxorisis,String poliproorismou,
            String anaxorisi,String epistrofi,int eisitiria) throws RemoteException;
    double bookTrip(int ptisi_anaxorisis, int ptisi_epistrofis, int eisitiria, String fullname) throws RemoteException;
}

