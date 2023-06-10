import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;
public class ProjectServer2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, FileNotFoundException, IOException {
        // Κλείδωμα / για κάθε πτήση για να μην γίνονται ταυτόχρονες κρατήσεις για μία πτήση
        HashMap<String, ReentrantLock> locks = new HashMap();
        ServerSocket server;
        String message,line;
        Connection connection = null;
        Statement statement = null;
        connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        System.out.println("Επιτυχής σύνδεση με βάση δεδομένων.");
        updateTablePtisi(connection);
        updateTableKratisi(connection);
        server = new ServerSocket(5556);
        System.out.println("waiting");
        Socket sock = server.accept();
        System.out.println("accepted");
        
        DataOutputStream outstream = new DataOutputStream(sock.getOutputStream());
        DataInputStream instream = new DataInputStream (sock.getInputStream()); 
        while(true){
            message = instream.readUTF();
            if (message.equals("SEARCHTRIP")){
                line = instream.readUTF();
                
                String []data = line.split(",");
                ArrayList<String> trips = selectTablePtisi(connection,locks,data);
                
                for(String trip:trips){
                    
                    outstream.writeUTF(trip);
                }
                outstream.writeUTF("DONE");
            }
            else{
                line = instream.readUTF();
                
                String []data = line.split(",");
                outstream.writeUTF(String.valueOf(insertTableKratisi(connection,locks,data)));
            }
            System.out.println(message);
        }
    }
    // Πίνακας πτήσεων, διαγραφή και δημιουργία από την αρχή.
    // Καταχώρηση κάποιων πτήσεων
    private static void updateTablePtisi(Connection connection) throws SQLException, FileNotFoundException{
        
        Statement statement = connection.createStatement();
        String SQL = "DROP TABLE IF EXISTS ptisi";
        statement.executeUpdate(SQL);
        System.out.println("Επιτυχής διαγραφή πίνακα 'ptiti'.");
        SQL = "CREATE TABLE ptisi ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "polianaxorisis VARCHAR(50),"
                + "poliproorismou VARCHAR(50),"
                + "anaxorisi DATETIME,"
                + "xoritikotita INTEGER,"
                + "timi real"
                + ")";
        statement.executeUpdate(SQL);
        System.out.println("Επιτυχής δημιουργία πίνακα 'ptiti'.");
        SQL = "INSERT INTO ptisi (polianaxorisis, poliproorismou, anaxorisi, xoritikotita, timi) VALUES (?, ?, ?, ?, ?)";
        File newfile = new File("database/ptiseis.txt");
        if (newfile.exists()){
            String line;
            Scanner scanner = new Scanner(newfile);
            // Διάβασμα και αποστολή γραμμή γραμμή το αρχείο
            while (scanner.hasNextLine()) {
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                line = scanner.nextLine();
                String []data = line.split(",");
                //System.out.println(line);
                preparedStatement.setString(1, data[0]);
                preparedStatement.setString(2, data[1]);
                preparedStatement.setString(3, data[2]);
                preparedStatement.setInt(4, Integer.parseInt(data[3]));
                preparedStatement.setDouble(5, Double.parseDouble(data[4]));
                preparedStatement.executeUpdate();
            }
        }
        else{
            System.out.println("Δεν δημιουργήθηκαν εγγραφές στον πίνακα 'ptiti'. Δεν βρέθηκε το αρχείο πτήσεων.");
        }
    }
    // Πίνακας κρατήεων, διαγραφή και δημιουργία από την αρχή.
    private static void updateTableKratisi(Connection connection) throws SQLException{
        Statement statement = connection.createStatement();
        String SQL = "DROP TABLE IF EXISTS kratisi";
        statement.executeUpdate(SQL);
        System.out.println("Επιτυχής διαγραφή πίνακα 'kratisi'.");
        SQL = "CREATE TABLE kratisi ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "onoma VARCHAR(50),"
            + "ptisi_anaxorisis INTEGER,"
            + "ptisi_epistrofis INTEGER,"
            + "eisitiria INTEGER,"    
            + "sinolikitimi real,"
            + "FOREIGN KEY (ptisi_anaxorisis) REFERENCES ptisi(id),"
            + "FOREIGN KEY (ptisi_epistrofis) REFERENCES ptisi(id)"
            + ")";
        statement.executeUpdate(SQL);
        System.out.println("Επιτυχής δημιουργία πίνακα 'kratisi'.");
    }
    // Επιλογή πτήσεων με ότι αναζήτησε ο client
    private static ArrayList<String> selectTablePtisi(Connection connection,HashMap<String, ReentrantLock> locks, String []data) throws SQLException{
        int xoritikotita,eisitiria,kratiseis;
        if (!locks.containsKey(data[0])){
            locks.put(data[0], new ReentrantLock());
        }
        if (!locks.containsKey(data[1])){
            locks.put(data[1], new ReentrantLock());
        }
        ReentrantLock lockanaxorisis = locks.get(data[0]);
        ReentrantLock lockepistrofis = locks.get(data[1]);
        lockanaxorisis.lock();
        lockepistrofis.lock();
        ArrayList<String> trips = new ArrayList();
        String SQL = "SELECT * FROM ptisi WHERE DATE(anaxorisi) = ?"
                + "AND polianaxorisis= ? AND poliproorismou= ?";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setString(1, data[2]);
        preparedStatement.setString(2, data[0]);
        preparedStatement.setString(3, data[1]);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()){
            xoritikotita = rs.getInt("xoritikotita");
            eisitiria = Integer.valueOf(data[4]);
            kratiseis=getKratiseis(connection,rs.getString("id"));
            if (xoritikotita-kratiseis>=eisitiria)
                trips.add(rs.getString("id")+","+rs.getString("polianaxorisis")+","+
                    rs.getString("poliproorismou")+","+rs.getString("anaxorisi")+","+
                    rs.getInt("xoritikotita")+","+rs.getDouble("timi"));
        }
        SQL = "SELECT * FROM ptisi WHERE DATE(anaxorisi) = ?"
                + "AND polianaxorisis= ? AND poliproorismou= ?";
        
        
        preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setString(1, data[3]);
        preparedStatement.setString(2, data[1]);
        preparedStatement.setString(3, data[0]);
        rs = preparedStatement.executeQuery();
        while (rs.next()){
            xoritikotita = rs.getInt("xoritikotita");
            eisitiria = Integer.valueOf(data[4]);
            kratiseis=getKratiseis(connection,rs.getString("id"));
            if (xoritikotita-kratiseis>=eisitiria)
                trips.add(rs.getString("id")+","+rs.getString("polianaxorisis")+","+
                    rs.getString("poliproorismou")+","+rs.getString("anaxorisi")+","+
                    rs.getInt("xoritikotita")+","+rs.getDouble("timi"));
        }
        lockepistrofis.unlock();
        lockanaxorisis.unlock();
        return trips;
    }
    // Εισαγωγή κράτησης στον πίνακα κρατήσεων
    private static double insertTableKratisi(Connection connection,HashMap<String, ReentrantLock> locks,String []data) throws SQLException{
        String SQL;
        if (!locks.containsKey(data[0])){
            locks.put(data[0], new ReentrantLock());
        }
        if (!locks.containsKey(data[1])){
            locks.put(data[1], new ReentrantLock());
        }
        ReentrantLock lockanaxorisis = locks.get(data[0]);
        ReentrantLock lockepistrofis = locks.get(data[1]);
        lockanaxorisis.lock();
        lockepistrofis.lock();
        int xoritikotitaanaxorisis=0;
        int xoritikotitaepistrofis=0;
        int kratiseisanaxorisis,kratiseisepistrofis;
        double timi=0.0;
        PreparedStatement preparedStatement;
        SQL = "SELECT * FROM ptisi WHERE id=?";
        preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setString(1, data[0]);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()){
            xoritikotitaanaxorisis = rs.getInt("xoritikotita");
            timi +=rs.getDouble("timi");
        }
        SQL = "SELECT * FROM ptisi WHERE id=?";
        preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setString(1, data[1]);
        rs = preparedStatement.executeQuery();
        if (rs.next()){
            xoritikotitaepistrofis = rs.getInt("xoritikotita");
            timi +=rs.getDouble("timi");
        }
        kratiseisanaxorisis = getKratiseis(connection,data[0]);
        kratiseisepistrofis = getKratiseis(connection,data[1]);
        if (xoritikotitaanaxorisis-Integer.valueOf(data[2])>=kratiseisanaxorisis &&
               xoritikotitaepistrofis-Integer.valueOf(data[2])>=kratiseisepistrofis ){
            SQL = "INSERT INTO kratisi (onoma,ptisi_anaxorisis, ptisi_epistrofis, eisitiria, sinolikitimi) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, data[3]);
            preparedStatement.setInt(2, Integer.valueOf(data[0]));
            preparedStatement.setInt(3, Integer.valueOf(data[1]));
            preparedStatement.setInt(4, Integer.valueOf(data[2]));
            preparedStatement.setDouble(4, timi*Integer.valueOf(data[2]));
            preparedStatement.executeUpdate();
            timi= timi*Integer.valueOf(data[2]);
        }
        lockepistrofis.unlock();
        lockanaxorisis.unlock();
        return timi;
    }
    // Επιστροφή αριθμού κρατήσεων για μία πτήση
    private static int getKratiseis(Connection connection,String id) throws SQLException{
        
        String SQL;
        SQL = "SELECT SUM(eisitiria) FROM kratisi WHERE (ptisi_anaxorisis=?) OR (ptisi_epistrofis=?)";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setString(1, id);
        preparedStatement.setString(2, id);
        ResultSet sumrs = preparedStatement.executeQuery();
        if (sumrs.next())
                return sumrs.getInt(1);
        return 0;
    }
}
