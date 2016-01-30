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

    abstract void reservationChanged(NautintojuomaMachine machine, boolean toValue);

    @Override
    public void run(){
        while(true){

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                return;
            }

            HashMap<NautintojuomaMachine, IMachine> currentState = server.getState();


            for (Map.Entry<NautintojuomaMachine, IMachine> entry : currentState.entrySet()) {
                NautintojuomaMachine name = entry.getKey();
                IMachine machine = entry.getValue();

                boolean isReserved = machine.isReserved();

                if(prevState == null || prevState.get(name).isReserved() != isReserved)
                    reservationChanged(name, isReserved);

            }

            prevState = currentState;

        }
    }

}

