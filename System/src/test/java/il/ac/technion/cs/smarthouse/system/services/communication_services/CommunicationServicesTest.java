package il.ac.technion.cs.smarthouse.system.services.communication_services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.system.services.ServiceManager;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;

public class CommunicationServicesTest {
    private ServiceManager serviceManager;
    
    @Before
    public void init() {
        serviceManager = new ServiceManager(null);
    }
    
    @Test
    public void testServices() {
        Assert.assertNotNull(serviceManager.<EmailService>getService(ServiceType.EMAIL_SERVICE).sendMsg("smarthouse5777@gmail.com", "HI"));
        Assert.assertNotNull(serviceManager.<PhoneService>getService(ServiceType.PHONE_SERVICE).makeCall("0501234"));
        Assert.assertNotNull(serviceManager.<SmsService>getService(ServiceType.SMS_SERVICE).sendMsg("0501234", "HI"));
    }
}
