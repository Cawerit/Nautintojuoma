package server.machines;


import java.util.Iterator;

/**
 * IContainerin toteuttavista luokista voidaan ottaa raaka-ainetta tai muuta sisältöä
 */
public interface IContainer extends IMachine {
    boolean hasNext();
    int next(int maxAmount);
    String getContentType();
    int getMaterialAmount();
    boolean canStartTaking(String name);
    void startTaking(String name);
    void stopTaking();
}
