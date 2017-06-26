package il.ac.technion.cs.smarthouse.sensors.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import il.ac.technion.cs.smarthouse.sensors.PathType;

/**
 * @author Elia Traore
 * @since Jun 17, 2017
 */
public class SensorsSimulator {
	public enum Action {
		ADD, REMOVE, GET
	};

	private Integer id = 0;

	private Map<PathType, Set<Consumer<String>>> loggers = new HashMap<>();
	private Map<Action, List<Consumer<GenericSensor>>> listeners = new HashMap<>();
	private Map<String, GenericSensor> sensors = new HashMap<>();
	private String selectedSensor;

	private void addLogger(PathType t, Consumer<String> logger) {
		if (!loggers.containsKey(t))
			loggers.put(t, new HashSet<>());
		loggers.get(t).add(logger);
		sensors.values().forEach(s -> s.addLogger(t, logger));
	}

	private void callListeners(Action a, GenericSensor s) {
		Optional.ofNullable(listeners.get(a)).ifPresent(ls -> ls.forEach(l -> l.accept(s)));
	}

	private String getNextId() {
		return id++ + "";
	}

	// ------------------------- public API -----------------------------------
	public String addSensor(GenericSensor s) {
		Stream.of(PathType.values())
				.forEach(type -> Optional.ofNullable(loggers.get(type))
										.ifPresent(ls -> ls.forEach(logger -> s.addLogger(type, logger))));
		String id = getNextId();
		sensors.put(id, s);
		callListeners(Action.ADD, s);
		return id;
	}

	public SensorsSimulator removeSensor(String id) {
		callListeners(Action.REMOVE, sensors.get(id));
		sensors.remove(id);
		return this;
	}

	public GenericSensor getSensor(String id) {
		callListeners(Action.GET, sensors.get(id));
		return sensors.get(id);
	}
	
	public SensorsSimulator updateSensor(String id,GenericSensor s){
		sensors.put(id,s);
		return this;
	}
	
	public SensorsSimulator startSendingMsgsInAllSensors() {
		sensors.values().forEach(s -> s.streamMessages());
		return this;
	}
	
	public SensorsSimulator stopSendingMsgsInAllSensors(){
		sensors.values().forEach(s -> s.stopStreaming());
		return this;
	}

	public String getSensorId(GenericSensor s){
		return sensors.keySet().stream()
						.filter(id -> sensors.get(id).equals(s))
						.findFirst().orElse(null);
	}
	
	//todo: is this here?
	public SensorsSimulator setSelectedSensor(String id) {
		this.selectedSensor = id;
		return this;
	}
	
	public String getSelectedSensor() {
		return selectedSensor;
	}
	
	// ---------- access through listeners/loggers ----------
	public SensorsSimulator addSentMsgLogger(Consumer<String> logger) {
		addLogger(PathType.INFO_SENDING, logger);
		return this;
	}

	public SensorsSimulator addInstructionReceivedLogger(Consumer<String> logger) {
		addLogger(PathType.INSTRUCTION_RECEIVING, logger);
		return this;
	}

	public SensorsSimulator addListenerWhen(Action a, Consumer<GenericSensor> listener) {
		if (!listeners.containsKey(a))
			listeners.put(a, new ArrayList<>());
		listeners.get(a).add(listener);

		if (Action.ADD.equals(a))// notify on anyone who is already connected
			sensors.values().forEach(s -> listener.accept(s));
		return this;
	}

}
