/**
 * 
 */
package il.ac.technion.cs.simulation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import il.ac.technion.cs.smarthouse.sensors.PathType;
import il.ac.technion.cs.smarthouse.sensors.simulator.GenericSensor;
import il.ac.technion.cs.smarthouse.sensors.simulator.SensorBuilder;
import il.ac.technion.cs.smarthouse.sensors.simulator.SensorsSimulator;

/**
 * @author Elia Traore
 * @since Jun 22, 2017
 */
public class Simulation extends Thread{
	public enum AcuAction{STOP,HOTTER,COLDER;
		Boolean isOn(){
			return !this.equals(STOP);
		}
	};
	
	private class DataContainer {
		private final String id;
		private Integer temperature, defaultTemp;
		private AcuAction action;
		
		public DataContainer(String id,Integer defaultTemp){
			this.id = id;
			this.defaultTemp = defaultTemp;
			this.temperature = defaultTemp +10*(ThreadLocalRandom.current().nextBoolean()? 1:-1);
			this.action = AcuAction.STOP;
		}
		

		/** returns the old temperature
		 * */
		public synchronized Integer updateTemperature(Integer delta){
			Integer $ = temperature;
			temperature += delta;
			return $;
		}
		
		/** returns the old temperature
		 * */
		public synchronized Integer getTemperatureCloserTo(){
			Integer delta = temperature - defaultTemp;
			delta = delta/Math.abs(delta);
			
			Integer $ = temperature;
			temperature -= delta;
			return $;
		}
		
		public synchronized void setAction(AcuAction action){
			this.action = action;
		}
		
		public synchronized AcuAction getAction(){
			return this.action;
		}
		
		public synchronized void setDefaultTemperature(Integer temperature){
			defaultTemp = temperature;
		}
	}
	
	public final static String commname = "ACUnit";
	public final static String instsBase = "wanted", obsersBase ="current";
	public final static String tempSuffix = "temperature", stateSuffix = "state", defaultTempSuffix="defaultTemperature";

	Integer defaultTemp;
	final Map<String, DataContainer> sensors;
	SensorBuilder builder;
	
	SensorsSimulator simulator;
	
	public Simulation(Integer defaultTemp, String ... aliases){
		this.defaultTemp = defaultTemp;
		sensors = new HashMap<>();
		
		builder = new SensorBuilder().setCommname(commname);
		builder.addInfoSendingPath(getPath(PathType.INFO_SENDING,tempSuffix), Integer.class);
		builder.addInfoSendingPath(getPath(PathType.INFO_SENDING,stateSuffix), Boolean.class);
		builder.addInstructionsReceiveingPath(getPath(PathType.INSTRUCTION_RECEIVING,stateSuffix));
		builder.addInstructionsReceiveingPath(getPath(PathType.INSTRUCTION_RECEIVING,defaultTempSuffix));
		
	}
	
	public static String getPath(PathType type, String suffix){
		return String.join(".", commname, 
								(PathType.INFO_SENDING.equals(type)? obsersBase : instsBase), suffix);
	}

	
	public Set<String> getAliases(){
		return sensors.keySet();
	}
	
	@Override
	public void interrupt() {
		simulator.stopSendingMsgsInAllSensors();
		super.interrupt();
	}
	

	@Override
	public void run() {
		simulator = new SensorsSimulator();
		sensors.keySet().forEach(alias -> {
			GenericSensor s = builder.setAlias(alias)
					.setMessageSupplier(()->{
						Integer newT = 0;
						AcuAction a = sensors.get(alias).getAction();
						if(a.isOn())
							newT = sensors.get(alias).updateTemperature(AcuAction.HOTTER.equals(a)? +1 : -1);
						else if(ThreadLocalRandom.current().nextBoolean())
							newT = sensors.get(alias).getTemperatureCloserTo();
						
						Map<String, Object> data = new HashMap<>();
						data.put(getPath(PathType.INFO_SENDING,tempSuffix), newT);
						data.put(getPath(PathType.INFO_SENDING,stateSuffix), a.isOn());
						return data;
					})
					.setInstructionHandler((path,val)->{
						try{
							if(getPath(PathType.INSTRUCTION_RECEIVING,stateSuffix).equals(path))
								sensors.get(alias).setAction(AcuAction.valueOf(val));
							if(getPath(PathType.INSTRUCTION_RECEIVING,defaultTempSuffix).equals(path))
								sensors.get(alias).setDefaultTemperature(Integer.parseInt(val));
								
						}catch(Exception e){
							return false;
						}
						return true;
					})
					.build();
			final String id = simulator.addSensor(s);
			sensors.put(alias, new DataContainer(id, defaultTemp));
		});
		
		simulator.startSendingMsgsInAllSensors();
	}
	


}
