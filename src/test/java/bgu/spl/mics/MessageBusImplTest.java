package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {
    MessageBus testMessageBus;
    AttackEvent event;
    MicroService m;
    //Broadcast broadcast;
    Message message;

    @BeforeEach
    public void setUp() {
        testMessageBus=MessageBusImpl.getInstance();
        event = new AttackEvent(new Attack(new LinkedList<>(),1));

    }

    @Test
    void subscribeEventTest() {
       testMessageBus.subscribeEvent(event.getClass(),m);


    }

    @Test
    void subscribeBroadcastTest() {//deleted by mistake need to fix
    }

    @Test
    void completeTest() {
        Future future = new Future();
        try {
            //   testMessageBus.complete(event,future.get());
        } catch (Exception e) {
            fail("Unexpected Exception: ");
        }
        assertTrue(future.isDone());


    }

    @Test
    void sendBroadcastTest() {
    }

    @Test
    void sendEventTest() {
        m = new HanSoloMicroservice();
        testMessageBus.register(m);
        testMessageBus.subscribeEvent(event.getClass(),m);
        message=null;
        try{
            testMessageBus.sendEvent(event);
            message=testMessageBus.awaitMessage(m);
        }catch (InterruptedException e){}
        assertNotNull(message);
        assertEquals(event,message);
    }

    @Test
    void registerTest() {
        try {
            testMessageBus.register(m);
        }catch (Exception e){}
       // assertNotNull(testMessageBus.getQueue(m));
    }

    // @Test
    //  void unregister() {
    //  }  //said not to test

    @Test
    void awaitMessageTest() {
    }
}