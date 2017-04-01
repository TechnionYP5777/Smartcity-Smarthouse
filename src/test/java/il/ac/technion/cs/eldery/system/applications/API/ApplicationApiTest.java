/**
 * 
 */
package il.ac.technion.cs.eldery.system.applications.API;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import il.ac.technion.cs.eldery.sensors.InteractiveSensor;
import il.ac.technion.cs.eldery.system.DatabaseHandler;
import il.ac.technion.cs.eldery.system.SystemCore;
import il.ac.technion.cs.eldery.system.applications.ApplicationManager;
import il.ac.technion.cs.eldery.system.applications.ApplicationsHandler;
import il.ac.technion.cs.eldery.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.eldery.system.applications.api.exceptions.OnLoadException;
import il.ac.technion.cs.eldery.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.eldery.system.exceptions.AppInstallerException;

/**
 * @author Elia Traore
 * @since Apr 1, 2017
 */
public class ApplicationApiTest {
    private enum Handler {APPS, SENSORS, DB};
    private class Core extends SystemCore{
        public Core() {
            // TODO: Auto-generated constructor stub
        }

        public Object getHandler(Handler h){
            switch(h){
                case APPS:
                    return applicationsHandler;
                case SENSORS:
                    return sensorsHandler;
//                case DB:
//                    return databaseHandler;
                default:
                        return null;
            }
        } 
    }
    
    // general tests data
    @Rule public TestName testName = new TestName();
    private Map<String, String> commName;
    private Map<String, String[]> obserNames;
    
    // single test data
    final private Core core = new Core();
    private SmartHouseApplication app;
    private InteractiveSensor sensor;
    
    @Before public void InstallTestApp(){
        app = new TestApplication();
        app.setApplicationsHandler((ApplicationsHandler)core.getHandler(Handler.APPS));
        /* assumption here ^- using the appHandler of the core is enough in order to receive apps' API 
         * services from the system. This is somewhat white-box testing and the tests will break if this 
         * changes.
        */
        String current = testName.getMethodName();
        sensor = new TestSensor(commName.get(current), obserNames.get(current));
    }
    
    // JUnits shouldn't be static!
    @Test @SuppressWarnings("static-method") public void TBD(){
        //todo:
    }
}

