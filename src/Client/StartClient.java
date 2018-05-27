package Client;

import javax.swing.*;
import javax.swing.border.*;

import Interfaces.ClientInterface;
import Interfaces.ServerInterface;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.*;

//Metoda që Starton Klientin
public class StartClient {

    private Client client;
    private static ServerInterface server;
    public String TmMessage = "GETTM";
    public String showTmMessage = "SHOWTMadm";
    public String killTmMessage = "KILLtm";

    String selectedClientName;

    static StartClient startClient;

    public static boolean administrator;

    String password = "";
    String myName;

    boolean connected = false;
    DefaultListModel listModel = new DefaultListModel();
    DefaultListModel admListModel = new DefaultListModel();
    DefaultListModel checkList = new DefaultListModel();


    JTextArea chatArea;
    JTextField tf, ip, name;
    JButton connect,RegClient;
    JList clientsList;
    JList administratorList;
    JFrame frame;
    JButton getTm;
    JPanel clients = new JPanel();
    JPanel admPanel = new JPanel();
    JPanel connectedPanel = new JPanel();

    private static final String TASKLIST = "tasklist.exe /nh";
    private static final String KILL = "taskkill /F /IM ";

    public static void main(String[] args) {
        System.out.println("Hello World !");
        startClient = new StartClient();
    }


    //Konstruktori
    public StartClient() {
        frame = new JFrame("Chat");

        JPanel main = new JPanel();
        JPanel top = new JPanel();
        JPanel cn = new JPanel();
        JPanel bottom = new JPanel();
        JPanel butonat = new JPanel();

        tf = new JTextField();
        chatArea = new JTextArea();
        RegClient = new JButton("Login");
        connect = new JButton("Cancel");
        JButton bt = new JButton("Send");
        bt.setEnabled(false);
        tf.setText("Login First");
        getTm = new JButton("Get TM");
        clientsList = new JList();
        clientsList.setBorder(new EmptyBorder(3, 10, 3, 10));

        administratorList = new JList();
        administratorList.setBorder(new EmptyBorder(3, 10, 3, 10));

        connectedPanel.setLayout(new BorderLayout(5, 5));
        main.setLayout(new BorderLayout(5, 5));
        butonat.setLayout(new GridLayout(1,1));
        top.setLayout(new GridLayout(1, 0, 5, 5));
        cn.setLayout(new BorderLayout(5, 5));
        bottom.setLayout(new BorderLayout(5, 5));
        top.add(RegClient);
        top.add(connect);

        cn.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        cn.add(connectedPanel, BorderLayout.EAST);
        bottom.add(tf, BorderLayout.CENTER);
        bottom.add(bt, BorderLayout.EAST);
        bottom.add(butonat,BorderLayout.SOUTH);
        main.add(top, BorderLayout.NORTH);
        main.add(cn, BorderLayout.CENTER);
        main.add(bottom, BorderLayout.SOUTH);
        main.setBorder(new EmptyBorder(10, 10, 10, 10));

        //Butoni që ndalon ekzekutimin e programit
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        //Butoni që hap dritaren për regjistrim të klientit,emri dhe IP e serverit
        RegClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                name = new JTextField();
                ip = new JTextField();
                JButton connectBt = new JButton("Connect");
                JButton cancel= new JButton("Cancel");
                JPanel mainLogin = new JPanel();
                JPanel loginTop = new JPanel();
                JPanel loginCenter = new JPanel();
                JPanel loginBottom = new JPanel();


                mainLogin.setLayout(new BorderLayout(15,15));
                loginTop.setLayout(new FlowLayout());
                loginCenter.setLayout(new GridLayout(2,1,5,5));
                loginCenter.setBorder(new EmptyBorder(0,10,0,10));
                loginBottom.setLayout(new FlowLayout());

                loginTop.add(new JLabel("Shënoni Emrin dhe IP e serverit:"));

                loginCenter.add(new JLabel("Emri i klientit"));
                loginCenter.add(name);
                loginCenter.add(new JLabel("IP e Serverit"));
                loginCenter.add(ip);

                loginBottom.add(connectBt);
                loginBottom.add(cancel);

                mainLogin.add(loginTop,BorderLayout.NORTH);
                mainLogin.add(loginCenter,BorderLayout.CENTER);
                mainLogin.add(loginBottom,BorderLayout.SOUTH);

                JFrame korniza = new JFrame("Connect");
                korniza.setContentPane(mainLogin);
                korniza.setSize(300,185);
                korniza.setVisible(true);
                cancel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                });

                //Butoni që ja bën SUBMIT për regjistrim
                connectBt.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!connected) {
                            if (name.getText().length() < 2) {
                                JOptionPane.showMessageDialog(frame, "You need to type a name.");
                                return;
                            }
                            if (ip.getText().length() < 2) {
                                JOptionPane.showMessageDialog(frame, "You need to type an IP.");
                                return;
                            }

                            ConnectDialog connect = new ConnectDialog();
                            bt.setEnabled(true);
                            tf.setText("");
                            RegClient.setEnabled(false);

                        } else {

                            try {
                                startClient.doConnect();

                            } catch (RemoteException e1) {
                                e1.printStackTrace();
                            }

                        }
                        korniza.dispose();
                    }
                });
            }
        });

        //Kërkesa nga administratori t'i qaset TaskManagerit të klientit
        getTm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int selectedIndex = clientsList.getSelectedIndex();

                if (selectedIndex == -1) {

                    JOptionPane.showMessageDialog(frame, "Select a client first");
                } else {
                    selectedClientName = (String) listModel.getElementAt(selectedIndex);

                    String getTmMessage = "GETTM" + selectedClientName + "^";
                    sendFromTm(getTmMessage);
                }
            }
        });

        //Butoni që dërgon mesazhin në chat(SEND)
        bt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendText();
            }
        });

        //Text Field ku shënohet mesazhi për tu dërguar në chat
        tf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendText();
            }
        });

        //Korniza
        frame.setContentPane(main);
        frame.setSize(600, 600);
        frame.setVisible(true);
    }

    //Metoda që dërgon tekstin
    public void sendText() {
        if (connect.getText().equals("Connect")) {
            JOptionPane.showMessageDialog(frame, "You need to connect first.");
            return;
        }
        String st = tf.getText();
        st = name.getText() + ": " + st;
        tf.setText("");

        try {
            server.broadcastMessage(st);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeMsg(String st) {

        int removeName = st.indexOf(":");
        int tmIndex = st.indexOf("^");

        if (tmIndex == -1 || removeName == -1) {
            chatArea.setText(chatArea.getText() + "\n" + st);

            int index = st.indexOf(" ");
            String connected = st.substring(index, st.length() - 1);
            if (connected.equals(" has just connected")) {


                String name = st.substring(0, index);
                listModel.addElement(name);
                clientsList.setModel(listModel);
            }
        } else {

            if(!administrator) {
                String fromAdm = st.substring(removeName + 1, tmIndex);
                String fromAdmMessage = TmMessage + myName;

                String killAppsMessage = killTmMessage + myName;

                if (fromAdm.trim().toLowerCase().equals(fromAdmMessage.trim().toLowerCase())) {

                    final Object[] options = {"Allow", "Deny",};

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {

                            //Kërkesa nga administratori për t'iu qasur TM të klientit
                            int choice = JOptionPane.showOptionDialog(frame,
                                    "Administrator wants to have access in your Task Manager!",
                                    "Warning!", //String title
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    options,
                                    null);
                            if (choice == 0) {

                                //Paraqitja e listës së proceseve ,ndarja e tyre me ,
                                ArrayList<String> appNames = listRunningProcesses();
                                StringBuilder str = new StringBuilder();

                                for (int i = 0; i < appNames.size(); i++) {
                                    str.append(appNames.get(i)).append(",");
                                }

                                String size = " " + appNames.size();
                                str.append(size).append(",");
                                System.out.println("allowed");
                                sendTmToAdministrator(showTmMessage + myName + "^" + str.toString());

                            } else {

                                System.out.println("denyed");
                            }
                        }
                    });

                }else if(fromAdm.trim().toLowerCase().equals(killAppsMessage.trim().toLowerCase())){

                    int appsSize = Integer.parseInt(st.substring(st.length() - 2,st.length() - 1).trim());

                    //Lista e proceseve të klienti
                    String appListFromCLient = st.substring(tmIndex + 1, st.length());
                    System.out.println(appListFromCLient);


                    StringTokenizer stringTokenizer = new StringTokenizer(appListFromCLient, ",");
                    ArrayList<String> killlist = new ArrayList<>();

                    for (int i = 0; i < appsSize; i++) {

                        killlist.add(stringTokenizer.nextToken());
                    }

                    for (int j = 0; j < killlist.size(); j++)
                        killProcess(killlist.get(j));
                }

            }else{

                String fromAdm = st.substring(removeName + 1, tmIndex);
                String fromAdmMessage = showTmMessage + selectedClientName;
                if (fromAdm.trim().toLowerCase().equals(fromAdmMessage.trim().toLowerCase())) {

                    int appsSize = Integer.parseInt(st.substring(st.length() - 3,st.length() - 1).trim());

                    String appListFromCLient = st.substring(tmIndex + 1, st.length());
                    System.out.println(appListFromCLient);

                    StringTokenizer stringTokenizer = new StringTokenizer(appListFromCLient, ",");
                    ArrayList<String> tmList = new ArrayList<>();

                    for (int i = 0; i < appsSize; i++) {

                        tmList.add(stringTokenizer.nextToken());
                    }

                    TaskManager c = new TaskManager(selectedClientName ,tmList);
                }
            }
        }
    }

    //Metoda që ndërpret punën e një procesi
    public static void killProcess(String serviceName) {

        try {
            Runtime.getRuntime().exec(KILL + serviceName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Metoda që shton klientët e regjistruar në listë
    public void updateUsers(int c, String name, ArrayList v) {

        myName = name;
        if (c == 1)
            for (int i = 0; i < v.size(); i++) {
                try {
                    String tmp = ((ClientInterface) v.get(i)).getName();
                    listModel.addElement(tmp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        else {
            try {
                int index = listModel.indexOf(name);
                v.remove(index);
                listModel.removeElement(name);
                checkList.removeElement(name);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        clientsList.setModel(listModel);
        clients.setLayout(new BorderLayout(5, 5));
        clients.add(new JLabel("Clients:"), BorderLayout.NORTH);
        clients.add(clientsList, BorderLayout.CENTER);

        administratorList.setModel(admListModel);
        admPanel.setLayout(new BorderLayout(5, 5));
        admPanel.add(new JLabel("Administrator:"), BorderLayout.NORTH);
        admPanel.add(administratorList, BorderLayout.CENTER);

        connectedPanel.add(clients, BorderLayout.CENTER);

        if (administrator) {
            connectedPanel.add(getTm, BorderLayout.SOUTH);
        }

    }

    //Në këtë ArrayList ruhen proceset e kompjuterit të klientit
    public static ArrayList<String> listRunningProcesses() {
        ArrayList<String> processes = new ArrayList<String>();
        try {
            String line;
            Process p = Runtime.getRuntime().exec(TASKLIST);
            BufferedReader input = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                if (!line.trim().equals("")) {

                    if (line.contains("Console") && line.contains(".exe")) {
                        if (!processes.contains(line.substring(0, line.indexOf(" "))))
                            processes.add(line.substring(0, line.indexOf(" ")));
                    }
                }

            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
        return processes;
    }


    public static void sendFromTm(String text) {

        text = "Administrator" + ": " + text;

        try {
            server.broadcastMessage(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendTmToAdministrator(String text) {

        text = "Client" + ": " + text;

        try {
            server.broadcastMessage(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Kyçja si Administrator
    public static void connectedAsAdministrator(boolean adm) {

        administrator = adm;
        try {

            startClient.doConnect();
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }


    //Metoda për kyçje
    public void doConnect() throws RemoteException {
        if (!connected) {
            if (name.getText().length() < 2) {
                JOptionPane.showMessageDialog(frame, "You need to type a name.");
                return;
            }
            if (ip.getText().length() < 2) {
                JOptionPane.showMessageDialog(frame, "You need to type an IP.");
                return;
            }

            try {
                client = new Client(name.getText());
                client.setGUI(this);
                server = (ServerInterface) Naming.lookup("rmi://"+ip.getText()+":4617/findme");

                for (int i = 0; i < server.connectedClients().size(); i++) {
                    try {
                        String tmp = ((ClientInterface) server.connectedClients().get(i)).getName();
                        checkList.addElement(tmp.toLowerCase());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (checkList.contains(client.getName().toLowerCase())) {

                    JOptionPane.showMessageDialog(frame, "Client already Registered!.");
                    return;
                }
                server.login((ClientInterface) client);
                updateUsers(1, name.getText(), server.connectedClients());
                connect.setText("Disconnect");
                connected = true;


            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "ERROR, we couldn't connect....");
            }
        } else {
            try {
                updateUsers(0, name.getText(), server.connectedClients());
                connected = false;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            connect.setText("Cancel");
        }
    }
}