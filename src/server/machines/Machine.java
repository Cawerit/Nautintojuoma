package server.machines;


import server.LoginService;
import server.User;

public class Machine extends Thread implements IMachine {

    private User reserved;
    private final LoginService loginService;

    public Machine(LoginService loginService){
        this.loginService = loginService;
    }

    public void reserve(String token){
        if(reserved != null) throw new AlreadyReservedException();
        User reserver = loginService.getUser(token);
        if(reserver == null) throw new LoginService.UserNotLoggedInException();
        reserved = reserver;
    }

    /**
     * @return Tieto siitä, onko laite varattu jollekin käyttäjälle
     */
    public boolean isReserved(){
        return reserved != null;
    }

    /**
     * @param token Kirjautuneen käyttäjän token
     * @return Tieto siitä, onko laite varattu nykyiselle kirjautuneelle käyttäjälle
     */
    public boolean isReserved(String token){
        return isReserved() && reserved.getToken() == token;
    }

    private class AlreadyReservedException extends RuntimeException {
        public AlreadyReservedException(){
            super("The machine " + Machine.this.getClass().getName() + " is reserved for user " + reserved.getName());
        }
    }

}
