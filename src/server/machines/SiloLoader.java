package server.machines;


public class SiloLoader extends Loader {

    public static final int MATERIAL_LOAD_SIZE = 40000;

    public SiloLoader(){
        super(200);//200kg sekunnissa
    }

    private MaterialSource[] invisibleSource = new MaterialSource[]{new MaterialSource()};

    public void moveMaterial(String reserver, Silo[] silos){
        invisibleSource[0].reserve(reserver);
        super.moveMaterial(reserver, MATERIAL_LOAD_SIZE, invisibleSource, silos);
    }


    /**
     * Luokka, joka tarjoaa siilojen lataajalle materiaalit.
     * Tämä säiliö on "näkymättömissä", sitä ei ohjata UI:n kautta, vaan se tarjoaa aina max 40k edestä materiaalia kerralla.
     * Siksi sen ei myöskään tarvitse implementoida luokkaa IFillable
     * @see server.machines.Pump.Bottling on hieman vastaavanlainen toteutus linjan loppupäähän
     */
    private static final class MaterialSource extends Machine implements IContainer {

        private int materialAmount = MATERIAL_LOAD_SIZE;

        @Override
        public boolean hasNext() {
            return materialAmount > 0;
        }
        @Override
        public int getMaterialAmount(){ return materialAmount; }

        @Override
        public synchronized int next(int maxAmount) {
            int take = maxAmount > materialAmount ? materialAmount : maxAmount;
            materialAmount -= take;
            return take;
        }

        @Override
        public String getContentType() {
            return null;
        }

        @Override
        public boolean canStartTaking(String name) {
            return true;
        }
        @Override
        public void startTaking(String name){
            this.materialAmount = MATERIAL_LOAD_SIZE;
        }

        @Override
        public void stopTaking(){}
    }

}
