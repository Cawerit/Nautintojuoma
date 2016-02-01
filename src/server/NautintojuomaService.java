package server;

import server.machines.*;

import java.rmi.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * Toteutus serverin rajapinnalle
 * @see INautintojuomaService
 */
public class NautintojuomaService implements INautintojuomaService {

    private HashMap<NautintojuomaMachine, IMachine> serverState = new HashMap<>();

    private final LoginService loginService = new LoginService();

    private Silo[] silos = {
            new Silo("hiiva"), new Silo("vesi"), new Silo("humala"), new Silo("ohra")
    };


    private SiloLoader siloLoader = new SiloLoader();
    private Loader[] procLoaders = new Loader[]{ new Loader(200), new Loader(200) };
    private Pump[]
            pumps = new Pump[]{ new Pump(), new Pump() },
            bottlingPumps = new Pump[]{ new Pump(), new Pump() };

    private Tank[] tanks = new Tank[]{
            new Tank("olut"), new Tank("olut"), new Tank("olut"), new Tank("olut"), new Tank("olut"),
            new Tank("olut"), new Tank("olut"), new Tank("olut"), new Tank("olut"), new Tank("olut")
    };


    //Juomakeittimet, joihin mahtuu 2k kiloa raaka-ainetta
    private Container[] processors = new Container[]{ new Processor("hiiva"), new Processor("ohra"), new Processor("humala") };

    public NautintojuomaService () throws RemoteException {
        super();
        //Luodaan HashMap kaikista laitteista, jotta niiden tila voidaan helposti jakaa clientin kanssa
        HashMap<NautintojuomaMachine, IMachine> s = serverState;
        s.put(NautintojuomaMachine.SILO1, silos[0]);
        s.put(NautintojuomaMachine.SILO2, silos[1]);
        s.put(NautintojuomaMachine.SILO3, silos[2]);
        s.put(NautintojuomaMachine.SILO4, silos[3]);
        s.put(NautintojuomaMachine.SILO_LOADER, siloLoader);
        s.put(NautintojuomaMachine.PROC_LOADER1, procLoaders[0]);
        s.put(NautintojuomaMachine.PROC_LOADER2, procLoaders[1]);
        s.put(NautintojuomaMachine.PROCESSOR1, processors[0]);
        s.put(NautintojuomaMachine.PROCESSOR2, processors[1]);
        s.put(NautintojuomaMachine.PROCESSOR3, processors[2]);
        s.put(NautintojuomaMachine.PUMP1, pumps[0]);
        s.put(NautintojuomaMachine.PUMP2, pumps[1]);
        s.put(NautintojuomaMachine.BOTTLING_PUMP1, bottlingPumps[0]);
        s.put(NautintojuomaMachine.BOTTLING_PUMP2, bottlingPumps[1]);
        s.put(NautintojuomaMachine.TANK1, tanks[0]);
        s.put(NautintojuomaMachine.TANK2, tanks[1]);
        s.put(NautintojuomaMachine.TANK3, tanks[2]);
        s.put(NautintojuomaMachine.TANK4, tanks[3]);
        s.put(NautintojuomaMachine.TANK5, tanks[4]);
        s.put(NautintojuomaMachine.TANK6, tanks[5]);
        s.put(NautintojuomaMachine.TANK7, tanks[6]);
        s.put(NautintojuomaMachine.TANK8, tanks[7]);
        s.put(NautintojuomaMachine.TANK9, tanks[8]);
        s.put(NautintojuomaMachine.TANK10, tanks[9]);
    }


    public void login(String name){
        if(name.length() == 0) throw new IllegalArgumentException("Nimi on liian lyhyt");
        loginService.login(name);
    }

    public void logOut(String name){
        loginService.logOut(name);
        for(IMachine m : serverState.values()) m.setFree();
    }

    /**
     * Vaihtaa annetun laitteen varaustilannetta
     */
    public void toggleReservation(NautintojuomaMachine machineName, String name){
        IMachine machine = serverState.get(machineName);
        if(machine == null) throw new NoSuchMachineException(machineName.toString());
        if(machine.isReserved()) {
            if(machine.reservedTo().equals(name)) machine.setFree();
        } else machine.reserve(name);
    }

    /**
     * Alkaa täyttää siiloja
     */
    public void fillSilos(String username){
        if(Objects.equals(siloLoader.reservedTo(), username)) siloLoader.setFree();
        else siloLoader.moveMaterial(username, silos);
    }

    /**
     * Täyttää keittimet
     * @pre usingLoader.equals(NautintojuomaMachine.PROC_LOADER1) || usingLoader.equals(NautintojuomaMachine.PROC_LOADER2)
     * @param usingLoader Toinen PROC_LOADEReista. HUOM! Mikä tahansa laite ei siis kelpaa.
     * @param amount paljonko siirretään
     * @param username
     */
    public void fillProcessors(NautintojuomaMachine usingLoader, int amount, String username){
        Loader l = null;

        if(NautintojuomaMachine.PROC_LOADER1.equals(usingLoader)) l = procLoaders[0];
        else if (NautintojuomaMachine.PROC_LOADER2.equals(usingLoader)) l = procLoaders[1];
        else throw new NoSuchMachineException(usingLoader.toString());

        if(Objects.equals(l.reservedTo(), username)) l.setFree();
        else l.moveMaterial(username, amount, silos, processors);

    }

    /**
     * Käyttää keittimiä
     * @pre serverState.get(processor) instanceof Processor
     * @param processor Keitin jota käytetään. HUOM! Mikä tahansa laite ei siis käy.
     * @param username
     */
    public void process(NautintojuomaMachine processor, String username){
        IMachine m = serverState.get(processor);
        Processor p;
        if(m instanceof  Processor) p = (Processor) m;
        else throw new NoSuchMachineException(processor.toString());
        p.process(username);
    }

    /**
     * Käyttää annettuja pumppuja
     * @param pump Jokin neljästä pumpusta
     * @param amount Paljonko siirretään. Mikäli null, siirretään niin paljon kuin on siirrettävissä
     * @param username Käyttäjänimi
     */
    public void usePumps(NautintojuomaMachine pump, Integer amount, String username){
        Pump p = null; IContainer[] sources; IFillable[] targets;

        if(pump.equals(NautintojuomaMachine.PUMP1)) p = pumps[0];
        else if(pump.equals(NautintojuomaMachine.PUMP2)) p = pumps[1];

        if(p != null) {
            sources = processors;
            targets = tanks;
        }

        else {
            if (pump.equals(NautintojuomaMachine.BOTTLING_PUMP1)) p = bottlingPumps[0];
            else if (pump.equals(NautintojuomaMachine.BOTTLING_PUMP2)) p = bottlingPumps[1];

            if (p != null) {
                sources = tanks;
                targets = new IFillable[]{new Pump.Bottling(username)};
            } else throw new NoSuchMachineException(pump.toString());
        }

        if(amount == null) amount = Arrays.<IContainer>stream(sources)
                .filter(s -> Objects.equals(s.reservedTo(), username))
                .mapToInt(s -> s.getMaterialAmount())
                .sum();

        p.moveMaterial(username, amount, sources, targets);
    }

    /**
     * Palauttaa tiedon palvelimen kaikkien laitteiden tämän hetkisestä tilasta
     */
    public HashMap<NautintojuomaMachine, IMachine> pullState(){
        return serverState;
    };

    private class NoSuchMachineException extends RuntimeException {
        public NoSuchMachineException(String machine){
            super("Machine " + machine + " has not been implemented for this use");
        }
    }

}
