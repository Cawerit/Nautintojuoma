package server;


import java.rmi.RemoteException;

public interface INautintojuomaService extends java.rmi.Remote {
    public String login(String name) throws RemoteException;
}
