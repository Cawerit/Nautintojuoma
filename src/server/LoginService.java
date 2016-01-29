package server;

import java.util.ArrayList;

/**
 * Ylläpitää listaa kirjautuneista käyttäjistä
 */
public class LoginService {

    private ArrayList<String> users = new ArrayList<String>();

    /**
     * Merkkaa annetun käyttäjän kirjatuksi sisään
     * @param name Käyttäjänimi
     */
    public void login(String name){
        users.add(name);
    }

    /**
     * Tarkistaa onko annettu käyttäjä kirjautuneena sisään
     * @param name Käyttäjänimi
     */
    public boolean loggedIn(String name){
        return users.contains(name);
    }

    /**
     * Kirjaa annetun käyttäjän ulos palvelusta
     * @param name
     */
    public void logOut(String name){
        users.remove(name);
    }

    public void assertLoggedIn(String name) throws UserNotLoggedInException {
        if(loggedIn(name)) throw new UserNotLoggedInException();
    }

    public static class UserNotLoggedInException extends RuntimeException {
        public UserNotLoggedInException(){
            super("The user is not logged in");
        }

    }

}

