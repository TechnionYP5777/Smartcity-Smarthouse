package il.ac.technion.cs.eldery.sensors.simulators.stove;

import java.util.ArrayList;
import java.util.List;

import il.ac.technion.cs.eldery.sensors.Sensor;

/** @author Yarden
 * @since 10.12.16 */
public class StoveSensor extends Sensor {
  private static final List<String> TYPES = new ArrayList<>();
  static {
    TYPES.add("Stove");
  }

  public StoveSensor(final String name, final String id) {
    super(name, id, TYPES);
  }

  @Override public String[] getObservationsNames() {
    return new String[] { "on / off", "temperature" };
  }
}
