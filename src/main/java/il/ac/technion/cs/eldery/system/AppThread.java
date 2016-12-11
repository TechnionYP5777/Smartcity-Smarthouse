/**
 * 
 */
package il.ac.technion.cs.eldery.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import il.ac.technion.cs.eldery.applications.BaseApplication;
import il.ac.technion.cs.eldery.system.exceptions.ApplicationNotRegisteredToEvent;
import il.ac.technion.cs.eldery.utils.Tuple;

/**
 * @author Elia
 * @since Dec 11, 2016
 */
public class AppThread {
  private class EventInfo<L,R>{
    final Consumer<List<Tuple<L,R>>> consumer;
    List<Tuple<L,R>> data = new ArrayList<>();
    
    @SuppressWarnings("unused")
    public EventInfo(Consumer<List<Tuple<L,R>>> $){
      consumer = $;
    }
    
    public void setData(final List<Tuple<L,R>> $){
      data = $;
    }
  }

  //Different sensors => different entry types => different consumers. we want to have them in the same map
  @SuppressWarnings("rawtypes") 
  Map<Long, EventInfo> callOnEvent = new HashMap<>();
  Boolean timeout = Boolean.FALSE, dontTerminate = Boolean.TRUE;
  Long interuptingSensor = null;
  
  BaseApplication app;
  Thread thread = new Thread(){
    @SuppressWarnings({ "boxing", "rawtypes", "unchecked" })
    @Override
    public void run(){
      while(dontTerminate){
        while(!interrupted()){
          // very dynamic much loading TODO: normal run of app. RON?
        }
        if(gotNewEvent()){
          final EventInfo $ = callOnEvent.get(interuptingSensor);
          $.consumer.accept($.data);
          clearAfterEventHandled();
        }
        if(timeout)
          synchronized(timeout){
            try {
              timeout.wait();
            } catch (final InterruptedException e) {
              // yo Dawg, I herd you like interrupts, so I put an interrupt in your interrupt-case so you can 
              // take care of interrupts while you take care of interrupts #tbt
              //seriously tho TODO: ELIA, what is the desired behavior?
              e.printStackTrace();
            }
          }
      }
    }
    
    private boolean gotNewEvent(){
      return interuptingSensor != null;
    }
    
    private void clearAfterEventHandled(){
      interuptingSensor = null;
    }
  };
  
  AppThread(final BaseApplication $){
    app = $;
  }
  
  public void start(){
    thread.start();
  }
  
  public void join() throws InterruptedException{
    dontTerminate = Boolean.FALSE;
    thread.join();
  }
  
  public void pauseThreadRun(){
    timeout = Boolean.TRUE;
    thread.interrupt();
  }
  
  public void continueThreadRun(){
    timeout = Boolean.FALSE;
    synchronized(timeout){
      timeout.notify();
    }
  }

  public <L,R> void notifyOnEvent(final Long eventId, final List<Tuple<L,R>> data) 
                                          throws ApplicationNotRegisteredToEvent{
    @SuppressWarnings("unchecked") final EventInfo<L, R> $ = Optional.ofNullable(callOnEvent.get(eventId))
                                                .orElseThrow(ApplicationNotRegisteredToEvent::new);
    $.setData(data);
    interuptingSensor = eventId;
    thread.interrupt();
  }
}
