package il.ac.technion.cs.smarthouse.system.services;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.SystemCore;

/**
 * A systemCore element that instantiates all of the services in the system.
 * <p>
 * The system's services that will be instantiated are defined in
 * {@link ServiceType}
 * 
 * @author RON
 * @since 02-04-2017
 */
public final class ServiceManager {
    private static Logger log = LoggerFactory.getLogger(ServiceManager.class);

    private final Map<ServiceType, Service> services = new HashMap<>();

    public ServiceManager(final SystemCore systemCore) {
        for (final ServiceType s : ServiceType.values())
            try {
                services.put(s, s.getServiceClass().getDeclaredConstructor(SystemCore.class).newInstance(systemCore));
                log.info("\n\tService " + s + " started");
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                log.error("\n\tService " + s + " can't start ", e);
            }
    }

    @SuppressWarnings("unchecked")
    public <T extends Service> T getService(final ServiceType t) {
        return (T) services.get(t);
    }
}
