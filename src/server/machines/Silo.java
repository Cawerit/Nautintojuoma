package server.machines;


public class Silo extends Machine {

    public void reserve(String name){
        super.reserve(name);
        try {
            System.out.println("Siilo varattu");
            Thread.sleep((5)*1000);
            this.setFree();
            System.out.println("Siilo vapaa");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
