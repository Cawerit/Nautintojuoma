package server.machines;


public class Processor extends Container {

    private double progress = 1.0;

    public Processor(String contentType){
        super(contentType, 2000);
    }

    @Override
    public String getStatus(){
        return isProcessing() ? ((int)(progress*100)) + "% keitetty" : super.getStatus();
    }

    @Override
    public boolean canStartFilling(String name){
        return !isProcessing() && !isFillingInProgress() && super.canStartFilling(name);
    }

    @Override
    public boolean canStartTaking(String name){
        return !isProcessing() && !isTakingInProgress() && super.canStartTaking(name);
    }

    @Override
    public boolean isProcessing(){
        return progress != 1.0;
    }

    public void process(String user){
        this.reserve(this::processSync, user);
    }

    private void processSync(){
        try {//Prosessointi kestää 20sekuntia
            int chunks = 8;
            int timeout = 20000/chunks;
            progress = 0.0;
            for(int i=0; i<chunks; i+=1){
                progress = ((double)i)/((double)chunks);
                Thread.sleep(timeout);
            }
            progress = 1.0;
        } catch (InterruptedException e) {
            progress = 1.0;
        }

    }

}
