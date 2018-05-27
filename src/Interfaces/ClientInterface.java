package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote{

    //Stringu që merr emrin e klientit
    public String getName()throws RemoteException;

    //Metoda që merr mesazhin
    public void retrieveMessage(String msg) throws RemoteException;
}