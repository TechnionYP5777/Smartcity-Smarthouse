package il.ac.technion.cs.smarthouse.sensors;

import java.util.Collection;

import il.ac.technion.cs.smarthouse.sensors.simulator.GenericSensor;

/**
 * @author Elia Traore
 * @since Jun 26, 2017
 */
public interface Simulatable {

	/**
	 * @return he list of sensors needed to allow the simulatable object to run
	 */
	public Collection<GenericSensor> getSimulatedSensors();
}
