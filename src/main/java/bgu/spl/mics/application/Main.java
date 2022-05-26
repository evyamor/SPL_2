package bgu.spl.mics.application;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.passiveObjects.Input;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
    private static Vector<Thread> threads = new Vector<>();
    private static List<MicroService> microServices = new LinkedList<>();
    private static Diary diary;
    private static Ewoks ewoks;
    private static MessageBusImpl messageBus;




    private static void start() {
        for (MicroService micro : microServices) {
            Thread thread = new Thread(micro, micro.getName()); // not sure if we need to store them somewhere
            thread.start();
            threads.add(thread);
        }
    }

    private static void joinL() {
        for (Thread thread : threads
        ) {
            try {
                if(thread!=null)
                 thread.join();
            } catch (InterruptedException iExc) {
                iExc.printStackTrace();
            }
        }
    }



    public static void main(String[] args) throws NullPointerException {
        Gson gson = new Gson();
        Input input=new Input();
        try {
            Reader reader = new FileReader(args[0]); //(args[0]) for terminal run input\output
            input = gson.fromJson(reader, Input.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //MicroServices
        MicroService leiaMicroservice = new LeiaMicroservice(input.getAttacks());
        microServices.add(leiaMicroservice);
        MicroService hanSoloMicroservice = new HanSoloMicroservice();
        microServices.add(hanSoloMicroservice);
        MicroService c3poMicroservice = new C3POMicroservice();
        microServices.add(c3poMicroservice);
        MicroService r2D2Microservice = new R2D2Microservice(input.getR2D2());
        microServices.add(r2D2Microservice);
        MicroService landoMicroservice = new LandoMicroservice(input.getLando());
        microServices.add(landoMicroservice);

        messageBus = MessageBusImpl.getInstance();

        ewoks = Ewoks.getInstance();
        ewoks.loadUp(input.getEwoks());

        start();
        joinL();
    }
}

