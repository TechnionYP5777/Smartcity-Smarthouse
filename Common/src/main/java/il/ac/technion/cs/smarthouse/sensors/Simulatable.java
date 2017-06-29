package il.ac.technion.cs.smarthouse.sensors;

import java.util.Collection;

import il.ac.technion.cs.smarthouse.sensors.simulator.GenericSensor;

/**
 * @author Elia Traore
 * @since Jun 26, 2017
 */
public interface Simulatable {

	/** notice, even though this method is not static (#java), a Simulatable object cannot
	 * be depended on specific details of implemention and must be able to work with any instance of
	 * obj.getSimulatedSensors() (even if <b>!</b>obj.eqauls(this).  
	 * @return he list of sensors needed to allow the simulatable object to run 
	 * */
	public Collection<GenericSensor> getSimulatedSensors();
}
