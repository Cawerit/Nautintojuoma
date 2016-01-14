package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class MainServer {

    public static void main ( String args[] ) throws Exception {
        try {
            NautintojuomaService process = new NautintojuomaService();
            INautintojuomaService stub = (INautintojuomaService) UnicastRemoteObject.exportObject(process, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("NautintojuomaService", stub);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
