package server.machines;


public class Processor extends Container {


    public Processor(String contentType){
        super(contentType, 2000);
    }


    @Override
    public boolean canStartFilling(String name){
        return !isFillingInProgress() && super.canStartFilling(name);
    }

    @Override
    public boolean canStartTaking(String name){
        return !isTakingInProgress() && super.canStartTaking(name);
    }


}
