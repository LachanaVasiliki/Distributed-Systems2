import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Gui extends JFrame implements ActionListener{
    // interface επικοινωνίας με τον server1
    RemoteInterface remote;
    // όνομα που θα χρησιμοποιείται στις κρατήσεις
    private String fullname;
    
    private JTextField polianaxorisistext,poliproorismoutext;
    private JTextField anaxorisitext,epistrofitext;
    private JTextField eisitiriatext;
    private JComboBox ptisianaxorisisbox,ptisiepistrofisbox;
    // Ο τύπος της ημερομηνίας που δέχεται
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
    public Gui(String title,String fullname,RemoteInterface remote){
        super(title+" "+fullname);
        this.fullname = fullname;
        this.remote = remote;
        setFields();
        setSeacrhTripMenu();
        setBookTripMenu();
        setVisible(true); // Να φαίνεται το frame
        setSize(400,300); // Θέτει το μέγεθος του παραθύρου
        setLocation(150,150); // Αρχική θέση στην οθόνη
        // κλείσιμο εφαρμογής με το κλείσιμο του παραθύρου
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    // θέτει το μενού με τις επιλογές αναζήτησης
    private void setSeacrhTripMenu(){
        JPanel menu = new JPanel();
        GridLayout gl = new GridLayout(1,2);
        gl.setHgap(5);
        JButton nf = new JButton("Search Trip");
        nf.addActionListener(this);
        menu.add(nf);
        
       
        this.add(menu,BorderLayout.PAGE_START);
        
    }
    // θέτει το μενού με τις επιλογές κράτησης
    private void setBookTripMenu(){
        JPanel menu = new JPanel();
        GridLayout gl = new GridLayout(1,2);
        gl.setHgap(5);
        
        JButton df = new JButton("Book Trip");
        df.addActionListener(this);
        menu.add(df);
       
        this.add(menu,BorderLayout.PAGE_END);
        
    }
    // θέτει τα πεδία στα οποία γράφει ο χρήσης
    private void setFields(){
        JPanel fields = new JPanel();
        GridLayout gl = new GridLayout(0,2);
        gl.setVgap(5);
        fields.setLayout(gl);
        fields.add(new JLabel("Search Fields"));
        fields.add(new JLabel());
        fields.add(new JLabel("Departure City"));
        polianaxorisistext = new JTextField();
        polianaxorisistext.setText("Athens");
        fields.add(polianaxorisistext);
        fields.add(new JLabel("Arrival City"));
        poliproorismoutext = new JTextField();
        poliproorismoutext.setText("Thessaloniki");
        fields.add(poliproorismoutext);
        fields.add(new JLabel("Departure"));
        anaxorisitext = new JTextField();
        anaxorisitext.setText("2023-05-29");
        fields.add(anaxorisitext);
        fields.add(new JLabel("Return"));
        epistrofitext = new JTextField();
        epistrofitext.setText("2023-05-30");
        fields.add(epistrofitext);
        fields.add(new JLabel("No of tickets"));
        eisitiriatext = new JTextField();
        eisitiriatext.setText("2");
        fields.add(eisitiriatext);
        fields.add(new JLabel("Departure Flight"));
        ptisianaxorisisbox = new JComboBox();
        fields.add(ptisianaxorisisbox);
        fields.add(new JLabel("Return Flight"));
        ptisiepistrofisbox = new JComboBox();
        fields.add(ptisiepistrofisbox);
        this.add(fields);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Search Trip")){
            ptisianaxorisisbox.removeAllItems();
            ptisiepistrofisbox.removeAllItems();
            LocalDate anaxorisi,epistrofi;
            String anaxorisistr,epistrofistr;
            String polianaxorisis,poliproorismou;
            String eisitiriastr;
            int eisitiria;
            try {
               anaxorisistr = anaxorisitext.getText();
               epistrofistr = epistrofitext.getText();
               eisitiriastr = eisitiriatext.getText();
               polianaxorisis = polianaxorisistext.getText();
               poliproorismou = poliproorismoutext.getText();
               anaxorisi = LocalDate.parse(anaxorisistr,formatter);
               epistrofi = LocalDate.parse(epistrofistr,formatter); 
               eisitiria = Integer.valueOf(eisitiriastr);
               ArrayList<String> trips = remote.searchTrip(polianaxorisis, poliproorismou, anaxorisistr, epistrofistr, eisitiria);
               if (trips.isEmpty()){
                    
                    JOptionPane.showMessageDialog(rootPane, "No flights available","Search Results", JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                   for(String trip:trips){
                       String []data = trip.split(",");
                       if (data[1].equals(polianaxorisis))
                           ptisianaxorisisbox.addItem(trip);
                       else 
                           ptisiepistrofisbox.addItem(trip);
                       
                   }
               }
            
            
            }
            catch(java.time.format.DateTimeParseException ex){
                JOptionPane.showMessageDialog(rootPane, "Dates must be in uuuu/MM/dd form","Date Error", JOptionPane.WARNING_MESSAGE);
            }
            catch(java.lang.NumberFormatException ex){
                JOptionPane.showMessageDialog(rootPane, "No of tickets must be a number","No of tickets Error", JOptionPane.WARNING_MESSAGE);
            } catch (RemoteException ex) {
                Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (e.getActionCommand().equals("Book Trip")){
            String eisitiriastr;
            int eisitiria;
            if (ptisianaxorisisbox.getSelectedItem()==null || ptisiepistrofisbox.getSelectedItem()==null){
                JOptionPane.showMessageDialog(rootPane, "No selected flights to book","Booking Results", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                try{
                    int ptisi_anaxorisis = Integer.valueOf(((String)ptisianaxorisisbox.getSelectedItem()).split(",")[0]);
                    int ptisi_epistrofis = Integer.valueOf(((String)ptisiepistrofisbox.getSelectedItem()).split(",")[0]);
                    eisitiriastr = eisitiriatext.getText();
                    eisitiria = Integer.valueOf(eisitiriastr);
                    double timi = remote.bookTrip(ptisi_anaxorisis,ptisi_epistrofis,eisitiria,fullname);
                    if (timi==0.0){
                        JOptionPane.showMessageDialog(rootPane, "No available tickets to book","Booking Results", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else{
                        JOptionPane.showMessageDialog(rootPane, "Booking done. Total price:"+timi,"Booking Results", JOptionPane.INFORMATION_MESSAGE);
                    }

                } catch(java.lang.NumberFormatException ex){
                    JOptionPane.showMessageDialog(rootPane, "No of tickets must be a number","No of tickets Error", JOptionPane.WARNING_MESSAGE);
                }catch (RemoteException ex) {
                    Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
      
}
