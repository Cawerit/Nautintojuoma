package client;


import server.INautintojuomaService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteObject;

public class NautintojuomaClient extends RemoteObject {

    private INautintojuomaService process;

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

    public void login(String name){
        try {
            System.out.println("login connectorservice");
            process.login(name);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
