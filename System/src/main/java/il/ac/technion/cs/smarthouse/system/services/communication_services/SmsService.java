package il.ac.technion.cs.smarthouse.system.services.communication_services;

import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.utils.Communicate;

public class SmsService extends CommunicationService {

    public SmsService(final SystemCore $) {
        super($);
    }

    @SuppressWarnings("static-method")
    public String sendMsg(final String destPhoneNumber, final String msg) {
        return Communicate.throughSms(destPhoneNumber, msg);
    }

}
