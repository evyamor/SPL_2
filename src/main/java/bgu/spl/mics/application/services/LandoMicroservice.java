package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
    private final long duration;
    private final Diary diary;

    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration=duration;
        diary=Diary.getInstance();
    }

    @Override
    protected void initialize() {

        subscribeBroadcast(TerminateBroadcast.class,(terminateMe)->{
            diary.setLandoTerminate(System.currentTimeMillis());
            terminate();
        });

        subscribeEvent(BombDestroyerEvent.class,(callback)->{
            try {
                Thread.sleep(duration);
                complete(callback,true);
            }catch (InterruptedException exception){}
        });
    }
}
