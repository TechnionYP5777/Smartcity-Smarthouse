package il.ac.technion.cs.smarthouse.system.applications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.applications.api.exceptions.OnLoadException;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.smarthouse.system.exceptions.AppInstallerException;
import il.ac.technion.cs.smarthouse.system.exceptions.ApplicationInitializationException;
import il.ac.technion.cs.smarthouse.utils.UuidGenerator;

/** API allowing smart house applications to register for information and notify
 * on emergencies
 * @author Elia Traore
 * @author Inbal Zukerman
 * @since Dec 13, 2016 */
public class ApplicationsCore {
    List<ApplicationManager> apps = new ArrayList<>();
    SystemCore systemCore;

    /** Initialize the applicationHandler with the database responsible of
     * managing the data in the current session */
    public ApplicationsCore(final SystemCore systemCore) {
        this.systemCore = systemCore;
    }

    // [start] Services to the SystemCore
    /** Adds a new application to the system, and initializes it
     * @throws IOException
     * @throws AppInstallerException
     * @throws OnLoadException
     * @throws ApplicationInitializationException */
    public ApplicationManager addApplication(final ApplicationPath<?> appPath) throws AppInstallerException, IOException, OnLoadException {
        final ApplicationManager $ = new ApplicationManager(UuidGenerator.GenerateUniqueIDstring(), appPath);
        $.initialize(systemCore.serviceManager);
        apps.add($);
        return $;
    }

    public List<ApplicationManager> getApplicationManagers() {
        return Collections.unmodifiableList(apps);
    }
    // [end]
}
