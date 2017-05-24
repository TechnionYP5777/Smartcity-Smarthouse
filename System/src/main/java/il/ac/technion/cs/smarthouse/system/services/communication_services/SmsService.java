package il.ac.technion.cs.smarthouse.system.services.communication_services;

import il.ac.technion.cs.smarthouse.system.SystemCore;

public class SmsService extends CommunicationService {

    public SmsService(final SystemCore $) {
        super($);
    }

    @SuppressWarnings("static-method")
    public void sendMsg(final String destPhoneNumber, final String msg) {
        Communicate.throughSms(destPhoneNumber, msg);
    }

}
