package il.ac.technion.cs.smarthouse.system.services.alerts_service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.services.ServiceManager;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
import il.ac.technion.cs.smarthouse.system.user_information.Contact;

/**
 * @author RON
 * @since 30-05-2017
 * 
 *        [[SuppressWarningsSpartan]]
 */
public class AlertsServiceTest {
    private ServiceManager serviceManager_noUser;
    private ServiceManager serviceManager_withUser;

    @Before
    public void init() {
        serviceManager_noUser = new ServiceManager(new SystemCore());

        SystemCore s = new SystemCore();
        serviceManager_withUser = new ServiceManager(s);
        s.initializeUser("Bob", "123", "050", "HERE");
        s.getUser().addContact(new Contact("111", "Alice", "999", "a@b.com"), EmergencyLevel.SMS_EMERGENCY_CONTACT);
        s.getUser().addContact(new Contact("111", "Alice", "999", "a@b.com"), EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
        s.getUser().addContact(new Contact("111", "Alice", "999", "a@b.com"), EmergencyLevel.CALL_EMERGENCY_CONTACT);
    }

    @Test
    public void withoutUserTest() {
        AlertsManager a;
        Assert.assertNotNull(a = serviceManager_noUser.<AlertsManager>getService(ServiceType.ALERTS_SERVICE));
        a.sendAlert("", "I'm dead", EmergencyLevel.CALL_EMERGENCY_CONTACT);
        a.sendAlert("", "I'm dead", EmergencyLevel.SMS_EMERGENCY_CONTACT);
        a.sendAlert("", "I'm dead", EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
    }

    @Test
    public void withUserTest() {
        AlertsManager a;
        Assert.assertNotNull(a = serviceManager_withUser.<AlertsManager>getService(ServiceType.ALERTS_SERVICE));
        a.sendAlert("", "I'm dead", EmergencyLevel.CALL_EMERGENCY_CONTACT);
        a.sendAlert("", "I'm dead", EmergencyLevel.SMS_EMERGENCY_CONTACT);
        a.sendAlert("", "I'm dead", EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
    }
}
