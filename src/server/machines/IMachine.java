package server.machines;


public interface IMachine {
    boolean isReserved();
    boolean isReserved(String token);
    void reserve(String token);
}
