package server;

import server.machines.Silo;
import server.machines.SiloLoader;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

public class NautintojuomaService implements INautintojuomaService {

    private final LoginService loginService = new LoginService();
    private SiloLoader siloLoader = new SiloLoader();
    private Silo[] silos = new Silo[]{
        new Silo(),
        new Silo(),
        new Silo(),
        new Silo()
    };

    public NautintojuomaService () throws RemoteException {
        super();
    }


    public void login(String name){
        if(name.length() == 0) throw new IllegalArgumentException("Nimi on liian lyhyt");
        loginService.login(name);
    }

    public void logOut(String name){
        loginService.logOut(name);
    }

    public SiloLoader getSiloLoader(){
        return siloLoader;
    }

    public Silo getSilo(int index){
        assert index >= 0 && index < silos.length : "There is no silo number " + index;
        System.out.println("kutsutaan " + index);
        System.out.println(Arrays.toString(silos));
        return silos[0];
    }

}
