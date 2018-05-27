package Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;


//Klasa që krijon një login-formë ku mund të zgjedhni të kyçeni si Klient apo Administrator
public class ConnectDialog {

    JFrame frame;

    boolean asAdm;

    //Konstruktori
    public ConnectDialog() {

        //Krijimi i GUI
        frame = new JFrame("Connect as:");
        JPanel main = new JPanel();
        JPanel center = new JPanel();
        JPanel top = new JPanel();
        JPanel bottom = new JPanel();
        JPanel radioButtons = new JPanel();
        final JButton connect = new JButton("Connect");
        bottom.setSize(20, 50);


        center.setLayout(new BorderLayout(5, 5));
        center.setBorder(new EmptyBorder(0, 30, 0, 30));

        final JPasswordField password = new JPasswordField();
        password.setEnabled(false);

        JLabel connnctAs = new JLabel("Connect as:");

        main.setLayout(new BorderLayout(5, 5));
        main.setBorder(new EmptyBorder(20, 20, 20, 20));


        JRadioButton client = new JRadioButton("Client");
        JRadioButton administrator = new JRadioButton("Administrator");
        ButtonGroup bG = new ButtonGroup();
        bG.add(client);
        bG.add(administrator);

        radioButtons.setLayout(new FlowLayout());
        radioButtons.add(client);
        radioButtons.add(administrator);

        top.add(connnctAs, BorderLayout.EAST);
        main.add(top, BorderLayout.NORTH);
        center.add(radioButtons, BorderLayout.NORTH);
        center.add(password, BorderLayout.SOUTH);
        main.add(center, BorderLayout.CENTER);
        bottom.add(connect, BorderLayout.CENTER);
        main.add(bottom, BorderLayout.SOUTH);
        frame.setContentPane(main);
        frame.setSize(300, 210);
        frame.setVisible(true);


        //Radio-butoni për tu kyçur si Administrator
        administrator.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                password.setEnabled(true);
                connect.setEnabled(true);
                asAdm = true;
            }
        });

        //Radio-butoni për tu kyçur si Klient
        client.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                password.setEnabled(false);
                connect.setEnabled(true);
                asAdm = false;
            }
        });

        //Butoni Connect
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Nëse është zgjedhur si Administrator:
                if (asAdm) {

                    //Leximi i passwordit
                    String passwordTxt = String.valueOf(password.getPassword());

                    //Kontrollimi nëse nuk është shtypur passwordi
                    if (passwordTxt.isEmpty()) {

                        JOptionPane.showMessageDialog(frame, "You have to type a password \n " +
                                "to connect as administrator!");

                        //Kontrollimi nëse passwordi është i saktë
                    } else if (!passwordTxt.equals("123456")) {

                        JOptionPane.showMessageDialog(frame, "Password is incorrect", "Wrong password", JOptionPane.ERROR_MESSAGE);
                    }
                    //Nëse passwordi është i sakt, mundësohet kyçja si administrator
                    if(passwordTxt.equals("123456")){

                        StartClient.connectedAsAdministrator(true);
                        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    }

                }else{
                    //Në të kundërten smund të kyçesh si administrator
                    StartClient.connectedAsAdministrator(false);
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                }
            }
        });
    }

    public static void main(String[] args) {

        ConnectDialog c = new ConnectDialog();
    }

}

