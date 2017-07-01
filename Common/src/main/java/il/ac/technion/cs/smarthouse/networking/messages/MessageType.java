package il.ac.technion.cs.smarthouse.networking.messages;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This enum specifies all possible types for a message sent between the system
 * and the sensors.
 * 
 * @author Elia Traore
 * @author Yarden
 * @since 11.12.16
 */
public enum MessageType {
    REGISTRATION("registration"),
    UPDATE("update"),
    SUCCESS_ANSWER("success_answer"),
    FAILURE_ANSWER("failure_answer");

    private final String type;

    private MessageType(final String type) {
        this.type = type;
    }

    /**
     * Retrieves the type of a message.
     * 
     * @param from
     *            A string representing the type of a message
     * @return The matching MessageType
     */
    public static MessageType fromString(final String from) {
        final String fromLower = from.toLowerCase();
        final List<MessageType> $ = Arrays.asList(MessageType.values()).stream().filter(mt -> mt.type.equals(fromLower))
                        .collect(Collectors.toList());
        $.add(null);
        return $.get(0);
    }
}
