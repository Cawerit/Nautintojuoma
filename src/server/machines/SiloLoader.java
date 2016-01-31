package server.machines;


import server.LoginService;

import java.util.Iterator;
import java.util.function.IntSupplier;

public class SiloLoader extends Loader {

    public static final int MATERIAL_LOAD_SIZE = 40000;

    public void fill (String reserver, Silo[] silos){
        MaterialSource[] src = new MaterialSource[] {new MaterialSource(reserver)};
        super.reserve(() -> super.fill(reserver, MATERIAL_LOAD_SIZE, src, silos), reserver);
    }


    private static final class MaterialSource extends Machine implements IContainer {

        private int materialAmount = MATERIAL_LOAD_SIZE;
        String reserver;

        public MaterialSource(String reserver){
            this.reserver = reserver;
        }

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
        public String reservedTo(){
            return this.reserver;
        }

    }

}
