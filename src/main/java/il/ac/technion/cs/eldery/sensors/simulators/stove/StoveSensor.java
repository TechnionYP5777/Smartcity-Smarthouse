package il.ac.technion.cs.eldery.sensors.simulators.stove;

import java.util.ArrayList;
import java.util.List;

import il.ac.technion.cs.eldery.sensors.Sensor;

/** @author Yarden
 * @author Sharon
 * @since 10.12.16 */
public class StoveSensor extends Sensor {
    private static final List<String> TYPES = new ArrayList<>();
    static {
        TYPES.add("Stove");
    }

    public StoveSensor(final String name, final String id, final String systemIP, final int systemPort) {
        super(name, id, TYPES, systemIP, systemPort);
    }

    @Override public String[] getObservationsNames() {
        return new String[] { "on / off", "temperature" };
    }
}
