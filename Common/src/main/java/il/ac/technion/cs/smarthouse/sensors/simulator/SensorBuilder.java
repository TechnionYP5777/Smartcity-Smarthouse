package il.ac.technion.cs.smarthouse.sensors.simulator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.google.common.base.Supplier;

import il.ac.technion.cs.smarthouse.sensors.InstructionHandler;
import il.ac.technion.cs.smarthouse.sensors.InteractiveSensor;
import il.ac.technion.cs.smarthouse.sensors.PathType;
import il.ac.technion.cs.smarthouse.utils.Random;

/**
 * @author Elia Traore
 * @since Jun 17, 2017
 */

@SuppressWarnings("rawtypes")
public class SensorBuilder {
	private String sensorId;
	private String commname, alias;
	private InstructionHandler iHandler;

	private Map<String, List> ranges;

	private final GenericSensor genericSensor = new GenericSensor();

	/**
	 * @return null if the commname, sensorId or alias we're initialized,
	 *         otherwise a generic sensor based on the set properties
	 */
	public GenericSensor build() {
		if (commname == null || alias == null)
			return null;
		if (sensorId == null)
			sensorId = Random.sensorId();

		GenericSensor newSensor = new GenericSensor(genericSensor);

		newSensor.setRanges(ranges);
		newSensor.setSensor(new InteractiveSensor(commname, sensorId, alias, newSensor.getPaths(PathType.INFO_SENDING),
				newSensor.getPaths(PathType.INSTRUCTION_RECEIVING)));
		newSensor.getSensor().setInstructionHandler((path, inst) -> {
			newSensor.logInstruction(path, inst);
			return iHandler == null || iHandler.applyInstruction(path, inst);
		});

		// force the builder to generate new id when the build method is called
		sensorId = null;
		return newSensor;
	}

	//--------------------- sensor identification ------------------------
	/**
	 * if not called, a random Id will be chosen
	 */
	public SensorBuilder setSensorId(String sensorId) {
		this.sensorId = sensorId;
		return this;
	}

	public SensorBuilder setCommname(String comm) {
		commname = comm;
		return this;
	}

	public SensorBuilder setAlias(String alias) {
		this.alias = alias;
		return this;
	}

	//--------------------- paths setting --------------------------------
	public SensorBuilder addPath(PathType t, String path, Class pathClass) {
		genericSensor.addPath(t, path, pathClass);
		return this;
	}

	public SensorBuilder addInfoSendingPath(String path, Class pathClass) {
		return addPath(PathType.INFO_SENDING, path, pathClass);
	}
	

	public SensorBuilder addInstructionsReceiveingPath(String path) {
		return addPath(PathType.INSTRUCTION_RECEIVING, path, null);
	}


	//--------------------- interval setting -----------------------------
	public SensorBuilder setPollingInterval(Long milliseconds) {
		genericSensor.setPollingInterval(milliseconds);
		return this;
	}

	
	public SensorBuilder setStreamInterval(Long milliseconds) {
		genericSensor.setStreamInterval(milliseconds);
		return this;
	}

	//--------------------- logging setting-------------------------------
	public SensorBuilder addLogger(PathType t, Consumer<String> logger) {
		genericSensor.addLogger(t, logger);
		return this;
	}

	
	public SensorBuilder addInfoSendingLogger(Consumer<String> logger) {
		return addLogger(PathType.INFO_SENDING, logger);
	}

	
	public SensorBuilder addInstructionsReceiveingLogger(Consumer<String> logger) {
		return addLogger(PathType.INSTRUCTION_RECEIVING, logger);
	}
	
	//--------------------- additional settings --------------------------
	public SensorBuilder setInstructionHandler(InstructionHandler h) {
		iHandler = h;
		return this;
	}

	public SensorBuilder addStreamingRange(String path, List values) {
		if (ranges == null)
			ranges = new HashMap<>();
		ranges.put(path, values);
		return this;
	}

	public SensorBuilder setMessageSupplier(Supplier<Map<String, Object>> msgsGenerator){
		genericSensor.setMsgsSupplier(msgsGenerator);
		return this;
	}
}
