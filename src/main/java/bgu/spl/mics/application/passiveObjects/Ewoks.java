package bgu.spl.mics.application.passiveObjects;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
    private final Map<Integer, Ewok> ewoks = new HashMap<>();

    private static class EwoksWrapper {
        private final static Ewoks instance = new Ewoks();
    }

    public static Ewoks getInstance() {
        return EwoksWrapper.instance;
    }

    //BUILDER
    private Ewoks() {
    }

    public synchronized void loadUp(int numOfEwoksToLoad) {
        for (int i = 1; i <= numOfEwoksToLoad; i++) { // this way we keep assigning new names to Ewoks
            ewoks.put(i, new Ewok(i));
        }
    }

    public synchronized  void callForResource(List<Integer> serials){
        try {
        for (Integer serialNumber : serials) { // we will return false on the first unavailable Ewok
                while (!ewoks.get(serialNumber).isAvailable())
                        wait();

                ewoks.get(serialNumber).acquire(); // when available
                }
            }  catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


            //RELEASE EWOK
            public synchronized void releaseEwok (List < Integer > serials) {
                for (Integer serialNumber : serials) {
                    if (ewoks.containsKey(serialNumber)) {
                        ewoks.get(serialNumber).release();
                    }
                }
                notifyAll();
            }
        }
