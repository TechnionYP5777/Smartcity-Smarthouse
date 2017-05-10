package il.ac.technion.cs.smarthouse.sensors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.networking.messages.AnswerMessage;
import il.ac.technion.cs.smarthouse.networking.messages.AnswerMessage.Answer;
import il.ac.technion.cs.smarthouse.networking.messages.MessageFactory;
import il.ac.technion.cs.smarthouse.networking.messages.RegisterMessage;
import il.ac.technion.cs.smarthouse.networking.messages.UpdateMessage;

/** Represents a physical component that can send information.
 * @author Sharon
 * @author Yarden
 * @since 7.12.16 */
public abstract class Sensor {
    private static Logger log = LoggerFactory.getLogger(Sensor.class);

    /** Defines the maximal amount of update messages that can be sent by this
     * sensor each second. */
    public static final int MAX_MESSAGES_PER_SECOND = 10;
    private final List<Long> lastMessagesMillis = new ArrayList<>();

    protected String id;
    protected String commName;

    protected String systemIP;
    protected int systemPort;

    protected Socket socket;
    protected PrintWriter out;
    protected BufferedReader in;

    protected SensorType sType;

    /** Initializes a new sensor given its name and id.
     * @param id id of the sensor
     * @param commName sensor's commercial name
     * @param types types this sensor qualifies for
     * @param systemIP IP address of the system
     * @param systemPort port on which the system listens to incoming
     *        messages */
    public Sensor(final String id, final String commName, final String systemIP, final int systemPort) {
        this.id = id;
        this.commName = commName;
        this.systemIP = systemIP;
        this.systemPort = systemPort;
        this.sType = SensorType.NON_INTERACTIVE;
    }

    /** Registers the sensor its TCP connection with the system. This method
     * must be called before any calls to the updateSystem method.
     * @return <code>true</code> if registration was successful,
     *         <code>false</code> otherwise */
    public boolean register() {
        try {
            socket = new Socket(InetAddress.getByName(systemIP), systemPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (final IOException e) {
            log.error("I/O error occurred when the sensor's socket was created", e);
        }
        final String $ = new RegisterMessage(id, commName, sType).send(out, in);
        return $ != null && ((AnswerMessage) MessageFactory.create($)).getAnswer() == Answer.SUCCESS;
    }

    /** Sends an update message to the system with the given observations. The
     * observations are represented as a map from the names of the observations,
     * to their values.
     * @param data observations to send to the system */
    public void updateSystem(final Map<String, String> data) {
        final long currMillis = System.currentTimeMillis();
        for (int ¢ = lastMessagesMillis.size() - 1; ¢ >= 0; --¢)
            if (currMillis - lastMessagesMillis.get(¢) > 1000)
                lastMessagesMillis.remove(¢);

        if (lastMessagesMillis.size() >= 10)
            return;

        lastMessagesMillis.add(currMillis);
        new UpdateMessage(id, data).send(out, null);
    }

    /** Returns the names of the parameters that will be sent to the system.
     * These names will be used to pass data to system as a dictionary of (type,
     * value) tuples.
     * @return array of names of the data this sensor observers */
    public abstract String[] getObservationsNames();

    /** @return id of the sensor */
    public String getId() {
        return id;
    }

    /** @return sensor's commercial name */
    public String getCommName() {
        return commName;
    }

    /** @return sensor's type */
    public SensorType getType() {
        return sType;
    }
}
