package il.ac.technion.cs.smarthouse.system.services.alerts_service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The level of emergency, defined by the level of expertise needed to take care
 * of it. Includes the following options: {@link #NOTIFY_ELDERLY},
 * {@link #SMS_EMERGENCY_CONTACT}, {@link #CALL_EMERGENCY_CONTACT},
 * {@link #CONTACT_POLICE}, {@link #CONTACT_HOSPITAL},
 * {@link #CONTACT_FIRE_FIGHTERS}
 */
public enum EmergencyLevel {
    /** Low level of emergency, requires a reminder to the elderly */
    NOTIFY_ELDERLY,
    /**
     * low-Medium level of emergency, requires e-mailing a previously defined
     * contact
     */
    EMAIL_EMERGENCY_CONTACT,
    /**
     * Medium level of emergency, requires texting a previously defined contact
     */
    SMS_EMERGENCY_CONTACT,
    /**
     * Medium-high level of emergency, requires calling a previously defined
     * contact
     */
    CALL_EMERGENCY_CONTACT,
    /** High level of emergency, requires police assistance */
    CONTACT_POLICE,
    /** High level of emergency, requires help from medical personnel */
    CONTACT_HOSPITAL,
    /** High level of emergency, requires fire fighters assistance */
    CONTACT_FIRE_FIGHTERS;

    public static EmergencyLevel fromString(final String $) {
        return EmergencyLevel.valueOf($);
    }

    public static List<String> stringValues() {
        return Stream.of(EmergencyLevel.values()).map(v->v.name()).collect(Collectors.toList());
    }
    
    public String toPretty() {
        return name().toLowerCase().replace('_', ' ');
    }

}
