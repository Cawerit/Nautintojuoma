package server.machines;


import java.util.Objects;

public class Container extends Machine implements IContainer, IFillable {

    private int materialAmount;
    private boolean takingInProgress;
    private boolean fillingInProgress;

    private final String contentType;
    private final int maxContent;

    public Container(String contentType, int maxContent){
        this.contentType = contentType;
        this.maxContent = maxContent;
    }

    public Container(int maxContent){
        this(null, maxContent);
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean hasNext() {
        return materialAmount > 0;
    }

    @Override
    public synchronized int next(int maxAmount) {
        assert takingInProgress : " säiliön tyhjennys on aloitettava erillisellä kutsulla!";
        int take = maxAmount > materialAmount ? materialAmount : maxAmount;
        add(-take);
        return take;
    }

    @Override
    public int canHold() {
        return maxContent - materialAmount;
    }

    @Override
    public void fill(int amount){
        assert fillingInProgress : " säiliön täyttö on aloitettava erillisellä kutsulla!";
        add(amount);
    }

    @Override
    public boolean canStartFilling(String name) {
        return Objects.equals(name, this.reservedTo()) && this.canHold() > 0;
    }

    @Override
    public boolean canStartTaking(String name) {
        return Objects.equals(name, this.reservedTo()) && this.hasNext();
    }

    @Override
    public void startFilling(String name) {
        assert canStartFilling(name) : name + " ei voi tällä hetkellä täyttää tätä säiliötä!";
        fillingInProgress = true;
    }

    @Override
    public void startTaking(String name) {
        assert canStartFilling(name) : name + " ei voi tällä hetkellä täyttää tätä säiliötä!";
        takingInProgress = true;
    }

    @Override
    public void stopFilling(){
        fillingInProgress = false;
    }

    @Override
    public String getStatus(){
        return materialAmount + "kg" + (contentType == null ? "" : " of " + contentType);
    }

    public boolean isFillingInProgress(){
        return fillingInProgress;
    }

    public boolean isTakingInProgress(){
        return takingInProgress;
    }

    public void stopTaking(){
        takingInProgress = false;
    }

    private synchronized void add(int amount) {
        int newAmount = materialAmount + amount;

        assert newAmount <= maxContent && newAmount >= 0;

        materialAmount = newAmount;
    }


}
