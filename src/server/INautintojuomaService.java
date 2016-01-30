package server;


import server.machines.IMachine;
import server.machines.NautintojuomaMachine;
import server.machines.Silo;
import server.machines.SiloLoader;

import java.rmi.RemoteException;
import java.util.HashMap;

public interface INautintojuomaService extends java.rmi.Remote {
    public void login(String name) throws RemoteException;
    public void logOut(String token) throws RemoteException;

    public void reserve(NautintojuomaMachine machine, String name) throws RemoteException;
    public HashMap<NautintojuomaMachine, IMachine> pullState() throws RemoteException;
}
