package il.ac.technion.cs.smarthouse.system.services;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import il.ac.technion.cs.smarthouse.system.SystemCore;

/** A systemCore element that instantiates all of the services in the system.
 * <p>
 * The system's services that will be instantiated are defined in
 * {@link ServiceType}
 * @author RON
 * @since 02-04-2017 */
public final class ServiceManager {
    private static Logger log = LoggerFactory.getLogger(ServiceManager.class);

    private Map<ServiceType, Service> services = new HashMap<>();

    public ServiceManager(SystemCore systemCore) {
        for (ServiceType s : ServiceType.values())
            try {
                services.put(s, s.getServiceClass().getDeclaredConstructor(SystemCore.class).newInstance(systemCore));
                log.info("Service " + s + " started");
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                    | SecurityException e) {
                log.error("Service " + s + " can't start ", e);
            }
    }

    public Service getService(ServiceType t) {
        return services.get(t);
    }
}
