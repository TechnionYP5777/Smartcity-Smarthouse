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
    protected String id;
    protected String commName;
    protected List<String> types;
    protected String systemIP;
    protected int systemPort;

    /** Initializes a new sensor given its name and id.
     * @param id id of the sensor
     * @param commName sensor's commercial name
     * @param types types this sensor qualifies for
     * @param systemIP IP address of the system
     * @param systemPort port on which the system listens to incoming
     *        messages */
    public Sensor(final String id, final String commName, final List<String> types, final String systemIP, final int systemPort) {
        this.id = id;
        this.commName = commName;
        this.types = types;
        this.systemIP = systemIP;
        this.systemPort = systemPort;
    }

    /** Registers the sensor to the system.
     * @return <code>true</code> if registration was successful,
     *         <code>false</code> otherwise */
    public boolean register() {
        final String $ = new RegisterMessage(id, commName).send(systemIP, systemPort);
        return $ != null && ((AnswerMessage) MessageFactory.create($)).getAnswer() == Answer.SUCCESS;
    }

    /** Sends an update message to the system with the given observations. The
     * observations are represented as a map from the names of the observations,
     * to their values.
     * @param data observations to send to the system */
    public void updateSystem(final Map<String, String> data) {
        new UpdateMessage(id, data).send(systemIP, systemPort);
    }

    /** Returns the names of the parameters that will be sent to the system.
     * These names will be used to pass data to system as a dictionary of (type,
     * value) tuples.
     * @return array of names of the data this sensor observers */
    public abstract String[] getObservationsNames();

    /** @return list of types this sensor qualifies for */
    public List<String> getTypes() {
        return types;
    }

    /** @return id of the sensor */
    public String getId() {
        return id;
    }

    /** @return sensor's commercial name */
    public String getCommName() {
        return commName;
    }
}
