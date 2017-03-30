package il.ac.technion.cs.eldery.sensors.stove;

import java.util.HashMap;
import java.util.Map;

import il.ac.technion.cs.eldery.sensors.Sensor;

/** This class represents a temperature sensor for a stove.
 * @author Yarden
 * @author Sharon
 * @since 10.12.16 */
public class StoveSensor extends Sensor {
    public StoveSensor(final String id, final String commName, final String systemIP, final int systemPort) {
        super(id, commName, systemIP, systemPort);
    }

    public void updateSystem(final boolean on, final int temperature) {
        final Map<String, String> data = new HashMap<>();
        data.put("on", on + "");
        data.put("temperature", temperature + "");
        super.updateSystem(data);
    }

    @Override public String[] getObservationsNames() {
        return new String[] { "on", "temperature" };
    }
}
