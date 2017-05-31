package il.ac.technion.cs.smarthouse.sensors.vitals;

import il.ac.technion.cs.smarthouse.sensors.Sensor;
import il.ac.technion.cs.smarthouse.system.Dispatcher;

/**
 * This class represents a vitals signs sensor and contains its logic.
 * 
 * @author Yarden
 * @author Inbal Zukerman
 * @since 16.1.17
 */
public class VitalsSensor extends Sensor {
    public VitalsSensor(final String id, final String systemIP, final int systemPort) {
        super(id, systemPort);
    }

    public void updateSystem(final int pulse, final int systolicBP, final int diastolicBP) {

        super.updateSystem(pulse, "vitals" + Dispatcher.DELIMITER + "pulse");

        super.updateSystem(systolicBP, "vitals" + Dispatcher.DELIMITER + "systolicBP");

        super.updateSystem(diastolicBP, "vitals" + Dispatcher.DELIMITER + "diastolicBP");

    }

}
