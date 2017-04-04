package il.ac.technion.cs.smarthouse.utils;

import java.util.UUID;

/** @author RON
 * @since 09-12-16 */
public enum UuidGenerator {
    ;
    /** Generates a Universally Unique IDentifier (UUID)
     * @return the new UUID */
    public static UUID generateUniqueID() {
        return UUID.randomUUID();
    }

    public static String GenerateUniqueIDstring() {
        return generateUniqueID() + "";
    }
}
