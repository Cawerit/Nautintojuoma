package server;

import server.machines.*;

import javax.crypto.Mac;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.HashMap;

public class NautintojuomaService implements INautintojuomaService {

    private HashMap<NautintojuomaMachine, IMachine> serverState = new HashMap<>();

    private final LoginService loginService = new LoginService();

    private Silo[] silos = {
            new Silo("hiiva"), new Silo("vesi"), new Silo("humala"), new Silo("ohra")
    };

    private SiloLoader siloLoader = new SiloLoader();

    public NautintojuomaService () throws RemoteException {
        super();
        HashMap<NautintojuomaMachine, IMachine> s = serverState;
        s.put(NautintojuomaMachine.SILO1, silos[0]);
        s.put(NautintojuomaMachine.SILO2, silos[1]);
        s.put(NautintojuomaMachine.SILO3, silos[2]);
        s.put(NautintojuomaMachine.SILO4, silos[3]);
        s.put(NautintojuomaMachine.SILO_LOADER, siloLoader);
    }


    public void login(String name){
        if(name.length() == 0) throw new IllegalArgumentException("Nimi on liian lyhyt");
        loginService.login(name);
    }

    public void logOut(String name){
        loginService.logOut(name);
    }


    public void reserve(NautintojuomaMachine machineName, String name){
        IMachine machine = serverState.get(machineName);
        if(machine == null) throw new NoSuchMachineException(machineName);
        machine.reserve(name);
    }

    public void fillSilos(String username){
        siloLoader.fill(username, silos);
    }

    public HashMap<NautintojuomaMachine, IMachine> pullState(){
        return serverState;
    };

    private class NoSuchMachineException extends RuntimeException {
        public NoSuchMachineException(NautintojuomaMachine machine){
            super("Machine " + machine + " has not been implemented");
        }
    }

}
