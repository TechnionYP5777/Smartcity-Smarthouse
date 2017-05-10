package il.ac.technion.cs.smarthouse.networking.messages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/** This class represents a general message that can be sent from a sensor to
 * the system or vice versa.
 * @author Sharon
 * @author Yarden
 * @since 11.12.16 */
public abstract class Message {
    private final MessageType type;
    private static Logger log = LoggerFactory.getLogger(Message.class);

    /** Creates a new message object.
     * @param type type of this message */
    public Message(final MessageType type) {
        this.type = type;
    }

    /** @return type of this message */
    public MessageType getType() {
        return type;
    }

    /** Converts the contents of this message into JSON format.
     * @return JSON formatted string */
    public String toJson() {
        return new Gson().toJson(this);
    }

    /** Sends the message to the specified destination.
     * @param out a PrintWrite object that was created from a socket connected
     *        to the destination
     * @param in a BufferedReader object that was created from a socket
     *        connected to the destination.If a response from the destination is
     *        not requested, <code> null </code> should be sent.
     * @return the response from the destination, if requested. If an error
     *         occurred or if a response was not requested, <code> null </code>
     *         will be returned. */
    public String send(final PrintWriter out, final BufferedReader $) {
        if (out == null)
            return null;

        out.println(toJson());
        if ($ != null)
            try {
                return $.readLine();
            } catch (final IOException e) {
                log.error("I/O error occurred", e);
                return null;
            }
        return null;
    }
}
