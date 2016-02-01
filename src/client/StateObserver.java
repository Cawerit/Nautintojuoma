package client;

import server.machines.IMachine;
import server.machines.NautintojuomaMachine;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Seuraa palvelimen tilaa ja päivittää olennaisia UI-komponentteja vastaamaan sitä
 */
public class StateObserver extends Thread {

    private final NautintojuomaClient server;
    private final String username;
    private final ArrayList<ObserverConfig> configs = new ArrayList<>();

    HashMap<NautintojuomaMachine, IMachine> prevState;

    public StateObserver(NautintojuomaClient server, String username) {
        this.server = server;
        this.username = username;
    }

    /**
     * Kiinnittää annetut UI-komponentit seurantaan, niin että niitä päivitetään sitä mukaa kun palvelimen tila muuttuu.
     * @see StateObserver.ObserverConfig for details
     * @return this
     */
    public StateObserver observe(NautintojuomaMachine m, JLabel s, JToggleButton b1, JToggleButton b2){
        ObserverConfig conf = new ObserverConfig(m, s, b1, b2);
        conf.setUsername(username);
        this.configs.add(conf);
        return this;
    }

    public StateObserver observe(NautintojuomaMachine m, JLabel s, JToggleButton b1){
        return this.observe(m, s, b1, null);
    }



    @Override
    public void run(){
        while(true){

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }


            HashMap<NautintojuomaMachine, IMachine> currentState = server.getState();

            for(ObserverConfig c : configs){
                IMachine state = currentState.get(c.getMachine()),
                        prev = prevState == null ? null : prevState.get(c.getMachine());
                if(state == null) continue;
                if(prev == null || !Objects.equals(state.reservedTo(), prev.reservedTo())) c.reservationChanged(state.reservedTo());
                if(prev == null || !Objects.equals(state.getStatus(), prev.getStatus())) c.statusChanged(state.getStatus());
                if(prev == null || state.isProcessing() != prev.isProcessing()) c.processingChanged(state.isProcessing());
            }


            prevState = currentState;

        }
    }

    /**
     * Configuraatio-luokka StateObserverille
     */
    private static final class ObserverConfig {

        private final String
                originalStatus,
                originalBtnTxt;
        private String username;
        private final NautintojuomaMachine machine;
        private final JToggleButton
                button1,
                button2;
        private final JLabel status;


        public ObserverConfig(NautintojuomaMachine machine, JLabel status, JToggleButton button1, JToggleButton button2) {
            this.machine = machine;
            this.button1 = button1;
            this.button2 = button2;
            this.status = status;
            originalStatus = status.getText();
            originalBtnTxt = button1.getText();
        }

        public ObserverConfig(NautintojuomaMachine machine, JLabel status, JToggleButton button1) {
            this(machine, status, button1, null);
        }

        protected void setUsername(String username){
            this.username = username;
        }

        public NautintojuomaMachine getMachine() {
            return machine;
        }

        public JToggleButton getButton1() {
            return button1;
        }

        public JToggleButton getButton2() {
            return button2;
        }

        public JLabel getStatus() {
            return status;
        }

        public void reservationChanged(String toValue){
            button1.setSelected(false);
            if (toValue != null){
                if(toValue.equals(username)) {
                    button1.setText("Vapauta");
                }
                else {
                    button1.setEnabled(false);
                    button1.setToolTipText("Varattu käyttäjälle " + toValue);
                }
            }
            else {
                button1.setToolTipText("Vapaa käytettäväksi");
                button1.setText(originalBtnTxt);
                button1.setEnabled(true);
                if(button2 != null) {
                    button2.setEnabled(true);
                }
            }
        }

        public void statusChanged(String toValue){
            if(toValue != null && toValue.length() > 0) status.setText(toValue);
            else status.setText(originalStatus);
        }

        public void processingChanged(boolean toValue){
            if(button2 != null) button2.setEnabled(!toValue);
        }

    }

}

