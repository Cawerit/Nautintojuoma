package server.machines;

/**
 * IFillablen toteuttavia luokkia voidaan täyttää raaka-aineilla tai muulla sisällöllä
 */
public interface IFillable extends IMachine {
    void fill(int amount);
    int canHold();
    boolean canStartFilling(String name);
    void startFilling(String name);
    void stopFilling();
}
