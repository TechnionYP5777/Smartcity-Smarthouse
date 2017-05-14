package il.ac.technion.cs.smarthouse.sensors.vitals;

import il.ac.technion.cs.smarthouse.sensors.Sensor;

/**
 * This class represents a vitals signs sensor and contains its logic.
 * 
 * @author Yarden
 * @author Inbal Zukerman
 * @since 16.1.17
 */
public class VitalsSensor extends Sensor {
	public VitalsSensor(final String id, final String systemIP, final int systemPort) {
		super(id, systemIP, systemPort);
	}

	public void updateSystem(final int pulse, final int systolicBP, final int diastolicBP) {
		String data = "pulse." + pulse;
		super.updateSystem(data);
		data = "systolicBP." + systolicBP;
		super.updateSystem(data);
		data = "diastolicBP." + diastolicBP;
		super.updateSystem(data);
	}

}
