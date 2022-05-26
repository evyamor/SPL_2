package bgu.spl.mics;

import javax.xml.bind.Element;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.*;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

    private final ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> microServicesQueues;//all the queues of the microservices will be stored here
    private final ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>> registeredToMessageType;
    //each kind of message has a quque that shows who is registered to it- it means who can handle this kind of message
    private final ConcurrentHashMap<Event, Future> futures;


    private static class MessageBusWrapper {
        private final static MessageBusImpl instance = new MessageBusImpl();
    }

    private MessageBusImpl() {
        microServicesQueues = new ConcurrentHashMap<>();
        registeredToMessageType = new ConcurrentHashMap<>();
        futures = new ConcurrentHashMap<>();

    }


    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) { //subscribing microservice to be able to handle this kind of event.
        registeredToMessageType.putIfAbsent(type, new ConcurrentLinkedQueue<>()); //checks if this kind of message already has a microService queue,if not creat one
        //adds this microService m to handle this kind of message
        registeredToMessageType.get(type).add(m);
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) { // the same as subscribe event, cause also a type of message.
        registeredToMessageType.putIfAbsent(type, new ConcurrentLinkedQueue<>()); //checks if this kind of message already has a microService queue,if not creat one
        //adds this microService m to handle this kind of message
        registeredToMessageType.get(type).add(m);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void complete(Event<T> e, T result) {
        futures.get(e).resolve(result);
    }

    @Override
    public void sendBroadcast(Broadcast b) { //sends broadcast 'b' to everybody (cause everybody registered to broadcasts)
        if (registeredToMessageType.get(b.getClass()) != null) {
            for (MicroService m : registeredToMessageType.get(b.getClass())) { //going through all microservices registered to broadcast
                microServicesQueues.get(m).add(b); //add to the microservice queue this broadcast
            }
        }

    }

    @Override
    public <T> Future<T> sendEvent(Event<T> e) {

        Future<T> future = new Future<>();
        Class type = e.getClass();
        if (registeredToMessageType.get(type) != null) {
            synchronized (registeredToMessageType.get(type)) {
                if (!registeredToMessageType.get(type).isEmpty()) {
                    MicroService m = registeredToMessageType.get(type).poll();
                    registeredToMessageType.get(type).add(m);
                    futures.put(e, future);
                    microServicesQueues.get(m).add(e);
                } else {
                    return null;
                }
            }

        } else {
            return null;
        }

        return future;
    }


    @Override
    public void register(MicroService m) {
        microServicesQueues.put(m, new LinkedBlockingQueue<>());//creates a queue to this microService
    }

    @Override
    public void unregister(MicroService m) {
        try {
            while (!microServicesQueues.get(m).isEmpty()) {
                microServicesQueues.get(m).take();
            }
        } catch (InterruptedException ex) {
        }
        microServicesQueues.remove(m);

        for (Class<? extends Message> msg : registeredToMessageType.keySet()) {
            if (registeredToMessageType.get(msg).contains(m)) {
                registeredToMessageType.get(msg).remove(m);
            }
        }
    }

    @Override
    public Message awaitMessage(MicroService m) throws InterruptedException {
        if (microServicesQueues.get(m) == null||microServicesQueues.isEmpty()) {// if this microservice queue does not exist it means it didnt register to the messageBus
            throw new IllegalStateException(m.getName() + "is not registered");
        }
        return microServicesQueues.get(m).take();


    }

    public static MessageBusImpl getInstance() {
        return MessageBusWrapper.instance;
    }

}
