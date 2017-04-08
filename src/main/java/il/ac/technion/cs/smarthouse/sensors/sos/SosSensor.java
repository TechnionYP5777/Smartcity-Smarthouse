package il.ac.technion.cs.smarthouse.sensors.sos;

import java.util.HashMap;
import java.util.Map;

import il.ac.technion.cs.smarthouse.sensors.Sensor;

/** This class represents an SOS button and contains its logic.
 * @author Yarden
 * @since 28.12.16 */
public class SosSensor extends Sensor {
    public SosSensor(final String id, final String commName, final String systemIP, final int systemPort) {
        super(id, commName, systemIP, systemPort);
    }

    public void updateSystem() {
        final Map<String, String> data = new HashMap<>();
        data.put("pressed", true + "");
        super.updateSystem(data);
    }

    @Override public String[] getObservationsNames() {
        return new String[] { "pressed" };
    }

}
