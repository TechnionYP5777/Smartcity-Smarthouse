package il.ac.technion.cs.eldery.sensors;

/** This class represents a sensor that can get instructions and operate
 * accordingly.
 * @author Yarden
 * @since 31.3.17 */
public abstract class InteractiveSensor extends Sensor {
    protected int instructionsPort;

    public InteractiveSensor(final String id, final String commName, final String systemIP, final int systemPort, final int instructionsPort) {
        super(id, commName, systemIP, systemPort);
        this.instructionsPort = instructionsPort;
    }

}
