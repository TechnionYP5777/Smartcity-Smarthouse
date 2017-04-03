package il.ac.technion.cs.smarthouse.system;

import org.junit.Assert;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.system.EmergencyLevel;

/** @author Inbal Zukerman
 * @since Jan 6, 2017 */

public class EmergencyLevelTest {
    private EmergencyLevel emergencyLevel;

    @Test public void fromStringTest() {
        emergencyLevel = EmergencyLevel.fromString("NOTIFY_ELDERLY");
        Assert.assertEquals(EmergencyLevel.NOTIFY_ELDERLY, emergencyLevel);

        emergencyLevel = EmergencyLevel.fromString("EMAIL_EMERGENCY_CONTACT");
        Assert.assertEquals(EmergencyLevel.EMAIL_EMERGENCY_CONTACT, emergencyLevel);

        emergencyLevel = EmergencyLevel.fromString("SMS_EMERGENCY_CONTACT");
        Assert.assertEquals(EmergencyLevel.SMS_EMERGENCY_CONTACT, emergencyLevel);

        emergencyLevel = EmergencyLevel.fromString("CALL_EMERGENCY_CONTACT");
        Assert.assertEquals(EmergencyLevel.CALL_EMERGENCY_CONTACT, emergencyLevel);

        emergencyLevel = EmergencyLevel.fromString("CONTACT_POLICE");
        Assert.assertEquals(EmergencyLevel.CONTACT_POLICE, emergencyLevel);

        emergencyLevel = EmergencyLevel.fromString("CONTACT_HOSPITAL");
        Assert.assertEquals(EmergencyLevel.CONTACT_HOSPITAL, emergencyLevel);

        emergencyLevel = EmergencyLevel.fromString("CONTACT_FIRE_FIGHTERS");
        Assert.assertEquals(EmergencyLevel.CONTACT_FIRE_FIGHTERS, emergencyLevel);

    }

    // JUnit tests methods should not be static!
    @Test @SuppressWarnings("static-method") public void stringValuesTest() {
        for (final EmergencyLevel elevel : EmergencyLevel.values())
            assert EmergencyLevel.stringValues().contains(elevel.name());
    }
}
