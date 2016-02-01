package server.machines;

/**
 * Tankit, joihin juoma lopulta siirretään ennen pullotusta
 */
public class Tank extends Container {

    public Tank(String contentType){
        super(contentType, 10000);
    }

    @Override
    public boolean canStartTaking(String name) {
        //Kypsytyssäiliöitä voi täyttää ja tyhjentää vain yksi pumppu kerrallaan, joten tiukennetaan hieman rajoituksia
        return !somethingInProgress() && super.canStartTaking(name);
    }

    @Override
    public boolean canStartFilling(String name) {
        //Kypsytyssäiliöitä voi täyttää ja tyhjentää vain yksi pumppu kerrallaan, joten tiukennetaan hieman rajoituksia
        return !somethingInProgress() && super.canStartFilling(name);
    }

    private boolean somethingInProgress(){
        return isTakingInProgress() || isFillingInProgress();
    }

}
