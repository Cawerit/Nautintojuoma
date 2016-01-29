package client;

import server.machines.IMachine;

import javax.swing.*;
import java.util.Map;

/**
 * Seuraa palvelimen tilaa ja päivittää olennaisia UI-komponentteja vastaamaan sitä
 */
public class StateObserver extends Thread {

    private JToggleButton button;
    private JLabel label;
    private IMachine machine;
    private String username;
    private State currentState;

    public StateObserver(JToggleButton button, JLabel label, IMachine machine){
        this.button = button;
        this.label = label;
        this.machine = machine;
        button.setEnabled(false);
    }


    public StateObserver onLogin(String name){
        username = name;
        return this;
    }

    @Override
    public void run(){
        System.out.println("run");
        boolean reserved = machine.isReserved();
        button.setEnabled(!reserved);
        currentState = reserved ? State.RESERVED : State.FREE;
        try {
            while(true) {
                Thread.sleep(1000);

                switch(currentState){

                    case USING_MACHINE:
                        System.out.println("Using machine...");
                        break;

                    case RESERVED:
                        System.out.println("Reserverd for another");
                        break;

                }
            }

        } catch (InterruptedException e) {
            return;
        }
    }

    private enum State {
        USING_MACHINE, RESERVED, FREE
    }

}

