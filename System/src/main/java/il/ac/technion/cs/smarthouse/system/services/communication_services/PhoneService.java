package il.ac.technion.cs.smarthouse.system.services.communication_services;

import il.ac.technion.cs.smarthouse.system.SystemCore;

public class PhoneService extends CommunicationService {

    public PhoneService(final SystemCore $) {
        super($);
    }

    @SuppressWarnings("static-method")
    public String makeCall(final String phoneNumber) {
        return Communicate.throughPhone(phoneNumber);
    }
}
