/**
 * 
 */
package il.ac.technion.cs.smarthouse.sensors.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import il.ac.technion.cs.smarthouse.sensors.PathType;

/**
 * @author Elia Traore
 * @since Jun 17, 2017
 */
public class SensorsSimulator {
	public enum Action {ADD, REMOVE};
	
    private Map<PathType, Consumer<String>> loggers = new HashMap<>();
    private Map<Action, List<Consumer<GenericSensor>>> listeners;
    private List<GenericSensor> sensors = new ArrayList<>();
    
    private void setLogger(PathType t, Consumer<String> logger){
        loggers.put(t, logger);
    }
    
    private void callListeners(Action a, GenericSensor s){
    	Optional.ofNullable(listeners.get(a)).ifPresent(ls -> ls.forEach(l -> l.accept(s)));
    }

  //------------------------- public API -----------------------------------------
	public SensorsSimulator addSensor(GenericSensor s){
		Stream.of(PathType.values()).forEach(type -> s.addLogger(type, loggers.get(type)));
		sensors.add(s);
		callListeners(Action.ADD, s);
		return this;
	}
	
	public SensorsSimulator removeSensor(GenericSensor s){
		sensors.remove(s);
		callListeners(Action.REMOVE, s);
		return this;
	}
	
	public SensorsSimulator startSendingMsgsInAllSensors(){
		sensors.forEach(s -> new Thread(()->s.streamMessages()).start());
		return this;
	}
	
	public SensorsSimulator setSentMsgLogger(Consumer<String> logger){
		loggers.put(PathType.INFO_SENDING, logger);
		return this;
	}
	
	public SensorsSimulator setInstructionReceivedLogger(Consumer<String> logger){
		loggers.put(PathType.INSTRUCTION_RECEIVING, logger);
		return this;
    }

	//------------------------- access through listener ------------------------- 
	public SensorsSimulator addListenerWhen(Action a, Consumer<GenericSensor> listener){
		if(!listeners.containsKey(a))
			listeners.put(a, new ArrayList<>());
		listeners.get(a).add(listener);
		
		if(Action.ADD.equals(a))//notify on anyone who is already connected
			sensors.forEach(s ->  listener.accept(s));
		return this;
	}
	
}
