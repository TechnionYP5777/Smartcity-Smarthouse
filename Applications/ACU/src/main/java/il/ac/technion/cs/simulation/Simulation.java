/**
 * 
 */
package il.ac.technion.cs.simulation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
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
	
	private class AcuState {
		private final String id;
		private Integer currTemp, defaultTemp;
		private AcuAction action;
		
		public AcuState(String id,Integer defaultTemp){
			this.id = id;
			this.defaultTemp = defaultTemp;
			this.currTemp = defaultTemp -10;//*(ThreadLocalRandom.current().nextBoolean()? 1:-1);
			this.action = AcuAction.STOP;
		}
		
		private synchronized Map<String, Object> getCurrentState(Boolean calcFuture){
			Map<String, Object> data = new HashMap<>();
			data.put(getPath(PathType.INFO_SENDING,tempSuffix), currTemp);
			data.put(getPath(PathType.INFO_SENDING,stateSuffix), action.isOn());

			if(calcFuture){
				if(action.isOn())
					currTemp += 5*(AcuAction.HOTTER.equals(action)? +1 : -1);
				else{ //if(ThreadLocalRandom.current().nextBoolean())
					Integer delta = currTemp - defaultTemp;
					delta = delta.equals(0) ? delta :delta/Math.abs(delta);
					currTemp -= delta;
				}
			
			}
			
			return data;
		}
		
		public synchronized Map<String, Object> getCurrentState(){
			return getCurrentState(true);
		}
		
		public synchronized Boolean processInstruction(String path, String val){
			Supplier<Map<String,Object>> getm = ()->{
				Map<String,Object> m = new HashMap<>();
				m.put("deftemp", defaultTemp);
				m.put("action", action);
				return m;
			};
			Map<String,Object> old = getm.get();
			if(getPath(PathType.INSTRUCTION_RECEIVING,stateSuffix).equals(path))
				action = AcuAction.valueOf(val);
			if(getPath(PathType.INSTRUCTION_RECEIVING,defaultTempSuffix).equals(path))
//				defaultTemp = Integer.parseInt(val);
				defaultTemp =  new Double(Double.parseDouble(val)).intValue();
			Map<String,Object> curr = getm.get();
			
			Boolean eqls = old.keySet().stream()
										.allMatch(k-> Optional.ofNullable(old.get(k))
																.map(oldval -> oldval.equals(curr.get(k)))
																.orElse(curr.get(k) == null)
																
																
												);
			System.out.println(!eqls? 
										"old:\n"+old+"\new:\n"+curr : 
											"received \ninst:"+path+"\tval:"+val+"\nbut nothing changed!");
			return true;
		}
	}
	
	public static final String commname = "ACUnit";
	public static final String instsBase = "wanted", obsersBase = "current";
	public static final String tempSuffix = "temperature", stateSuffix = "state",
			defaultTempSuffix = "defaultTemperature";

	Integer defaultTemp;
	final Map<String, AcuState> sensors;
	SensorBuilder builder;
	
	SensorsSimulator simulator;
	
	/**
	 * [[SuppressWarningsSpartan]]
	 */
	public Simulation(Integer defaultTemp, String ... aliases){
		this.defaultTemp = defaultTemp;
		sensors = new HashMap<>();
		Stream.of(aliases).forEach(key -> sensors.put(key+" ACU sensor", null));
		
		builder = new SensorBuilder()
				.setCommname(commname)
				.addInfoSendingPath(getPath(PathType.INFO_SENDING,tempSuffix), Integer.class)
				.addInfoSendingPath(getPath(PathType.INFO_SENDING,stateSuffix), Boolean.class)
				.addInstructionsReceiveingPath(getPath(PathType.INSTRUCTION_RECEIVING,stateSuffix))
				.addInstructionsReceiveingPath(getPath(PathType.INSTRUCTION_RECEIVING,defaultTempSuffix))
				.setStreamInterval(TimeUnit.SECONDS.toMillis(5))
				.setPollingInterval(TimeUnit.SECONDS.toMillis(1));

		//previously the run method
		simulator = new SensorsSimulator();
//				.addSentMsgLogger(s -> System.out.println("MSGs LOGGER:" + s))
//				.addInstructionReceivedLogger(s -> System.out.println("INSTRUCTIONSs LOGGER:\n" + s));
		
		sensors.keySet().forEach(alias -> {
			GenericSensor s = builder.setAlias(alias)
					.setMessageSupplier(()->sensors.get(alias).getCurrentState())
					.setInstructionHandler((p,v)->sensors.get(alias).processInstruction(p,v))
					.build();
			final String id = simulator.addSensor(s);
			sensors.put(alias, new AcuState(id, defaultTemp));
		});
	}
	
	public static String getPath(PathType type, String suffix){
		return String.join(".", commname, 
								(PathType.INFO_SENDING.equals(type)? obsersBase : instsBase), suffix);
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
