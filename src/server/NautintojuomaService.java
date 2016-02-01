package server;

import server.machines.*;

import java.rmi.*;
import java.util.HashMap;
import java.util.Objects;

public class NautintojuomaService implements INautintojuomaService {

    private HashMap<NautintojuomaMachine, IMachine> serverState = new HashMap<>();

    private final LoginService loginService = new LoginService();

    private Silo[] silos = {
            new Silo("hiiva"), new Silo("vesi"), new Silo("humala"), new Silo("ohra")
    };

    private SiloLoader siloLoader = new SiloLoader();
    private Loader[] procLoaders = new Loader[]{ new Loader(), new Loader() };

    //Juomakeittimet, joihin mahtuu 2k kiloa raaka-ainetta
    private Container[] processors = new Container[]{ new Container(2000), new Container(2000), new Container(2000) };

    public NautintojuomaService () throws RemoteException {
        super();
        HashMap<NautintojuomaMachine, IMachine> s = serverState;
        s.put(NautintojuomaMachine.SILO1, silos[0]);
        s.put(NautintojuomaMachine.SILO2, silos[1]);
        s.put(NautintojuomaMachine.SILO3, silos[2]);
        s.put(NautintojuomaMachine.SILO4, silos[3]);
        s.put(NautintojuomaMachine.SILO_LOADER, siloLoader);
        s.put(NautintojuomaMachine.PROC_LOADER1, procLoaders[0]);
        s.put(NautintojuomaMachine.PROC_LOADER2, procLoaders[1]);
        s.put(NautintojuomaMachine.PROCESSOR1, processors[0]);
        s.put(NautintojuomaMachine.PROCESSOR2, processors[1]);
        s.put(NautintojuomaMachine.PROCESSOR3, processors[2]);
    }


    public void login(String name){
        if(name.length() == 0) throw new IllegalArgumentException("Nimi on liian lyhyt");
        loginService.login(name);
    }

    public void logOut(String name){
        loginService.logOut(name);
        for(IMachine m : serverState.values()) m.setFree();
    }


    public void toggleReservation(NautintojuomaMachine machineName, String name){
        IMachine machine = serverState.get(machineName);
        if(machine == null) throw new NoSuchMachineException(machineName);
        if(machine.isReserved()) {
            if(machine.reservedTo().equals(name)) machine.setFree();
        } else machine.reserve(name);
    }

    public void fillSilos(String username){
        if(Objects.equals(siloLoader.reservedTo(), username)) siloLoader.setFree();
        else siloLoader.moveMaterial(username, silos);
    }

    public void fillProcessors(NautintojuomaMachine usingLoader, int amount, String username){
        Loader l = null;

        if(NautintojuomaMachine.PROC_LOADER1.equals(usingLoader)) l = procLoaders[0];
        else if (NautintojuomaMachine.PROC_LOADER2.equals(usingLoader)) l = procLoaders[1];
        else throw new NoSuchMachineException(usingLoader);

        if(Objects.equals(l.reservedTo(), username)) l.setFree();
        else l.moveMaterial(username, amount, silos, processors);

    }

    public HashMap<NautintojuomaMachine, IMachine> pullState(){
        return serverState;
    };

    private class NoSuchMachineException extends RuntimeException {
        public NoSuchMachineException(NautintojuomaMachine machine){
            super("Machine " + machine + " has not been implemented for this use");
        }
    }

}
