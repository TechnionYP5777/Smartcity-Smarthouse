package il.ac.technion.cs.smarthouse.system.applications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.annotations.Expose;

import il.ac.technion.cs.smarthouse.system.Savable;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.smarthouse.system.exceptions.AppInstallerException;
import il.ac.technion.cs.smarthouse.system.exceptions.ApplicationInitializationException;
import il.ac.technion.cs.smarthouse.utils.UuidGenerator;

/** Part of the system's core. Stores the managers of the running applications.
 * @author Elia Traore
 * @author Inbal Zukerman
 * @author RON
 * @since Dec 13, 2016 */
public class ApplicationsCore implements Savable {
    @Expose private List<ApplicationManager> apps = new ArrayList<>();
    private SystemCore systemCore;

    /** Initialize the applicationHandler with the database responsible of
     * managing the data in the current session */
    public ApplicationsCore(final SystemCore systemCore) {
        this.systemCore = systemCore;
    }

    // [start] Services to the SystemCore
    /** Adds a new application to the system, and initializes it
     * @throws IOException
     * @throws AppInstallerException
     * @throws Exception
     * @throws ApplicationInitializationException */
    public ApplicationManager addApplication(final ApplicationPath appPath) throws Exception {
        final ApplicationManager $ = new ApplicationManager(UuidGenerator.GenerateUniqueIDstring(), appPath);
        initializeApplicationManager($);
        apps.add($);
        return $;
    }

    public List<ApplicationManager> getApplicationManagers() {
        return Collections.unmodifiableList(apps);
    }
    // [end]
    
    // [start] Private functions
    private void initializeApplicationManager(ApplicationManager $) throws Exception {
        $.initialize(systemCore.serviceManager);
    }
    // [end]
    
    @Override public void populate(final String jsonString) throws Exception {
        Savable.super.populate(jsonString);
        
        for (ApplicationManager applicationManager : apps)
            initializeApplicationManager(applicationManager);
    }
}
