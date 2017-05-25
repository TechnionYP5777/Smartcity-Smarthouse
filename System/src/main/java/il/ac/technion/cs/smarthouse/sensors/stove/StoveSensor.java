package il.ac.technion.cs.smarthouse.sensors.stove;

import il.ac.technion.cs.smarthouse.sensors.Sensor;
import il.ac.technion.cs.smarthouse.system.Dispatcher;

/**
 * This class represents a temperature sensor for a stove and contains its
 * logic.
 * 
 * @author Yarden
 * @author Sharon
 * @author Inbal Zukerman
 * @since 10.12.16
 */
public class StoveSensor extends Sensor {
    public StoveSensor(final String id, final String systemIP, final int systemPort) {
        super(id, systemIP, systemPort);
    }

    public void updateSystem(final boolean on, final int temperature) {
        super.updateSystem(on, "stove" + Dispatcher.DELIMITER + "on");

        super.updateSystem(temperature, "stove" + Dispatcher.DELIMITER + "temperature");

    }

}
