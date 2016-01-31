package server.machines;


import java.util.function.Predicate;

public abstract class Loader extends Machine {

    private String status;

    public String getStatus(){ return status; }

    public void fill(String username, int fillKg, IContainer[] sources, IFillable[] targets){
        status = "Lataa...";
        outer:
        while(fillKg > 0) {

            //Etsitään sopiva source josta täytetään ja sopiva target johon täytetään
            IContainer source = findAvailable(username, sources, Loader::isSourceUseful);
            IFillable target = findAvailable(username, targets, Loader::isTargetUseful);

            System.out.println("found " + source + " " + target);
            if(source == null || target == null || !source.hasNext()){
                try {
                    Thread.sleep(1000);//Odotetaan sopivien säiliöiden vapautumista
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break outer;
                }
            }

            while (fillKg > 0 && source.hasNext()) {//Täytetään niin paljon kun voidaan
                int
                    amountToMove = fillKg > 200 ? 200 : fillKg,
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
                    e.printStackTrace();
                    break outer;
                }
            }
        }
        status = "Valmiina käytettäväksi";
    }


    private static <T extends IMachine> T findAvailable(String username, T[] machines, Predicate<T> predicate){
        for(T m : machines) {
            String reserved = m.reservedTo();
            if(((reserved == username) || (username != null && username.equals(reserved))) && predicate.test(m)) return m;
        }
        return null;
    }

    private static boolean isSourceUseful(IMachine m){
        return m instanceof IContainer && ((IContainer) m).hasNext();
    }

    private static boolean isTargetUseful(IMachine m){
        return m instanceof IFillable && ((IFillable) m).canHold() > 0;
    }


}
