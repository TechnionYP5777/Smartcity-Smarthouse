package il.ac.technion.cs.eldery.sensors;

/** @author Yarden
 * @since 10.12.16 */
public class StoveSensor extends Sensor {
  public StoveSensor(final String name, final String id) {
    super(name, id);
  }

  @Override protected String[] getObservationsNames() {
    String[] $ = new String[2];
    $[0] = "on / off";
    $[1] = "temperature";
    return $;
  }
}
