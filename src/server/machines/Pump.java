package server.machines;

/**
 * Pumput, jotka siirtävät tavaraa prosessoreista tankkeihin ja tankeista pullotukseen
 */
public class Pump extends Loader {

    public Pump(){
        super(500);//500 litraa sekunnissa
    }

    /**
     * Luokka joka kuvaa pullotusta, eli linjan viimeistä etappia.
     * Tämä luokka toteuttaa interfacen IFillable siten, että se ottaa vastaan niin paljon tavaraa
     * kuin sille annetaan (pullotuspiste ei täyty)
     * @see server.machines.SiloLoader.MaterialSource on vastaavanlainen toteutus listan alkupäähän
     */
    public static final class Bottling extends Machine implements IFillable {

        public Bottling(String username){
            super();
            this.reserve(username);
        }

        @Override
        public boolean canStartFilling(String name) {
            return true;
        }
        @Override
        public void startFilling(String name) {}
        @Override
        public void stopFilling() {}

        @Override
        public int canHold() {
            return Integer.MAX_VALUE;
        }
        @Override
        public void fill(int amount) {}
    }

}
