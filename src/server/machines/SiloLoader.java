package server.machines;


import server.LoginService;

public class SiloLoader extends Machine {


    @Override
    public void reserve(String name){
        this.reserve(name, 0);
    }

    public void reserve(String name, int amountKg){
        super.reserve(name);
        try {
            System.out.println("loader varattu");
            Thread.sleep((amountKg/200)*1000);
            this.setFree();
            System.out.println("loader vapaa");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
