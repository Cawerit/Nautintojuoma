package client;

import server.machines.IMachine;

/**
 * Säiliö kaikille StateObserver-olioille.
 * Käytetään lähinnä jotta MainWindow.javaa ei tarvitse tukkia enää uusilla muuttujilla.
 */
public abstract class ObserverCollection {

    StateObserver silo1;
    StateObserver silo2;
    StateObserver silo3;

    public ObserverCollection() {
        init();
    }
    abstract void init();

    public StateObserver[] toArray(){
        return new StateObserver[] {
                silo1,
                silo2,
                silo3
        };
    }

}
