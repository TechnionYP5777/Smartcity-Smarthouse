/**
 * 
 */
package il.ac.technion.cs.simulation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import il.ac.technion.cs.smarthouse.sensors.PathType;
import il.ac.technion.cs.smarthouse.sensors.simulator.GenericSensor;
import il.ac.technion.cs.smarthouse.sensors.simulator.SensorBuilder;
import il.ac.technion.cs.smarthouse.sensors.simulator.SensorsSimulator;

/**
 * @author Elia Traore
 * @since Jun 22, 2017
 * 
 * The simulation randomize temperatures to the given rooms, and get closer to the default given
 * temperature as time progresses.
 * If the room sensor receive an instruction to turn on an ACU, the temperature will increase/decrease 
 * according to the instruction, until an instruction to stop the ACU is given
 */
public class Simulation {
	public enum AcuAction{STOP,HOTTER,COLDER;
		Boolean isOn(){
			return !this.equals(STOP);
		}
	};
	
	public static final String commname = "ACUnit";
	public static final String instsBase = "wanted", obsersBase = "current";
	public static final String tempSuffix = "temperature", stateSuffix = "state",
			defaultTempSuffix = "defaultTemperature";

	Integer naturalRoomTemperature;
	final Map<String, AcuState> sensors;
	SensorBuilder builder;
	
	SensorsSimulator simulator;
	
	/**
	 * [[SuppressWarningsSpartan]]
	 */
	public Simulation(Integer defaultTemp, String ... aliases){
		this.naturalRoomTemperature = defaultTemp;
		
		builder = new SensorBuilder()
				.setCommname(commname)
				.addInfoSendingPath(getPath(PathType.INFO_SENDING,tempSuffix), Integer.class)
				.addInfoSendingPath(getPath(PathType.INFO_SENDING,stateSuffix), Boolean.class)
				.addInstructionsReceiveingPath(getPath(PathType.INSTRUCTION_RECEIVING,stateSuffix))
				.addInstructionsReceiveingPath(getPath(PathType.INSTRUCTION_RECEIVING,defaultTempSuffix))
				.setStreamInterval(TimeUnit.SECONDS.toMillis(5))
				.setPollingInterval(TimeUnit.SECONDS.toMillis(1));

		sensors = new HashMap<>();
		simulator = new SensorsSimulator();
		Stream.of(aliases)
				.map(key -> key+" ACU sensor")
				.forEach(alias -> {
					GenericSensor s = builder.setAlias(alias)
							.setMessageSupplier(()->sensors.get(alias).getCurrentState())
							.setInstructionHandler((p,v)->sensors.get(alias).processInstruction(p,v))
							.build();
					final String id = simulator.addSensor(s);
					sensors.put(alias, new AcuState(id, naturalRoomTemperature));
				});
	}
	
	public static String getPath(PathType type, String suffix){
		return String.join(".", commname, (PathType.INFO_SENDING.equals(type)? obsersBase : instsBase), suffix);
	}

	
	public Set<String> getAliases(){
		return sensors.keySet();
	}
	
	
	public SensorsSimulator getSimulator(){
		return simulator;
	}
	
	public Collection<GenericSensor> getSensors(){
		return simulator.getAllSensors();
	}
}
