package il.ac.technion.cs.smarthouse.system.services;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import il.ac.technion.cs.smarthouse.system.SystemCore;

public class ServiceManager {
    static Logger log = Logger.getLogger(ServiceManager.class);
    
    private Map<ServiceType, Service> services = new HashMap<>();
    
    public ServiceManager(SystemCore systemCore) {
        for (ServiceType s : ServiceType.values())
            try {
                services.put(s, s.getServiceClass().getDeclaredConstructor(SystemCore.class).newInstance(systemCore));
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                    | SecurityException e) {
                log.error(e.getMessage());
            }
    }
    
    public Service getService(ServiceType t) {
        return services.get(t);
    }
}
