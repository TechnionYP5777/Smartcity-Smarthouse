package il.ac.technion.cs.eldery.system.services.alerts_service;

import il.ac.technion.cs.eldery.system.EmergencyLevel;
import il.ac.technion.cs.eldery.system.SystemCore;
import il.ac.technion.cs.eldery.system.services.Service;

public class AlertsManager extends Service {
    
    public AlertsManager(SystemCore $) {
        super($);
    }

    /** Report an abnormality in the expected schedule. The system will contact
     * the needed personal, according to the urgency level
     * @param message Specify the abnormality, will be presented to the
     *        contacted personal
     * @param eLevel The level of personnel needed in the situation */
    public final void sendAlert(final String message, final EmergencyLevel eLevel) {
        systemCore.alert(message, eLevel);
    }
}
