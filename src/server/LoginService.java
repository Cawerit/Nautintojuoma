package server;


import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * Ylläpitää listaa kirjautuneista käyttäjistä
 */
public class LoginService {

    private ArrayList<User> users = new ArrayList<User>();
    private SecureRandom random = new SecureRandom();

    /**
     * Merkkaa annetun käyttäjän kirjatuksi sisään ja palauttaa tokenin, jota voi myöhemmin käyttää autentikointiin
     * @param name Käyttäjänimi
     * @return Token
     */
    public String login(String name){
        String token = new BigInteger(130, random).toString();
        users.add(new User(name, token));
        return token;
    }

    /**
     * Tarkistaa onko käyttäjää annetulla tokenilla kirjautuneena sisään
     * @param token Käyttäjälle aiemmin annettu token
     * @return
     */
    public boolean loggedIn(String token){
        return users
                .stream()
                .anyMatch(u -> token.equals(u.getToken()));
    }

    public User getUser(String token) {
        for (User u : users) {
            if (u.getToken() == token) return u;
        }
    }

    public void logOut(String token){
        users.removeIf(u -> token.equals(u.getToken()));
    }


    private class User {

        private String name;
        private String token;

        public User(String name, String token){
            this.name = name;
            this.token = token;
        }

        public String getName(){
            return name;
        }

    public static class UserNotLoggedInException extends RuntimeException {
        public UserNotLoggedInException(){
            super("The user is not logged in");
        }

    }

}

