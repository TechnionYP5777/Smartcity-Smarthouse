package il.ac.technion.cs.smarthouse.networking.messages;

/**
 * This enum specifies all possible types for a message sent between the system
 * and the sensors.
 * 
 * @author Yarden
 * @author Inbal Zukerman
 * @since 11.12.16
 */
public enum MessageType {
    REGISTRATION("registration"),
    UPDATE("update"),
    ANSWER("answer"),
    SUCCESS("success"),
    FAILURE("failure");

    private final String strMessageType;
    
    private MessageType(String strMessageType){
        this.strMessageType = strMessageType;
    }
    
    @Override public String toString(){
        return this.strMessageType;
    }
}
