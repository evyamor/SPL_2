package bgu.spl.mics.application.services;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewok;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.Comparator;
import java.util.List;

/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {
    private final Ewoks ewoks;
    private final Diary diary;

    public C3POMicroservice() {
        super("C3PO");
        ewoks = Ewoks.getInstance();
        diary=Diary.getInstance();
    }

    @Override
    protected void initialize() {
        //terminate
        subscribeBroadcast(TerminateBroadcast.class, (toTerminate)->{
            diary.setC3POTerminate(System.currentTimeMillis()); //termination time documentation in diary
            terminate();
        });

        subscribeEvent(AttackEvent.class, (c3p0_attackCallback) -> {
            List<Integer> callEwoks = c3p0_attackCallback.getAttack().getSerials();
            callEwoks.sort(Comparator.naturalOrder());
            try {
                ewoks.callForResource(callEwoks);
                Thread.sleep(c3p0_attackCallback.getAttack().getDuration());
                }catch (InterruptedException e){e.printStackTrace();}

                diary.increaseTotalAttacks();// each attack should be documented in the diary
                diary.setC3POFinish(System.currentTimeMillis());
                ewoks.releaseEwok(c3p0_attackCallback.getAttack().getSerials());
                complete(c3p0_attackCallback,true);
            //}
        });
    }
}
