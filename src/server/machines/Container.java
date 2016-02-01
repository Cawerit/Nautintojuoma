package server.machines;


import java.util.Objects;

/**
 * Yleinen toteutus laitteille joita voi sekä täyttää että tyhjätä
 */
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
    public int getMaterialAmount(){ return materialAmount; }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean hasNext() {
        return materialAmount > 0;
    }

    /**
     * Tyhjentää säiliötä annetulla määrällä, tai vähemmän mikäli säiliössä ei ole tarpeeksi sisältöä.
     * HUOM! Jotta tätä voi kutsua, pitää ensin eksplisiittisesti ilmoittaa alkavansa tyhjennyksen kutsumalla
     * metodia <pre>startTaking(String name)</pre>
     *
     * @pre takingInProgress
     * @param maxAmount Paljonko maksimissaan tyhjätään
     * @return Tyhjätty määrä
     */
    @Override
    public synchronized int next(int maxAmount) {
        assert takingInProgress : " säiliön tyhjennys on aloitettava erillisellä kutsulla!";
        int take = maxAmount > materialAmount ? materialAmount : maxAmount;
        add(-take);
        return take;
    }

    /**
     * @return Tieto siitä paljonko säiliöön vielä mahtuu materiaalia
     */
    @Override
    public int canHold() {
        return maxContent - materialAmount;
    }

    /**
     * Täyttää säiliötä annetun määrän.
     * HUOM! Ensin tulee eksplisiittisesti ilmoittaa täyttämisen aloittamisesta kutsumalla metodia <pre>startFilling(String name)</pre>
     * Lisäksi tulee huolehtia ettei säiliön tilavuus lisäyksen jälkeen ylitä ylärajaa.
     * @pre fillingInProgress AND maxContent <= getMaterialAmount() + amount
     * @param amount Paljonko säiliötä täytetään
     */
    @Override
    public void fill(int amount){
        assert fillingInProgress : " säiliön täyttö on aloitettava erillisellä kutsulla!";
        add(amount);
    }

    /**
     * @param name Käyttäjänimi
     * @return Tieto siitä, voiko annettu käyttäjä aloittaa täyttämisen
     */
    @Override
    public boolean canStartFilling(String name) {
        return Objects.equals(name, this.reservedTo()) && this.canHold() > 0;
    }
    /**
     * @param name Käyttäjänimi
     * @return Tieto siitä, voiko annettu käyttäjä aloittaa tyhjentämisen
     */
    @Override
    public boolean canStartTaking(String name) {
        return Objects.equals(name, this.reservedTo()) && this.hasNext();
    }
    /**
     * Ilmoittaa täyttämisen alkaneeksi. Tämän jälkeen voi kutsua metodia <pre>fill(int amount)</pre>
     * @pre canStartFilling(name)
     */
    @Override
    public void startFilling(String name) {
        assert canStartFilling(name) : name + " ei voi tällä hetkellä täyttää tätä säiliötä!";
        fillingInProgress = true;
    }

    /**
     * Ilmoittaa tyhjentämisen alkaneeksi. Tämän jälkeen voi kutsua metodia <pre>next(int maxAmount)</pre>
     * @param name
     */
    @Override
    public void startTaking(String name) {
        assert canStartFilling(name) : name + " ei voi tällä hetkellä täyttää tätä säiliötä! " + this.toString();
        takingInProgress = true;
    }

    @Override
    public void stopFilling(){
        fillingInProgress = false;
    }

    /**
     * Palauttaa lopulta UI:lle näytettävän statuksen
     * @return
     */
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
