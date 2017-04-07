package il.ac.technion.cs.smarthouse.system.services;

import il.ac.technion.cs.smarthouse.system.services.alerts_service.AlertsManager;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorsManager;

/** An enum of the classes of the available services. The services must extend
 * {@link Service}
 * @author RON
 * @since 02-04-2017 */
public enum ServiceType {
    SENSORS_SERVICE(SensorsManager.class),
    ALERTS_SERVICE(AlertsManager.class);
    private Class<? extends Service> serviceClass;

    private ServiceType(Class<? extends Service> serviceClass) {
        this.serviceClass = serviceClass;
    }

    public Class<? extends Service> getServiceClass() {
        return serviceClass;
    }
}
