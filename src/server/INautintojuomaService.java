package server;


import java.rmi.RemoteException;

public interface INautintojuomaService extends java.rmi.Remote {
    public void login(String name) throws RemoteException;
}
