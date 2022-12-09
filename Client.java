

package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JOptionPane;
import java.awt.*;
import javax.swing.*;

public class Client extends javax.swing.JFrame {

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message="";
    private String serverIP;
    private Socket connection;
    private int port = 6789;
    ArrayList one;
    ArrayList two;
    int ScoreClient;
    int ScoreServer;
    
    
    public int getScoreClient() {
        return ScoreClient;
    }


    public void setScoreClient(int scoreClient) {
        ScoreClient = scoreClient;
    }


    public int getScoreServer() {
        return ScoreServer;
    }


    public void setScoreServer(int scoreServer) {
        ScoreServer = scoreServer;
    }


    public ArrayList getOne() {
        return one;
    }


    public void setOne(ArrayList one) {
        this.one = one;
    }


    public ArrayList getTwo() {
        return two;
    }


    public void setTwo(ArrayList two) {
        this.two = two;
    }


    public Client(String s) {
        
        initComponents();
        
        this.setTitle("Client");
        this.setVisible(true);
        status.setVisible(true);
        serverIP = s;
        one = new ArrayList<>();
        two = new ArrayList<>();
    }

    private void initComponents() {

        Icon pierre = new ImageIcon("asset/pierre.PNG");
        Icon feuille = new ImageIcon("asset/feuille.PNG");
        Icon ciseau = new ImageIcon("asset/ciseau.PNG");


        jPanel1 = new JPanel();
        jTextField1 = new JTextField();


        jPanel1.setBackground(Color.GRAY);
        jButton1 = new javax.swing.JButton(pierre);
        jButton2 = new javax.swing.JButton(feuille);
        jButton3 = new javax.swing.JButton(ciseau);

        jScrollPane1 = new JScrollPane();
        chatArea = new JTextArea();
        jLabel2 = new JLabel();
        status = new JLabel();
        jLabel1 = new JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setLayout(null);

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jPanel1.add(jTextField1);
        jScrollPane1.setBounds(10, 150, 380, 100);


        jPanel1.add(jButton1);
        jPanel1.add(jButton2);
        jPanel1.add(jButton3);

        jButton1.setBounds(10, 50, 80, 80);
        jButton2.setBounds(160, 50, 80, 80);
        jButton3.setBounds(310, 50, 80, 80);


        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendMessage("pierre");
            }
        });

        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendMessage("feuille");
            }
        });

        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendMessage("ciseau");
            }
        });


        chatArea.setColumns(20);
        chatArea.setRows(5);

        chatArea.setBackground(Color.BLACK);
        chatArea.setForeground(Color.WHITE);
        jScrollPane1.setViewportView(chatArea);

        jPanel1.add(jScrollPane1);
    
        jPanel1.add(jLabel2);
        jLabel2.setBounds(30, 30, 150, 20);

        this.add(jPanel1);

        setSize(new java.awt.Dimension(414, 300));
        setLocationRelativeTo(null);
    }
    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {

        sendMessage(jTextField1.getText());
	    jTextField1.setText("");
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {

       sendMessage(jTextField1.getText());
	    jTextField1.setText("");
    }

    
    public void startRunning()
    {
       try
       {
            status.setText("Attempting Connection ...");
            try
            {
                connection = new Socket(InetAddress.getByName(serverIP),port);
            }catch(IOException ioEception)
            {
                    JOptionPane.showMessageDialog(null,"Server Might Be Down!","Warning",JOptionPane.WARNING_MESSAGE);
            }
            status.setText("Connected to: " + connection.getInetAddress().getHostName());


            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());

            whileChatting();
       }
       catch(IOException ioException)
       {
            ioException.printStackTrace();
       }
    }
    
    private void whileChatting() throws IOException
    {
      jTextField1.setEditable(true);
      do{
              try
              {
                      message = (String) input.readObject();                    
                      two.add(message);
                      if(two.size()==one.size()){
                        calculPoint(this);
                        chatArea.append("\n Server -"+two.get(two.size()-1));
                        chatArea.append("\n");
                      }                     
              }
              catch(ClassNotFoundException classNotFoundException)
              {
              }


      }while(!message.equals("Client - END"));
    }
  
    
    private void sendMessage(String message)
    {
        try
        {
            if(two.size()==one.size() || two.size()>one.size())
            {   
           
            output.writeObject(message);
            output.flush();
            chatArea.append("\nClient - "+message);
            one.add(message);
            }else{
                JOptionPane.showMessageDialog(null,"Nooooo!!!","Warning",JOptionPane.WARNING_MESSAGE);
            }
            
            if(two.size()==one.size()){
                calculPoint(this);
                chatArea.append("\n Server - "+two.get(two.size()-1));
                chatArea.append("\n");
            }
        }
        catch(IOException ioException)
        {
            chatArea.append("\n Unable to Send Message");
        }
    }
  
    public void calculPoint(Client server){
        String A= (String) server.getOne().get(server.getOne().size()-1);
        String B= (String) server.getTwo().get(server.getTwo().size()-1);

        if(A.equalsIgnoreCase("pierre") && B.equalsIgnoreCase("ciseau")){
            server.setScoreClient(server.getScoreClient()+1);
        }if(B.equalsIgnoreCase("pierre") && A.equalsIgnoreCase("ciseau")){
            server.setScoreServer(server.getScoreServer()+1);
        }

        if(A.equalsIgnoreCase("ciseau") && B.equalsIgnoreCase("feuille")){
            server.setScoreClient(server.getScoreClient()+1);
        }if(B.equalsIgnoreCase("ciseau") && A.equalsIgnoreCase("feuille")){
            server.setScoreServer(server.getScoreServer()+1);
        }

        if(A.equalsIgnoreCase("feuille") && B.equalsIgnoreCase("pierre")){
            server.setScoreClient(server.getScoreClient()+1);
        }if(B.equalsIgnoreCase("feuille") && A.equalsIgnoreCase("pierre")){
            server.setScoreServer(server.getScoreServer()+1);
        }

        System.out.println(A);
        System.out.println(B);

        if(server.getScoreServer()>=3){
            JOptionPane.showMessageDialog(null,"Victoire server","Warning",JOptionPane.WARNING_MESSAGE);
        }
        if(server.getScoreClient()>=3){
            JOptionPane.showMessageDialog(null,"Victoire Client","Warning",JOptionPane.WARNING_MESSAGE);     
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JTextArea chatArea;
    private JButton jButton1;
    private JButton jButton2;
    private JButton jButton3;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JTextField jTextField1;
    private JLabel status;

    // End of variables declaration//GEN-END:variables
    public void paint(Graphics g) {	
        super.paint(g);	
        g.setFont(new Font("SansSerif",Font.PLAIN ,15));
        g.drawString( "Server : " +  String.valueOf(ScoreServer), 50, 50);
        g.drawString( "Client : " + String.valueOf(ScoreClient) , 320, 50);
        repaint();
    }

     public static void main(String[] args) 
    {
        Client client=new Client("127.0.0.1");
        client.startRunning();
    }
}
