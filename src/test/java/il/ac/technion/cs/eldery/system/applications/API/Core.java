/**
 * 
 */
package il.ac.technion.cs.eldery.system.applications.API;

import il.ac.technion.cs.eldery.system.SystemCore;

/**
 * @author Elia Traore
 * @since Apr 1, 2017
 */
public class Core extends SystemCore{
    Thread sensorsThread = new Thread(sensorsHandler);
    
    @SuppressWarnings("unused")
    public Core() {
        sensorsThread.start();
    }

    public Object getHandler(Handler h){
        switch(h){
            case APPS:
                return applicationsHandler;
            case SENSORS:
                return sensorsHandler;
//            case DB:
//                return databaseHandler;
            default:
                    return null;
        }
    } 
    
    public Thread getSensorHandlerThread(){
        return sensorsThread;
    }
}

