package il.ac.technion.cs.eldery.sensors;

/** @author Yarden
 * @since 10.12.16 */
public class StoveSensor extends Sensor {
  public StoveSensor(final String name, final String id) {
    super(name, id);
  }

  @Override protected String[] getObservationsNames() {
    return new String[] { "on / off", "temperature" };
  }
}
