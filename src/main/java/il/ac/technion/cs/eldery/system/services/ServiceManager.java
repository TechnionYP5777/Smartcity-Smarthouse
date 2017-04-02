package il.ac.technion.cs.eldery.system.services;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import il.ac.technion.cs.eldery.system.SystemCore;

public class ServiceManager {
    Logger log = Logger.getLogger(getClass());
    
    private Map<ServiceType, Service> services = new HashMap<>();
    
    public ServiceManager(SystemCore systemCore) {
        for (ServiceType s : ServiceType.values()) {
            try {
                Service newService = s.getServiceClass().getDeclaredConstructor(SystemCore.class).newInstance(systemCore);
                services.put(s, newService);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                log.error(e.getMessage());
            }
        }
    }
    
    public Service getService(ServiceType s) {
        return services.get(s);
    }
}
