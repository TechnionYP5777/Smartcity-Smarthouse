package il.ac.technion.cs.smarthouse.system.services.communication_services;

import org.junit.Before;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.system.services.ServiceManager;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;

/**
 * @author RON
 * @since 30-05-2017
 */
public class CommunicationServicesTest {
    private ServiceManager serviceManager;

    @Before
    public void init() {
        serviceManager = new ServiceManager(null);
    }

    @Test
    public void testServices() {
        assert serviceManager.<EmailService>getService(ServiceType.EMAIL_SERVICE).sendMsg("smarthouse5777@gmail.com",
                        "HI") != null;
        assert serviceManager.<PhoneService>getService(ServiceType.PHONE_SERVICE).makeCall("0501234") != null;
        assert serviceManager.<SmsService>getService(ServiceType.SMS_SERVICE).sendMsg("0501234", "HI") != null;
    }
}
