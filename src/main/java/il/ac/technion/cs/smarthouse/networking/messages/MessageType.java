package il.ac.technion.cs.smarthouse.networking.messages;

/** @author Yarden
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
