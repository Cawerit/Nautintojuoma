package server.machines;


public class SiloLoader extends Loader {

    public static final int MATERIAL_LOAD_SIZE = 40000;

    private MaterialSource[] invisibleSource = new MaterialSource[]{new MaterialSource()};

    public void moveMaterial(String reserver, Silo[] silos){
        invisibleSource[0].reserve(reserver);
        super.moveMaterial(reserver, MATERIAL_LOAD_SIZE, invisibleSource, silos);
    }


    private static final class MaterialSource extends Machine implements IContainer {

        private int materialAmount = MATERIAL_LOAD_SIZE;

        @Override
        public boolean hasNext() {
            return materialAmount > 0;
        }

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
