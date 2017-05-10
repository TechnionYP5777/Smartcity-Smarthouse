package il.ac.technion.cs.smarthouse.system.services.communication_services;

import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.services.Service;

/**
 * An abstract communication service class
 * @author RON
 * @since 09-05-2017
 */
abstract class CommunicationService extends Service {
    
    public CommunicationService(SystemCore $) {
        super($);
    }   
}
