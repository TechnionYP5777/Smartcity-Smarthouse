package il.ac.technion.cs.eldery.networking.messages;

/** @author Yarden
 * @since 11.12.16 */

public enum MessageType {
  REGISTRATION, UPDATE;
  public static MessageType fromString(String ¢) {
    return "registration".equals(¢) ? REGISTRATION : "update".equals(¢) ? UPDATE : null;
  }
}
