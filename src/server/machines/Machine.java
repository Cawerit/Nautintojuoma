package server.machines;


import jdk.nashorn.internal.objects.annotations.Function;

import java.io.Serializable;

public abstract class Machine implements IMachine, Serializable {

    /**
     * Laitteen käyttäjän token
     */
    private String reserved;

    public void reserve(String name){
        this.reserved = name;
    }

    @Override
    public String getStatus(){
        return this.reservedTo() == null ? "Vapaa" : (this.reservedTo()) + " käyttää laitetta";
    }

    /**
     * Varaa laitteen määritetyksi ajaksi käyttäjälle.
     * @param forTask Tehtävä joka suoritetaan ennen kuin varaus päättyy
     * @param name Varaajan nimi
     */
    public void reserve(Runnable forTask, String name){
        if(reserved != null) throw new AlreadyReservedException();
        reserved = name;
        new TaskRunner(forTask).start();
    }

    public void setFree(){
        this.reserved = null;
    }

    /**
     * @return Tieto siitä, onko laite varattu jollekin käyttäjälle
     */
    public boolean isReserved(){
        return this.reservedTo() != null;
    }

    public String reservedTo(){
        return reserved;
    }

    private class AlreadyReservedException extends RuntimeException {
        public AlreadyReservedException(){
            super("Laite " + Machine.this.getClass().getName() + " on varattu.");
        }
    }

    private class TaskRunner extends Thread {
        private Runnable task;
        public TaskRunner(Runnable task){
            super();
            this.task = task;
        }
        @Override
        public void run(){
            task.run();
            //Kun valmista, merkataan vapaaksi
            Machine.this.setFree();
        }
    }

}
