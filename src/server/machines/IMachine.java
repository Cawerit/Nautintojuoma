package server.machines;


import server.User;

public interface IMachine {
    boolean isReserved();
    boolean isReserved(String token);
    void reserve(String token);
}
