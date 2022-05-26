package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.application.services.HanSoloMicroservice;
import bgu.spl.mics.application.services.LandoMicroservice;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {

    private static class DiaryWrapper {
        private final static Diary instance = new Diary();
    }

    public static Diary getInstance() {
        return DiaryWrapper.instance;
    }

    private final AtomicInteger totalAttacks = new AtomicInteger(0);
    private long HanSoloFinish;
    private long HanSoloTerminate;
    private long C3POFinish;
    private long C3POTerminate;
    private long R2D2Deactivate;
    private long R2D2Terminate;
    private long LeiaTerminate;
    private long LandoTerminate;


    private Diary() {
        //TODO setting values at constructor? make sure there are no illegal requests!
        /*HanSoloFinish=0;
        HanSoloTerminate=0;
        C3POFinish=0;
        C3POTerminate=0;
        R2D2Terminate=0;
        LeiaTerminate=0;
        LandoTerminate=0;


         */

    }


    //setters
    public void increaseTotalAttacks() {
        int val;
        do {
            val = totalAttacks.get();
        } while (!totalAttacks.compareAndSet(val, val + 1));
    }

    public void setHanSoloFinish(long finish) {
        HanSoloFinish = finish;
    }

    public void setHanSoloTerminate(long terminate) {
        HanSoloTerminate = terminate;
    }

    public void setC3POFinish(long finish) {
        C3POFinish = finish;
    }

    public void setC3POTerminate(long terminate) {
        C3POTerminate = terminate;
    }

    public void setR2D2Deactivate(long deactivate) {
        R2D2Deactivate = deactivate;
    }

    public void setR2D2Terminate(long terminate) {
        R2D2Terminate = terminate;
    }

    public void setLeiaTerminate(long terminate) {
        LeiaTerminate = terminate;
    }

    public void setLandoTerminate(long terminate) {
        LandoTerminate = terminate;
    }

    //getters
    public AtomicInteger getTotalAttacks() {
        return totalAttacks;
    }

    public long getHanSoloFinish() {
        return HanSoloFinish;
    }

    public long getHanSoloTerminate(){ return HanSoloTerminate;}

    public long getC3POFinish() {
        return C3POFinish;
    }

    public long getC3POTerminate() {
        return C3POTerminate;
    }

    public long getR2D2Deactivate() {
        return R2D2Deactivate;
    }

    public long getR2D2Terminate() {
        return R2D2Terminate;
    }

    public long getLandoTerminate() {
        return LandoTerminate;
    }

    public long getLeiaTerminate() {
        return LeiaTerminate;
    }



    public String toString(){
        return "Total Attacks: "+ getTotalAttacks() +"\n"+
                "HanSolo Finish Time : " + getHanSoloFinish()+"\n"+
                "C3P0 Finish Time : " + getC3POFinish() + "\n" +
                "R2D2 Deactivate Time : " + getR2D2Deactivate() +"\n" +
                "HanSolo Terminate Time : "+ getHanSoloTerminate() + "\n"+
                "C3P0 Terminate Time : "+ getC3POTerminate() + "\n" +
                "R2D2 Terminate Time : " + getR2D2Terminate() +"\n" +
                "Lando Terminate Time: " +getLandoTerminate() +"\n" +
                "Leia Terminate Time : " + getLeiaTerminate();
    }
    public void resetNumberAttacks(){
        int val;
        do {
            val=totalAttacks.get();
        }
        while (!totalAttacks.compareAndSet(val, 0));
    }
}

