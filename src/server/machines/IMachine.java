package server.machines;


public interface IMachine {
    boolean isReserved();
    String reservedTo();
    String getStatus();
    void reserve(String name);
    void setFree();
}
