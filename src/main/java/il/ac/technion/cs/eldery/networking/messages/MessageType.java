package il.ac.technion.cs.eldery.networking.messages;

/** @author Yarden
 * @since 11.12.16 */
public enum MessageType {
    REGISTRATION,
    UPDATE,
    ANSWER;

    public static MessageType fromString(String ¢) {
        String $ = ¢.toLowerCase();
        return "registration".equals($) ? REGISTRATION : "update".equals($) ? UPDATE : "answer".equals($) ? MessageType.ANSWER : null;
    }
}
