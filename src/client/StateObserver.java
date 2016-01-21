package client;

import server.machines.IMachine;

import javax.swing.*;

/**
 * Seuraa palvelimen tilaa ja päivittää olennaisia UI-komponentteja vastaamaan sitä
 */
public class StateObserver extends Thread {

    private JToggleButton button;
    private JLabel label;
    private IMachine machine;
    private State currentState = State.NOT_LOGGED_IN;
    private String userToken;

    public StateObserver(JToggleButton button, JLabel label, IMachine machine){
        this.button = button;
        this.label = label;
        this.machine = machine;
    }

    public void onLogin(String token){
        currentState = null;
        userToken = token;
    }

    @Override
    public void run(){
        try {
            while(true) {
                Thread.sleep(1000);

                switch(currentState){

                    case NOT_LOGGED_IN:
                        continue;

                    case USING_MACHINE:
                        System.out.println("Using machine...");
                        break;


                }
            }

        } catch (InterruptedException e) {
            return;
        }
    }

    private enum State {
        USING_MACHINE, NOT_LOGGED_IN
    }

}

