package il.ac.technion.cs.eldery.sensors;

import java.util.List;
import java.util.Map;

import il.ac.technion.cs.eldery.networking.messages.AnswerMessage;
import il.ac.technion.cs.eldery.networking.messages.AnswerMessage.Answer;
import il.ac.technion.cs.eldery.networking.messages.MessageFactory;
import il.ac.technion.cs.eldery.networking.messages.RegisterMessage;
import il.ac.technion.cs.eldery.networking.messages.UpdateMessage;

/** @author Sharon
 * @author Yarden
 * @since 7.12.16 */
public abstract class Sensor {
    protected String name;
    protected String id;
    protected List<String> types;
    protected String systemIP;
    protected int systemPort;

    /** Initializes a new sensor given its name and id.
     * @param name name of the sensor
     * @param id id of the sensor
     * @param types types this sensor qualifies for */
    public Sensor(final String name, final String id, final List<String> types, final String systemIP, final int systemPort) {
        this.name = name;
        this.id = id;
        this.types = types;
        this.systemIP = systemIP;
        this.systemPort = systemPort;
    }

    /** Registers the sensor to the system.
     * @return <code>true</code> if registration was successful,
     *         <code>false</code> otherwise */
    public boolean register() {
        String response = (new RegisterMessage(this)).send(systemIP, systemPort, true);
        return response != null && ((AnswerMessage) MessageFactory.create(response)).getAnswer() == Answer.SUCCESS;
    }

    /** Sends an update message to the system with the given observations. The
     * observations are represented as a map from the names of the observations,
     * to their values.
     * @param data observations to send to the system */
    public void updateSystem(Map<String, String> data) {
        (new UpdateMessage(this, data)).send(systemIP, systemPort, false);
    }

    /** Returns the names of the parameters that will be sent to the system.
     * These names will be used to pass data to system as a dictionary of (type,
     * value) tuples.
     * @return array of names of the data this sensor observers */
    public abstract String[] getObservationsNames();

    /** @return name of the sensor */
    public String getName() {
        return name;
    }

    /** @return list of types this sensor qualifies for */
    public List<String> getTypes() {
        return this.types;
    }

    /** @return id of the sensor */
    public String getId() {
        return id;
    }

    /** Sets a new name for the sensor.
     * @param name new name of the sensor */
    public void setName(final String name) {
        this.name = name;
    }
}
