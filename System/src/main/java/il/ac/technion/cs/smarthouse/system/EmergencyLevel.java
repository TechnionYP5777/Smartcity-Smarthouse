package il.ac.technion.cs.smarthouse.system;

import java.util.ArrayList;
import java.util.List;

/** The level of emergency, defined by the level of expertise needed to take
 * care of it. Includes the following options: {@link #NOTIFY_ELDERLY},
 * {@link #SMS_EMERGENCY_CONTACT}, {@link #CALL_EMERGENCY_CONTACT},
 * {@link #CONTACT_POLICE}, {@link #CONTACT_HOSPITAL},
 * {@link #CONTACT_FIRE_FIGHTERS} */
public enum EmergencyLevel {
    /** Low level of emergency, requires a reminder to the elderly */
    NOTIFY_ELDERLY,
    /** low-Medium level of emergency, requires e-mailing a previously defined
     * contact */
    EMAIL_EMERGENCY_CONTACT,
    /** Medium level of emergency, requires texting a previously defined
     * contact */
    SMS_EMERGENCY_CONTACT,
    /** Medium-high level of emergency, requires calling a previously defined
     * contact */
    CALL_EMERGENCY_CONTACT,
    /** High level of emergency, requires police assistance */
    CONTACT_POLICE,
    /** High level of emergency, requires help from medical personnel */
    CONTACT_HOSPITAL,
    /** High level of emergency, requires fire fighters assistance */
    CONTACT_FIRE_FIGHTERS;

    public static EmergencyLevel fromString(final String ¢) {

        switch (¢) {
            case "NOTIFY_ELDERLY":
                return NOTIFY_ELDERLY;
            case "EMAIL_EMERGENCY_CONTACT":
                return EMAIL_EMERGENCY_CONTACT;
            case "SMS_EMERGENCY_CONTACT":
                return SMS_EMERGENCY_CONTACT;
            case "CALL_EMERGENCY_CONTACT":
                return CALL_EMERGENCY_CONTACT;
            case "CONTACT_POLICE":
                return CONTACT_POLICE;
            case "CONTACT_HOSPITAL":
                return CONTACT_HOSPITAL;
            default:
                return CONTACT_FIRE_FIGHTERS;
        }
    }

    public static List<String> stringValues() {
        final ArrayList<String> $ = new ArrayList<>();
        for (final EmergencyLevel elvl : EmergencyLevel.values())
            $.add(elvl.name());

        return $;
    }

}
