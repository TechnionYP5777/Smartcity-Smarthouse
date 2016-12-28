package il.ac.technion.cs.eldery.system.userInformation;

import il.ac.technion.cs.eldery.system.EmergencyLevel;

/** @author Inbal Zukerman
 * @since Dec 28, 2016 */
public class Contact {

    private final String id;
    private final String name;
    private String phoneNumber;
    private EmergencyLevel eLevelToInform;

    public Contact(final String id, final String name, final String phoneNumber, final EmergencyLevel eLevelToInform) {
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

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public EmergencyLevel getELevelToInform() {
        return eLevelToInform;
    }

    public void seteLevelToInform(final EmergencyLevel eLevelToInform) {
        this.eLevelToInform = eLevelToInform;
    }

}
