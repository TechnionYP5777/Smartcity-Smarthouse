package il.ac.technion.cs.eldery.system.userInformation;

import il.ac.technion.cs.eldery.system.EmergencyLevel;

/** @author Inbal Zukerman
 * @since Dec 28, 2016 */
public class Contact {

    private String id;
    private String name;
    private String phoneNumber;
    private EmergencyLevel eLevelToInform;

    public Contact(String id, String name, String phoneNumber, EmergencyLevel eLevelToInform) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.eLevelToInform = eLevelToInform;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public EmergencyLevel getELevelToInform() {
        return eLevelToInform;
    }

    public void seteLevelToInform(EmergencyLevel eLevelToInform) {
        this.eLevelToInform = eLevelToInform;
    }

}
