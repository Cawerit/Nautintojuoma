package server.machines;


public interface IFillable extends IMachine {
    public void fill(int amount);
    public int canHold();
    boolean canStartFilling(String name);
    void startFilling(String name);
    void stopFilling();
}
