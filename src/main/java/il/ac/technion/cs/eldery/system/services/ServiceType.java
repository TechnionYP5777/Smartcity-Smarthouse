package il.ac.technion.cs.eldery.system.services;

import il.ac.technion.cs.eldery.system.services.alerts_service.AlertsManager;
import il.ac.technion.cs.eldery.system.services.sensors_service.SensorsManager;

public enum ServiceType {
    SENSORS_SERVICE(SensorsManager.class),
    ALERTS_SERVICE(AlertsManager.class)
    ;
    private Class<? extends Service> serviceClass;
    
    private ServiceType(Class<? extends Service> serviceClass) {
        this.serviceClass = serviceClass;
    }
    
    public Class<? extends Service> getServiceClass() {
        return serviceClass;
    }
}
