package server;


import server.machines.IMachine;
import server.machines.NautintojuomaMachine;
import server.machines.Silo;
import server.machines.SiloLoader;

import java.rmi.RemoteException;
import java.util.HashMap;

public interface INautintojuomaService extends java.rmi.Remote {
    void login(String name) throws RemoteException;
    void logOut(String name) throws RemoteException;

    void toggleReservation(NautintojuomaMachine machine, String name) throws RemoteException;
    HashMap<NautintojuomaMachine, IMachine> pullState() throws RemoteException;
    void fillSilos(String name) throws RemoteException;
    void fillProcessors(NautintojuomaMachine usingLoader, int amount, String username) throws RemoteException;
}
