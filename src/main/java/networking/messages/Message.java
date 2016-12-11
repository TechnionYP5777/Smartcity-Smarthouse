package networking.messages;

/** @author Sharon
 * @since 11.12.16 */
public interface Message {
  /** Converts the contents of this message into JSON format.
   * @return JSON formatted string */
  String toJson();
}
