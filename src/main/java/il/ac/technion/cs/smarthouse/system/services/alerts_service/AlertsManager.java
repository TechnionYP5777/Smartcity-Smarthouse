package il.ac.technion.cs.smarthouse.system.services.alerts_service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.EmergencyLevel;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.services.Service;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
import il.ac.technion.cs.smarthouse.system.services.communication_services.EmailService;
import il.ac.technion.cs.smarthouse.system.services.communication_services.PhoneService;
import il.ac.technion.cs.smarthouse.system.services.communication_services.SmsService;
import il.ac.technion.cs.smarthouse.system.user_information.Contact;
import il.ac.technion.cs.smarthouse.system.user_information.UserInformation;

public class AlertsManager extends Service {
    private static Logger log = LoggerFactory.getLogger(AlertsManager.class);

    public AlertsManager(SystemCore $) {
        super($);
    }

    /** Report an abnormality in the expected schedule. The system will contact
     * the needed personal, according to the urgency level
     * @param message Specify the abnormality, will be presented to the
     *        contacted personal
     * @param eLevel The level of personnel needed in the situation */
    public final void sendAlert(final String message, final EmergencyLevel eLevel) {
        UserInformation user = systemCore.getUser();

        if (user == null) {
            log.debug("systemCore.getUser() returned a null");
            return;
        }
        
        PhoneService ps = (PhoneService) getAnotherService(ServiceType.PHONE_SERVICE);
        SmsService ss = (SmsService) getAnotherService(ServiceType.SMS_SERVICE);
        EmailService es = (EmailService) getAnotherService(ServiceType.EMAIL_SERVICE);

        final List<Contact> $ = user.getContacts(eLevel);
        switch (eLevel) {
            case SMS_EMERGENCY_CONTACT:
                $.stream().forEach(c -> ss.sendMsg(c.getPhoneNumber(), message));
                break;
            case CALL_EMERGENCY_CONTACT:
            case CONTACT_HOSPITAL:
            case CONTACT_POLICE:
            case CONTACT_FIRE_FIGHTERS:
                $.stream().forEach(c -> ps.makeCall(c.getPhoneNumber()));
                break;
            case EMAIL_EMERGENCY_CONTACT:
                $.stream().forEach(c -> es.sendMsg(c.getEmailAddress(), message));
                break;
            default:
                log.warn(eLevel + " is not handled");
        }
    }
}
