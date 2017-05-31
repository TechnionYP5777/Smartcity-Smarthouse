package il.ac.technion.cs.smarthouse.system.services;

import il.ac.technion.cs.smarthouse.system.SystemCore;

/**
 * @author Elia Traore
 * @since Apr 1, 2017
 */
public class Core extends SystemCore {
    Thread sensorsThread = new Thread(sensorsHandler);

    public Core() {
        sensorsThread.start();
    }

    public Object getHandler(final Handler h) {
        switch (h) {
            case APPS:
                return getSystemApplicationsHandler();
            case SENSORS:
                return sensorsHandler;
            case SERVICES:
                return serviceManager;
            // case DB:
            // return databaseHandler;
            default:
                return null;
        }
    }

    public Thread getSensorHandlerThread() {
        return sensorsThread;
    }
}
