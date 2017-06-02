package il.ac.technion.cs.smarthouse.networking.messages;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** This enum specifies all possible types for a message sent between the system
 * and the sensors.
 * @author Elia Traore
 * @author Yarden
 * @since 11.12.16 */
public enum MessageType {
    REGISTRATION("registration"),
    UPDATE("update"),
    SUCCESS_ANSWER("success_answer"),
    FAILURE_ANSWER("failure_answer");
    
    final private String type;
    private MessageType(String type){
        this.type = type;
    }
    
    public static MessageType fromString(final String from){
        final String fromLower = from.toLowerCase();
        List<MessageType> $ = Arrays.asList(MessageType.values())
                                .stream()
                                .filter(mt -> mt.type.equals(fromLower))
                                .collect(Collectors.toList());
        $.add(null);
        return $.get(0);
    }
}
