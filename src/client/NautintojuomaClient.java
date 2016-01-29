package client;


import server.INautintojuomaService;
import server.machines.IMachine;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteObject;

public class NautintojuomaClient extends RemoteObject {

    private INautintojuomaService process;

    public NautintojuomaClient() throws RemoteException {
        super();
        try {
            Registry registry = LocateRegistry.getRegistry(null);
            process = (INautintojuomaService) registry.lookup("NautintojuomaService");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean login(String name) {
        try {
            process.login(name);
            Runtime.getRuntime().addShutdownHook(new Thread(){
                @Override
                public void run(){
                    logOut(name);
                }
            });
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void logOut(String token){
        try {
            process.logOut(token);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public INautintojuomaService getProcess(){
        return process;
    }

}
