package server;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class NautintojuomaService implements INautintojuomaService {

    public NautintojuomaService () throws RemoteException {
        super();
    }


    public void login(String name){
        System.out.println("VOUUUU serverille tulee " + name);
    }

}
