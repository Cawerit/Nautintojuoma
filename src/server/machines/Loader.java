package server.machines;


import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Luokka jonka instansseja voidaan käyttää materiaalin siirtämiseen IContainer-olioista
 * IFillable-olioihin.
 */
public class Loader extends Machine {

    private String status;
    private int unitsPerSecond;

    public Loader(int unitsPerSecond){
        this.unitsPerSecond = unitsPerSecond;
    }

    public String getStatus(){ return status; }

    public void moveMaterial(String username, int fillKg, IContainer[] sources, IFillable[] targets){
        this.reserve(() -> this.moveMaterialSync(username, fillKg, sources, targets), username);
    }
    /**
     * Siirtää raaka-ainetta annetuista säiliöistä toisiin
     * @param username Laitteen käyttäjän nimi
     * @param fillKg Kuinka paljon raaka-ainetta siirretään
     * @param sources Mistä raaka-aine saadaan
     * @param targets Minne raaka-aine siirretään
     */
    public void moveMaterialSync(String username, int fillKg, IContainer[] sources, IFillable[] targets){
        status = "Siirto käynnissä...";
        outer:
        while(fillKg > 0 && Objects.equals(username, reservedTo())) {

            //Etsitään sopiva source josta täytetään ja sopiva target johon täytetään
            IContainer source = find(sources, s -> s.canStartTaking(username));
            IFillable target = find(targets, t -> t.canStartFilling(username));

            if(source == null || target == null || !source.hasNext()){
                try {
                    Thread.sleep(1000);//Odotetaan sopivien säiliöiden vapautumista
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break outer;
                }
            }

            source.startTaking(username);
            target.startFilling(username);

            //Täytetään niin paljon kun voidaan, niin kauan kun itse ollaan kirjautuneena sisään
            while (fillKg > 0 && Objects.equals(username, reservedTo()) && source.hasNext() && source.isReserved() && target.isReserved()) {
                int
                    amountToMove = fillKg > unitsPerSecond ? unitsPerSecond : fillKg,
                    canHold = target.canHold();

                if(canHold < amountToMove) {
                    if(canHold == 0) break;
                    else amountToMove = target.canHold();
                }
                amountToMove = source.next(amountToMove);//HUOM! Tässä kohtaa amountToMove saattaa pienentyä, jos säiliössä ei ollut tarpeeksi
                try {
                    Thread.sleep(1000);
                    target.fill(amountToMove);
                    fillKg -= amountToMove;
                } catch (InterruptedException e) {
                    target.stopFilling();
                    source.stopTaking();
                    break outer;
                }
            }

            target.stopFilling();
            source.stopTaking();
        }
        status = null;
    }


    private static <T> T find(T[] machines, Predicate<T> predicate){
        for(T m : machines) if(predicate.test(m)) return m;
        return null;
    }
}
