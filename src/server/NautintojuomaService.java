package server;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class NautintojuomaService implements INautintojuomaService {

    private LoginService loginService = new LoginService();

    public NautintojuomaService () throws RemoteException {
        super();
    }


    public String login(String name){
        if(name.length() == 0) throw new IllegalArgumentException("Nimi on liian lyhyt");
        return loginService.login(name);
    }
    public void logOut(String token){
        loginService.logOut(token);
    }

}
