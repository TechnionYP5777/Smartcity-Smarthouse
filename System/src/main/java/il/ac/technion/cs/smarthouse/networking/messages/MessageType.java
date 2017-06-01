package il.ac.technion.cs.smarthouse.networking.messages;

/** This enum specifies all possible types for a message sent between the system
 * and the sensors.
 * @author Yarden
 * @since 11.12.16 */
public enum MessageType {
    REGISTRATION,
    UPDATE,
    ANSWER;

    public static MessageType fromString(final String ¢) {
        final String $ = ¢.toLowerCase();
        return "registration".equals($) ? REGISTRATION : "update".equals($) ? UPDATE : "answer".equals($) ? MessageType.ANSWER : null;
    }
}
