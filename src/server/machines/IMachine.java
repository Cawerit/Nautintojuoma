package server.machines;


public interface IMachine {
    boolean isReserved();
    boolean isReserved(String name);
    void reserve(String token);
}
