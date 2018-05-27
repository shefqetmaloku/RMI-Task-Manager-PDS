package Server;

import java.rmi.*;


//Klasa që starton Serverin
public class StartServer {
    public static void main(String[] args) {
        try {

            //Krijimi i regjistrit në portin e dhënë
            java.rmi.registry.LocateRegistry.createRegistry(4617);


            //System.setProperty("java.security.policy","C:\\Users\\Shefqet\\Desktop\\Fakultet\\Loto\\src\\policy.txt");
            //System.setSecurityManager(new SecurityManager());

            ChatServer server = new ChatServer();

            //Krijimi i referencës së objektit në IP e caktuar,portin dhe emrin e objektit
            Naming.rebind("rmi://192.168.137.212:4617/findme", server);
            System.out.println("Serveri eshte gati");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


}