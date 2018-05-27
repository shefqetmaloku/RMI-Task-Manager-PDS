package Server;

import Client.StartClient;
import Interfaces.ServerInterface;
import Interfaces.ClientInterface;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

//Klasa që implementon ServerInterface
public class ChatServer  extends UnicastRemoteObject implements ServerInterface {

    //Krijimi i ArrayListës kur ruhen klientët e kyçur!
    private ArrayList v=new ArrayList();

    //Konstruktori
    public ChatServer() throws RemoteException{}
    private StartClient client;

    //Implementimi i metodave të Interface:

    //Login i klientit
    public boolean login(ClientInterface a) throws RemoteException{
        a.retrieveMessage("You have Connected successfully.");
        broadcastMessage(a.getName()+ " has just connected.");
        //Shtimi i klientit të sapo regjistruar në ArrayList
        v.add(a);
        return true;
    }

    //Metoda për transmetim të mesazhit
    public void broadcastMessage(String s) throws RemoteException{
        for(int i=0;i<v.size();i++){
            try{
                ClientInterface tmp=(ClientInterface)v.get(i);
                tmp.retrieveMessage(s);
            }catch(Exception e){

            }
        }
    }

    //ArrayLista që përmban klientët e kyçur
    public ArrayList connectedClients() throws RemoteException{
        return v;
    }

}
