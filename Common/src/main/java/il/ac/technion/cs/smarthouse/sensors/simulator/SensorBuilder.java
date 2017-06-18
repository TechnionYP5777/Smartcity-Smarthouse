/**
 * 
 */
package il.ac.technion.cs.smarthouse.sensors.simulator;

import java.util.function.Consumer;

import il.ac.technion.cs.smarthouse.sensors.InstructionHandler;
import il.ac.technion.cs.smarthouse.sensors.InteractiveSensor;
import il.ac.technion.cs.smarthouse.sensors.PathType;
import il.ac.technion.cs.smarthouse.utils.Random;

/**
 * @author Elia Traore
 * @since Jun 17, 2017
 */
public class SensorBuilder {
	private String sensorId = Random.sensorId();
    private String commname, alias;
    private InstructionHandler iHandler;

    private final GenericSensor genericSensor = new GenericSensor();
    
    /** @return null if the commname, sensorId or alias we're initialized, 
     * 			otherwise a generic sensor based on the set properties
     * */
	public GenericSensor build(){
		if(commname == null || alias == null)
			return null;
		GenericSensor newSensor = new GenericSensor(genericSensor);
		
		newSensor.setSensor(new InteractiveSensor(commname, sensorId, alias, 
													newSensor.getPaths(PathType.INFO_SENDING),
													newSensor.getPaths(PathType.INSTRUCTION_RECEIVING)));
		newSensor.getSensor().setInstructionHandler((path,inst)->{
			newSensor.logInstruction(path,inst);
			return iHandler == null || iHandler.applyInstruction(path, inst);
		});
		return newSensor;
	}
	
    public SensorBuilder setInstructionHandler(InstructionHandler h){
        iHandler = h;
        return this;
    }
    
    public SensorBuilder setCommname(String comm){
        commname = comm;
        return this;
    }
    
    /** if not called, a random Id will be chosen
     * */
    public SensorBuilder setSensorId(String sensorId){
        this.sensorId = sensorId;
        return this;
    }
    
    public SensorBuilder setAlias(String alias){
        this.alias = alias;
        return this;
    }

    private SensorBuilder addPath(PathType type, String path, Class pathClass){
    	genericSensor.addPath(type, path, pathClass);
    	return this;
    }
    
    public SensorBuilder addInfoSendingPath(String path, Class pathClass){
    	return addPath(PathType.INFO_SENDING, path, pathClass);
    }
    
    public SensorBuilder addInstructionsReceiveingPath(String path){
    	return addPath(PathType.INSTRUCTION_RECEIVING, path, null);
    }
    public SensorBuilder addLogger(PathType t, Consumer<String> logger){
    	genericSensor.addLogger(t, logger);
    	return this;
    }
    
    public SensorBuilder setPollingInterval(Long milliseconds){
    	genericSensor.setPollingInterval(milliseconds);
    	return this;
    }
}
