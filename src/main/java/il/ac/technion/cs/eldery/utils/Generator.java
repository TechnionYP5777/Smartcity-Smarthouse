package il.ac.technion.cs.eldery.utils;

import java.util.*;

/** @author RON
 * @since 09-12-16 */
public enum Generator {
    ;
    /** Generates a Universally Unique IDentifier (UUID)
     * @return the new UUID */
    public static UUID generateUniqueID() {
        return UUID.randomUUID();
    }
    
    public static String GenerateUniqueIDstring(){
        return generateUniqueID() + "";
    }
}
