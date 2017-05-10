package il.ac.technion.cs.smarthouse.system.services;

import il.ac.technion.cs.smarthouse.system.SystemCore;

/** An abstract class for all services in the system.
 * <p>
 * Services are created and stored by {@link ServiceManager}
 * @author RON
 * @since 02-04-2017 */
public abstract class Service {

    protected SystemCore systemCore;

    public Service(final SystemCore $) {
        systemCore = $;
    }
    
    /** Get another service from the system
     * @param $
     * @return */
    protected Service getAnotherService(ServiceType $) {
        return systemCore.serviceManager.getService($);
    }
}
