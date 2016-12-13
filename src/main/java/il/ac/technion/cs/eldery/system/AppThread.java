/**
 * 
 */
package il.ac.technion.cs.eldery.system;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import il.ac.technion.cs.eldery.system.applications.SmartHouseApplication;
import il.ac.technion.cs.eldery.system.exceptions.ApplicationNotRegisteredToEvent;
import il.ac.technion.cs.eldery.utils.Tuple;

/** This class will hold, run and manage the dynamic loaded code of the
 * application. Using this class will allow us to mimic the context switch of an
 * OS on its programs.
 * @author Elia
 * @since Dec 11, 2016 */
public class AppThread {
    private class EventInfo<L, R> {
        // TODO: ELIA consider moving the beauty that is Consumer<List<Tuple<L,R>>> into the closest thing to typedef
        final Consumer<Tuple<L, R>> consumer;
        Tuple<L, R> data;

        public EventInfo(Consumer<Tuple<L, R>> $) {
            consumer = $;
        }

        public void setData(final Tuple<L, R> $) {
            data = $;
        }
    }

    // Different sensors => different entry types => different consumers. we
    // want to have them in the same map
    @SuppressWarnings("rawtypes") Map<Long, EventInfo> callOnEvent = new HashMap<>();
    Boolean timeout = Boolean.FALSE, dontTerminate = Boolean.TRUE;
    Long interuptingSensor;
    SmartHouseApplication app;
    Thread thread = new Thread() {
        @SuppressWarnings({ "boxing", "rawtypes", "unchecked" }) @Override public void run() {
            while (dontTerminate) {
                while (!interrupted()) {
                    // very dynamic much loading TODO: normal run of app. RON?
                }
                if (gotNewEvent()) {
                    final EventInfo $ = callOnEvent.get(interuptingSensor);
                    $.consumer.accept($.data);
                    clearAfterEventHandled();
                }
                if (timeout)
                    synchronized (timeout) {
                        try {
                            timeout.wait();
                        } catch (final InterruptedException e) {
                            // yo Dawg, I herd you like interrupts, so I put an
                            // interrupt in your interrupt-case so you can
                            // take care of interrupts while you take care of
                            // interrupts #tbt
                            // seriously tho TODO: ELIA, what is the desired
                            // behavior?
                            e.printStackTrace();
                        }
                    }
            }
        }

        boolean gotNewEvent() {
            return interuptingSensor != null;
        }

        void clearAfterEventHandled() {
            interuptingSensor = null;
        }
    };

    public AppThread(final SmartHouseApplication $) {
        app = $;
    }

    /** @see java.lang.Thread#start */
    public void start() {
        thread.start();
    }

    /** @see java.lang.Thread#join */
    public void join() throws InterruptedException {
        dontTerminate = Boolean.FALSE;
        thread.join();
    }

    public void pauseThreadRun() {
        timeout = Boolean.TRUE;
        thread.interrupt();
    }

    public void continueThreadRun() {
        timeout = Boolean.FALSE;
        synchronized (timeout) {
            timeout.notify();
        }
    }

    /** Adds a consumer to the application, that can process infotmation from a
     * specific sensor
     * @return The id of the consumer in the system, needed for any activation
     *         action of the consumer. */
    public <L, R> Long registerEventConsumer(Consumer<Tuple<L, R>> $) {
        Long id = Long.valueOf(callOnEvent.size() + 1); // TODO: change id calc
                                                        // if removal is allowed
        callOnEvent.put(id, new EventInfo<>($));
        return id;
    }

    /** Gives the AppThread the new info and forces it to process it
     * @param eventId: The id returned at the registration of the consumer that
     *        can process the data
     * @param data: The new information from the sensor */
    public <L, R> void notifyOnEvent(final Long eventId, final Tuple<L, R> data) throws ApplicationNotRegisteredToEvent {
        @SuppressWarnings("unchecked") final EventInfo<L, R> $ = Optional.ofNullable(callOnEvent.get(eventId))
                .orElseThrow(ApplicationNotRegisteredToEvent::new);
        $.setData(data);
        interuptingSensor = eventId;
        thread.interrupt();
    }
}
