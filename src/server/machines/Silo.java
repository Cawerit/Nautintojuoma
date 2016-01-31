package server.machines;


import java.util.Iterator;
import java.util.function.IntSupplier;

public class Silo extends Machine implements IContainer, IFillable{

    public static final int MAX_CONTENT = 10000;

    /**
     * Paljonko raaka-ainetta on siilossa (kiloa)
     */
    private int content;
    private String contentType;

    public Silo(String contentType){
        this.contentType = contentType;
    }

    public void reserve(String name){
        super.reserve(name);
    }

    public String getStatus(){ return "" + content; }

    /**
     * Täyttää siiiloa annetulla määrällä sisältöä.
     * HUOM! Ennen käyttöä on varmistettava canHold()-kutsulla että siilossa todella on tilaa annetulle määrälle raaka-ainetta!
     * @pre canHold() >= amount
     * @param amount Kuinka paljon siiloa täytetään (kg)
     */
    public void fill(int amount) {
        assert canHold() >= amount : "Too much " + contentType + " in the silo!";
        System.out.println("Filling silo with " + amount + "kg of " + contentType);
        content += amount;
    }

    public int canHold(){
        int amount = MAX_CONTENT - content;
        return amount < 0 ? 0 : amount;
    }

    public synchronized int next(int amount){
        int take = amount > content ? content : amount;
        content -= take;
        return take;
    }

    public boolean hasNext(){
        return content > 0;
    }

}
