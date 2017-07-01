package il.ac.technion.cs.smarthouse.utils;

import java.util.UUID;

/**
 * A Universally Unique IDentifier (UUID) generator.
 * 
 * @author RON
 * @since 09-12-16
 */
public enum UuidGenerator {
    ;
    /**
     * Generates a UUID
     * 
     * @return the new UUID
     */
    public static UUID generateUniqueID() {
        return UUID.randomUUID();
    }

    /**
     * Generates a UUID as a String
     * 
     * @return String representing the new UUID
     */
    public static String GenerateUniqueIDstring() {
        return generateUniqueID() + "";
    }
}
