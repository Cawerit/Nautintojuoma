package client;


import server.INautintojuomaService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteObject;

public class NautintojuomaClient extends RemoteObject {

    private INautintojuomaService process;
    private String token;

    public NautintojuomaClient() throws RemoteException {
        super();
        try {
            Registry registry = LocateRegistry.getRegistry(null); //tässä null = localhost
            //String address = "rmi://" + "localhost" + "/process";
            process = (INautintojuomaService) registry.lookup("NautintojuomaService");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean login(String name){
        try {
            token = process.login(name);
            System.out.println("User " + name + " logged in and got token " + token);
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

}
