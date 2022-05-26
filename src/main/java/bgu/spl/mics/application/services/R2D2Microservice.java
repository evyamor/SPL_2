package bgu.spl.mics.application.services;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {
    private final long duration;
    private final Diary diary;

    public R2D2Microservice(long duration) {
        super("R2D2");
        this.duration = duration;
        diary = Diary.getInstance();
    }

    @Override
    protected void initialize() {
        //termination in the end
        subscribeBroadcast(TerminateBroadcast.class, (terminateMe) -> {
            diary.setR2D2Terminate(System.currentTimeMillis());//termination time documentation in diary
            terminate();
        });

        subscribeEvent(DeactivationEvent.class, (deActivationCallBack) -> {
            try {
                Thread.sleep(duration);
                complete(deActivationCallBack,true);
                diary.setR2D2Deactivate(System.currentTimeMillis());//not sure if here.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
