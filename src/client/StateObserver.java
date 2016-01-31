package client;

import server.machines.IMachine;
import server.machines.NautintojuomaMachine;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Seuraa palvelimen tilaa ja päivittää olennaisia UI-komponentteja vastaamaan sitä
 */
public abstract class StateObserver extends Thread {

    NautintojuomaClient server;

    HashMap<NautintojuomaMachine, IMachine> prevState;

    public StateObserver(NautintojuomaClient server) {
        this.server = server;
    }

    abstract void reservationChanged(NautintojuomaMachine machine, String toReserver);
    abstract void statusChanged(NautintojuomaMachine machine, String toValue);

    @Override
    public void run(){
        while(true){

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }


            HashMap<NautintojuomaMachine, IMachine> currentState = server.getState();

            boolean first = prevState == null;


            for (Map.Entry<NautintojuomaMachine, IMachine> entry : currentState.entrySet()) {

                NautintojuomaMachine name = entry.getKey();
                IMachine
                        machine = entry.getValue(),
                        prev = first ? null : prevState.get(name);

                String reserverNow = machine.reservedTo();
                if(first || different(prev.reservedTo(), reserverNow))
                    reservationChanged(name, reserverNow);

                String statusNow = machine.getStatus();
                if(first || different(prev.getStatus(), statusNow))
                    statusChanged(name, statusNow);


            }

            prevState = currentState;

        }
    }

    /**
     * Tarkistaa voidaanko kahta objektia pitää erilaisina, ottaen huomioon että
     * ne voivat olla null
     */
    private static boolean different(Object a, Object b){
        return a == null ? b != null : !a.equals(b);
    }

}

