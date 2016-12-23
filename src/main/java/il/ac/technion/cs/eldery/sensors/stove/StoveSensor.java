package il.ac.technion.cs.eldery.sensors.stove;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import il.ac.technion.cs.eldery.sensors.Sensor;

/** @author Yarden
 * @author Sharon
 * @since 10.12.16 */
public class StoveSensor extends Sensor {
    private static final List<String> TYPES = new ArrayList<>();

    static {
        TYPES.add("Stove");
    }

    public StoveSensor(final String name, final String id, final String commName, final String systemIP, final int systemPort) {
        super(name, id, commName, TYPES, systemIP, systemPort);
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
