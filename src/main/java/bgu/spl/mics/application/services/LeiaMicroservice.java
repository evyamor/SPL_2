package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class
LeiaMicroservice extends MicroService {
    private Attack[] attacks;
    private final Future[] futures;
    private final Diary diary;

    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
        this.attacks = attacks;
        futures = new Future[attacks.length];
        diary = Diary.getInstance();

    }

    private boolean allAttacksDone() { // verifies if hanSolo & c3p0 finished attacking
        for (Future future : futures) {
            if ((future == null) || !future.isDone())
                return false;
        }
        return true;
    }
    private void terminateAll() { // once Lando ( went sleeping ) all can terminate gracefully
        sendBroadcast(new TerminateBroadcast());
    }

    private static void makeToJson(Diary diary) {
        System.out.println(diary.toString());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("output.json")) {
            gson.toJson(diary, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void initialize() {
        subscribeBroadcast(TerminateBroadcast.class, (terminateMe) -> {
            diary.setLeiaTerminate(System.currentTimeMillis());//termination time documentation in diary
            terminate();
        });
        for (int i = 0; i < attacks.length; i++) {
            AttackEvent attackEvent = new AttackEvent(attacks[i]);
            futures[i] = sendEvent(attackEvent);

        }
        while (!allAttacksDone()) {
            for (Future f : futures) {
                if (f != null)
                    f.get();
            }
        }

        Future<Boolean> deActivate = sendEvent(new DeactivationEvent());
        deActivate.get();
        Future<Boolean> bomb = sendEvent(new BombDestroyerEvent());
        bomb.get();



        terminateAll(); // sends a termination call to all threads
        diary.setLeiaTerminate(System.currentTimeMillis());//termination time documentation in diary
        makeToJson(diary);
        terminate();



    }


}
