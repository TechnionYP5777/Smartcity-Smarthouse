package il.ac.technion.cs.smarthouse.system.services.communication_services;

import il.ac.technion.cs.smarthouse.system.SystemCore;

public class PhoneService extends CommunicationService {

    public PhoneService(SystemCore $) {
        super($);
    }

    @SuppressWarnings("static-method")
    public void makeCall(String phoneNumber) {
        Communicate.throughPhone(phoneNumber);
    }
}
