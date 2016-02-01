package server.machines;


import java.util.Iterator;

public interface IContainer extends IMachine {
    boolean hasNext();
    int next(int maxAmount);
    String getContentType();
    boolean canStartTaking(String name);
    void startTaking(String name);
    void stopTaking();
}
