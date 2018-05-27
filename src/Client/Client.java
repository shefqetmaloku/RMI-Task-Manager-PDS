package Client;

import Interfaces.ClientInterface;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

//Klasa që implementon ClientInterface
public class Client extends UnicastRemoteObject implements ClientInterface {

    private String name;
    private StartClient startClient;


    //Konstruktori
    public Client (String n) throws RemoteException {
        name=n;
    }

    //Implementimi i metodave të premtuara në Interface:

    //Metoda që merr mesazhin
    public void retrieveMessage(String st) throws RemoteException{
        startClient.writeMsg(st);
    }

    //Metoda që kthen emrin e klientit
    public String getName() throws RemoteException{
        return name;
    }

    public void setGUI(StartClient t){

        startClient=t ;
    }

}
