package il.ac.technion.cs.smarthouse.system;

import java.util.function.Consumer;

/**
 * @author Inbal Zukerman
 * @date May 22, 2017
 */

public interface Dispatcher {

    /**
     * DELIMITER is the const string we will use to separate between the
     * different parts of a path
     */
    public static final String DELIMITER = ".";

    /**
     * SEPARATOR is the const string we will use to part the path of the message
     * from the value it updates
     */
    public static final String SEPARATOR = "=";

    /**
     * This methods allows to subscribe to new messages on a certain path.
     * 
     * @param path
     *            The path on which the subscriber would like to listen
     * @param subscriber
     * @return The subscriber's ID in our system (for future use)
     */
    public String subscribe(Consumer<String> subscriber, String... path);

    /**
     * This method will allow a subscriber to unsubscribe to a certain path
     * 
     * @param subscriberId
     *            The ID of the subscriber in our system
     * @param path
     *            The path on which the subscriber would like to stop listen
     */
    public void unsubscribe(String subscriberId, String... path);

    /**
     * This method allows to send a message
     * 
     * @param value
     *            The value to send
     * @param path
     *            The path of the update
     */
    public void sendMessage(InfoType infoType, String value, String... path);

}
