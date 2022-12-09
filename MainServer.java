
package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;

public class MainServer extends JFrame {

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket connection;
    private ServerSocket server;
    private int totalClients = 100;
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

  
    public MainServer() {
        initComponents();
        this.setTitle("Server");
        this.setVisible(true);
        one = new ArrayList<>();
        two = new ArrayList<>();
        ScoreClient=0;
        ScoreServer=0;
    }
    
    public void startRunning()
    {
        try
        {
            server=new ServerSocket(port, totalClients);
            while(true)
            {
                try
                {
                    connection=server.accept();

                    output = new ObjectOutputStream(connection.getOutputStream());
                    output.flush();
                    input = new ObjectInputStream(connection.getInputStream());

                    whileChatting();

                }catch(EOFException eofException)
                {
                }
            }
        }
        catch(IOException ioException)
        {
                ioException.printStackTrace();
        }
    }

    

    private void initComponents() {

        Icon pierre = new ImageIcon("asset/pierre.PNG");
        Icon feuille = new ImageIcon("asset/feuille.PNG");
        Icon ciseau = new ImageIcon("asset/ciseau.PNG");


        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        chatArea = new javax.swing.JTextArea();

        jButton1 = new javax.swing.JButton(pierre);
        jButton2 = new javax.swing.JButton(feuille);
        jButton3 = new javax.swing.JButton(ciseau);


        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(null);

        chatArea.setColumns(20);
        chatArea.setRows(5);
        jScrollPane1.setViewportView(chatArea);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(10, 150, 380, 100);
        chatArea.setBackground(Color.BLACK);
        chatArea.setForeground(Color.WHITE);

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

        this.add(jPanel1);

        setSize(new java.awt.Dimension(417, 300));
        setLocationRelativeTo(null);
    }

    
        
    private void whileChatting() throws IOException
    {
         String message="";    
         do{
                 try
                 {
                         message = (String) input.readObject();
                         two.add(message);
                         if(two.size()==one.size()){
                            calculPoint(this);
                            chatArea.append("\n Client - "+two.get(two.size()-1));
                            chatArea.append("\n");
                        
                          }
                          
                 }catch(ClassNotFoundException classNotFoundException)
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
            chatArea.append("\nServer - "+message);
            one.add(message);
            }else{
                JOptionPane.showMessageDialog(null,"Nooooo!!!","Warning",JOptionPane.WARNING_MESSAGE);
            }
            
            if(two.size()==one.size()){
                calculPoint(this);
                chatArea.append("\n Client - "+two.get(two.size()-1));
                chatArea.append("\n");
            }
            System.out.println(ScoreClient);

        }
        catch(IOException ioException)
        {
            chatArea.append("\n Unable to Send Message");
        }
    }


    public void calculPoint(MainServer server){
        String A= (String) server.getOne().get(server.getOne().size()-1);
        String B= (String) server.getTwo().get(server.getTwo().size()-1);

        if(A.equalsIgnoreCase("pierre") && B.equalsIgnoreCase("ciseau")){
            server.setScoreServer(server.getScoreServer()+1);
        }if(B.equalsIgnoreCase("pierre") && A.equalsIgnoreCase("ciseau")){
            server.setScoreClient(server.getScoreClient()+1);
        }

        if(A.equalsIgnoreCase("ciseau") && B.equalsIgnoreCase("feuille")){
            server.setScoreServer(server.getScoreServer()+1);
        }if(B.equalsIgnoreCase("ciseau") && A.equalsIgnoreCase("feuille")){
            server.setScoreClient(server.getScoreClient()+1);
        }

        if(A.equalsIgnoreCase("feuille") && B.equalsIgnoreCase("pierre")){
            server.setScoreServer(server.getScoreServer()+1);
        }if(B.equalsIgnoreCase("feuille") && A.equalsIgnoreCase("pierre")){
            server.setScoreClient(server.getScoreClient()+1);
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
    private javax.swing.JTextArea chatArea;

    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;

    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel status;
 
    public void paint(Graphics g) {	
        super.paint(g);	
        g.setFont(new Font("SansSerif",Font.PLAIN ,15));
        g.drawString( "Server : " +  String.valueOf(ScoreServer), 50, 50);
        g.drawString( "Client : " + String.valueOf(ScoreClient) , 320, 50);
        repaint();
    }

    
    public static void main(String[] args) {
		    
		MainServer myServer=new MainServer();
                myServer.startRunning();
	}
}
