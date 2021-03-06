package client;


import server.INautintojuomaService;
import server.machines.IMachine;
import server.machines.NautintojuomaMachine;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteObject;
import java.util.HashMap;

public class NautintojuomaClient extends RemoteObject {

    private INautintojuomaService process;
    private String username;

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
            username = name;
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

    public void toggleReservation(NautintojuomaMachine machine){
        try {
            getProcess().toggleReservation(machine, getUsername());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void fillSilos(){
        try {
            getProcess().fillSilos(getUsername());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void fillProcessors(NautintojuomaMachine usingMachine, int amount){
        try {
            getProcess().fillProcessors(usingMachine, amount, getUsername());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void process(NautintojuomaMachine usingProcessor){
        try {
            getProcess().process(usingProcessor, getUsername());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void usePumps(NautintojuomaMachine usingPump, Integer amount){
        try {
            getProcess().usePumps(usingPump, amount, getUsername());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public HashMap<NautintojuomaMachine, IMachine> getState(){
        try {
            return getProcess().pullState();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUsername(){ return username; }

    public INautintojuomaService getProcess(){
        return process;
    }

}
