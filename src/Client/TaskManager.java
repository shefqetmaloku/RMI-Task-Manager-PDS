package Client;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

/*Klasa që merr të gjitha proceset e kompjuterit të Klientit në momentin kur Administratorit i jepet
 e drejta për t'iu çasur TaskManagerit të Klientit*/

public class TaskManager
        implements ItemListener {

    //ArrayListat ku do të ruhen proceset e klientit,emrat e proceseve dhe proceset e selektuara për ndalim të punës së tyre
    ArrayList<JCheckBox> apps = new ArrayList<>();
    ArrayList<String> appNames = new ArrayList<>();
    ArrayList<String> selectedApps = new ArrayList<>();

    //Komanda që ndërpret punën e një procesi
    private static final String KILL = "taskkill /F /IM ";

    public String killTmMessage = "KILLtm";

    String clientName;

    JFrame frame;

    public TaskManager(final String clientName, ArrayList<String> names) {

        appNames = names;
        this.clientName = clientName;

        JButton endTaskButton = new JButton("End Task");
        JButton startProcess = new JButton("Start Task");

        JList list = new JList();
        DefaultListModel listModel = new DefaultListModel();

        JPanel checkPanel = new JPanel(new GridLayout(0, 4, 3, 3));
        JPanel main = new JPanel();
        JPanel butonat = new JPanel();

        //Listimi i proceseve të Klientit dhe shfaqja e tyre në panelë
        for (int i = 0; i < appNames.size(); i++) {

            listModel.addElement(appNames.get(i));
            apps.add(i, new JCheckBox((String) listModel.get(i)));
            apps.get(i).addItemListener(this);
            checkPanel.add(apps.get(i));
        }


        list.setModel(listModel);

        //Krijimi i GUI
        main.setLayout(new BorderLayout(5, 5));
        checkPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        checkPanel.setOpaque(true);

        main.add(new JScrollPane(checkPanel), BorderLayout.CENTER);
        butonat.add(startProcess);
        butonat.add(endTaskButton);
        main.add(butonat, BorderLayout.SOUTH);

        //Krijimi i Dritares (Frame)
        frame = new JFrame("Task Manager");

        frame.add(main, BorderLayout.CENTER);
        frame.setContentPane(main);
        frame.pack();
        frame.setVisible(true);


        //Butoni i cili mund të startojë një proces tek Klienti
        startProcess.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Process p1 = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + JOptionPane.showInputDialog("Sheno Procesin"));

                }
                catch (Exception e2)
                {
                    JOptionPane.showMessageDialog(frame,"Nuk Mund te gjindet ky proces");
                }
            }
        });


        //Butoni i cili ndërpret punën e një procesi tek Klienti
        endTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                StringBuilder str = new StringBuilder();

                if(selectedApps.size() > 0) {

                    for (int i = 0; i < selectedApps.size(); i++) {
                        str.append(selectedApps.get(i)).append(",");
                    }
                    str.append(selectedApps.size()).append(",");

                    StartClient.sendFromTm(killTmMessage + clientName + "^" + str.toString());
                    //frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                }
                else
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });

    }

    //Metoda që ndalon punën e procesit tek Klienti
    public static void killProcess(String serviceName) {

        try {
            Runtime.getRuntime().exec(KILL + serviceName);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //Metoda që kontrollon gjendjen e proceseve,nëse është shtuar ndonjë proces apo është larguar nga lista
    public void itemStateChanged(ItemEvent e) {

        Object source = e.getItemSelectable();
        String a = source.toString();
        int length = a.length();
        int last = a.lastIndexOf("text");
        String name = a.substring(last + 5, length - 1);

        if (e.getStateChange() == ItemEvent.SELECTED) {

            if (!selectedApps.contains(name))
                selectedApps.add(name);
        }

        if (e.getStateChange() == ItemEvent.DESELECTED) {

            selectedApps.remove(name);
        }
    }

    public static void main(String[] args) {

    }
}