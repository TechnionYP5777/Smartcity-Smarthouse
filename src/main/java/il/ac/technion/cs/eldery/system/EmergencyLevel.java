package il.ac.technion.cs.eldery.system;


/** The level of emergency, defined by the level of expertise needed to take care of it. Includes the following options:
 * {@link #NOTIFY_ELDERLY}, {@link #SMS_EMERGENCY_CONTACT}, {@link #CALL_EMERGENCY_CONTACT}, {@link #CONTACT_POLICE},
 * {@link #CONTACT_HOSPITAL}, {@link #CONTACT_FIRE_FIGHTERS}
 * */
public enum EmergencyLevel {
    NOTIFY_ELDERLY,           /**Low level of emergency, requires a reminder to the elderly*/
    SMS_EMERGENCY_CONTACT,    /**Medium level of emergency, requires texting a previously defined contact*/
    CALL_EMERGENCY_CONTACT,   /**Medium-high level of emergency, requires calling a previously defined contact*/
    CONTACT_POLICE,           /**High level of emergency, requires police assistance*/ 
    CONTACT_HOSPITAL,         /**High level of emergency, requires help from medical personnel*/
    CONTACT_FIRE_FIGHTERS     /**High level of emergency, requires fire fighters assistance*/
}
