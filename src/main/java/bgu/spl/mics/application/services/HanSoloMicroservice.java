package bgu.spl.mics.application.services;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.Comparator;
import java.util.List;


/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {
    private final Ewoks ewoks;
    private final Diary diary;


    public HanSoloMicroservice() {
        super("Han");
        ewoks = Ewoks.getInstance();
        diary = Diary.getInstance();
    }

    @Override
    protected void initialize() {

        subscribeBroadcast(TerminateBroadcast.class, (terminateMe) -> {
            diary.setHanSoloTerminate(System.currentTimeMillis());//termination time documentation in diary
            terminate();
        });

        subscribeEvent(AttackEvent.class, (attackCallback) -> {
            List<Integer> callEwoks = attackCallback.getAttack().getSerials();
            callEwoks.sort(Comparator.naturalOrder());
            try {
                ewoks.callForResource(callEwoks);
                Thread.sleep(attackCallback.getAttack().getDuration());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                diary.increaseTotalAttacks(); // each attack should be documented in the diary
                diary.setHanSoloFinish(System.currentTimeMillis());
                ewoks.releaseEwok(attackCallback.getAttack().getSerials());
                complete(attackCallback, true);
            /*}
            catch (InterruptedException e) {
             e.printStackTrace();
            }

             */
            });
        }
    }
