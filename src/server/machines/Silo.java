package server.machines;


public class Silo extends Container {

    public static final int MAX_CONTENT = 10000;

    public Silo(String contentType){
        super(contentType, MAX_CONTENT);
    }



}
