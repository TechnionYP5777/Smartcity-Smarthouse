package il.ac.technion.cs.eldery.system.userInformation;

import org.junit.Test;

import il.ac.technion.cs.eldery.system.EmergencyLevel;

/** @author Inbal Zukerman
 * @since Dec 28, 2016 */
public class ContactTest {

    private final Contact contactA = new Contact("123", "Alon", "0508080123", EmergencyLevel.SMS_EMERGENCY_CONTACT);

    @Test public void initializationTest() {

        assert contactA.getId() == "123";
        assert contactA.getName() == "Alon";
    }

    @Test public void phoneNumberTest() {
        assert contactA.getPhoneNumber() == "0508080123";
        contactA.setPhoneNumber("026798080");
        assert contactA.getPhoneNumber() == "026798080";
    }

    @Test public void emergencyLevelTest() {
        assert contactA.getELevelToInform() == EmergencyLevel.SMS_EMERGENCY_CONTACT;
        contactA.seteLevelToInform(EmergencyLevel.CONTACT_HOSPITAL);
        assert contactA.getELevelToInform() == EmergencyLevel.CONTACT_HOSPITAL;
    }
}
