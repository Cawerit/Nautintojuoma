package server.machines;


public interface IMachine {
    boolean isReserved();
    boolean isProcessing();
    String reservedTo();
    String getStatus();
    void reserve(String name);
    void setFree();
}
