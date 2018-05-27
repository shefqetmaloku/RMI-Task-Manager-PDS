package Interfaces;
import java.rmi.*;
import java.util.*;

public interface ServerInterface extends Remote{

    //Metoda për Login të Klientit
    public boolean login(ClientInterface client) throws RemoteException;

    //ArrayLista në të cilën shtohen klientët e kyçur
    public ArrayList connectedClients() throws RemoteException;

    //Metoda për transmetim të mesazhit
    public void broadcastMessage(String message) throws RemoteException;

}