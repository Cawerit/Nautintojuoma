package server;

import server.machines.SiloLoader;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class NautintojuomaService implements INautintojuomaService {

    private final LoginService loginService = new LoginService();
    private final SiloLoader siloLoader = new SiloLoader(loginService);

    public NautintojuomaService () throws RemoteException {
        super();
    }


    public String login(String name){
        if(name.length() == 0) throw new IllegalArgumentException("Nimi on liian lyhyt");
        return loginService.login(name);
    }

    public SiloLoader getSiloLoader(){
        return siloLoader;
    }

}
