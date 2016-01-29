package server;


import server.machines.Silo;
import server.machines.SiloLoader;

import java.rmi.RemoteException;

public interface INautintojuomaService extends java.rmi.Remote {
    public void login(String name) throws RemoteException;
    public SiloLoader getSiloLoader() throws RemoteException;
    public Silo getSilo(int index) throws RemoteException;
    public void logOut(String token) throws RemoteException;
}
