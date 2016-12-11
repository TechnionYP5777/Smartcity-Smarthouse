package il.ac.technion.cs.eldery.sensors;

import java.util.List;
import java.util.Map;

/** @author Sharon
 * @author Yarden
 * @since 7.12.16 */
// TODO Add some interesting functionality
public abstract class Sensor {
  protected String name;
  protected String id;
  protected List<String> types;

  /** Initializes a new sensor given its name and id.
   * @param name name of the sensor
   * @param id id of the sensor
   * @param types types this sensor qualifies for */
  public Sensor(final String name, final String id, final List<String> types) {
    this.name = name;
    this.id = id;
    this.types = types;
  }

  /** Registers the sensor to the system.
   * @return <code>true</code> if registration was successful,
   *         <code>false</code> otherwise */
  @SuppressWarnings("static-method") public boolean register() {
    // TODO: Sharon/Yarden
    return true;
  }

  /** Sends an update message to the system with the given observations. The
   * observations are represented as a map from the names of the observations,
   * to their values.
   * @param __ observations to send to the system
   * @return <code>true</code> if message was sent successfully,
   *         <code>false</code> otherwise */
  @SuppressWarnings("static-method") public boolean updateSystem(Map<Object, Object> __) {
    // TODO: Sharon/Yarden
    return true;
  }

  /** Returns the names of the parameters that will be sent to the system. These
   * names will be used to pass data to system as a dictionary of (type, value)
   * tuples.
   * @return array of names of the data this sensor observers */
  protected abstract String[] getObservationsNames();

  /** @return name of the sensor */
  public String getName() {
    return name;
  }

  /** Sets a new name for the sensor.
   * @param name new name of the sensor */
  public void setName(final String name) {
    this.name = name;
  }

  /** @return list of types this sensor qualifies for */
  public List<String> getTypes() {
    return this.types;
  }

  /** @return id of the sensor */
  public String getId() {
    return id;
  }
}
