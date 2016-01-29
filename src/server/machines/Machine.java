package server.machines;


import java.io.Serializable;

public class Machine extends Thread implements IMachine, Serializable {

    /**
     * Laitteen käyttäjän token
     */
    private String reserved;

    public void reserve(String name){
        if(reserved != null) throw new AlreadyReservedException();
        reserved = name;
        System.out.println("varattu käyttäjälle " + name);
    }

    /**
     * @return Tieto siitä, onko laite varattu jollekin käyttäjälle
     */
    public boolean isReserved(){
        return reserved != null;
    }

    /**
     * @param name Kirjautuneen käyttäjän token
     * @return Tieto siitä, onko laite varattu nykyiselle kirjautuneelle käyttäjälle
     */
    public boolean isReserved(String name){
        return isReserved() && reserved.equals(name);
    }

    private class AlreadyReservedException extends RuntimeException {
        public AlreadyReservedException(){
            super("Laite " + Machine.this.getClass().getName() + " on varattu.");
        }
    }

}
