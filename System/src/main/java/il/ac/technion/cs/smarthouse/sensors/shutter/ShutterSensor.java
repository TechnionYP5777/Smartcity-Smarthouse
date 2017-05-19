package il.ac.technion.cs.smarthouse.sensors.shutter;

import il.ac.technion.cs.smarthouse.sensors.Sensor;

/**
 * This class represents shutters sensor and contains its logic.
 * 
 * @author Alex
 * @author Inbal Zukerman
 * @since 8.5.17
 */
public class ShutterSensor extends Sensor {

	public ShutterSensor(final String id, final String systemIP, final int systemPort) {
		super(id, systemIP, systemPort);
	}

	public void updateSystem(final boolean open, final int fromTime, final int toTime) {
		super.updateSystem("shutter.open." + open + ".time." + fromTime);
	}

}
