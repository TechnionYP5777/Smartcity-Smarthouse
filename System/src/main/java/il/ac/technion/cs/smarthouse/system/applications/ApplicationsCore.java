package il.ac.technion.cs.smarthouse.system.applications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.Expose;

import il.ac.technion.cs.smarthouse.developers_api.ApplicationMetaData;
import il.ac.technion.cs.smarthouse.notification_center.NotificationsCenter;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.applications.installer.AppInstallerException;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.smarthouse.system.cores.ChildCore;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;
import il.ac.technion.cs.smarthouse.utils.UuidGenerator;

/**
 * Part of the system's core. Stores the managers of the running applications.
 * 
 * @author Elia Traore
 * @author Inbal Zukerman
 * @author RON
 * @since Dec 13, 2016
 */
public class ApplicationsCore extends ChildCore {
    private static Logger log = LoggerFactory.getLogger(ApplicationsCore.class);

    @Expose private final List<ApplicationMetaData> apps = new ArrayList<>();
    private Runnable onAppsChange;

    /**
     * Initialize the applicationHandler with the database responsible of
     * managing the data in the current session
     */
    public ApplicationsCore(final SystemCore systemCore) {
        super(systemCore);
    }

    // [start] Services to the SystemCore
    /**
     * Adds a new application to the system, and initializes it
     * 
     * @throws IOException
     * @throws AppInstallerException
     * @throws Exception
     */
    public ApplicationMetaData addApplication(final ApplicationPath appPath) {
        try {
            final ApplicationMetaData $ = new ApplicationMetaData(UuidGenerator.GenerateUniqueIDstring(), appPath);
            apps.add($);
            initializeApplicationManager($);
            return $;
        } catch (final Exception $) {
            log.warn("\n\tAplication (" + appPath + ") failed to install", $);
            NotificationsCenter.sendAppFailedToInstall($.getMessage());
        }
        return null;
    }

    public List<ApplicationMetaData> getApplicationsMetaData() {
        return Collections.unmodifiableList(apps);
    }

    public List<String> getInstalledApplicationNames() {
        final List<String> l = new ArrayList<>();
        for (final ApplicationMetaData applicationManager : apps)
            Optional.ofNullable(applicationManager.getApplicationName()).ifPresent(l::add);
        return l;
    }

    public void setOnAppsListChange(final Runnable onAppsChange) {
        this.onAppsChange = onAppsChange;
    }
    // [end]

    // [start] Private functions
    private void initializeApplicationManager(final ApplicationMetaData $) throws Exception {
        $.initialize(systemCore);
        log.info("\n\tApplicationsCore: initializing a new application\n\tAppliactions name: "
                        + $.getApplicationName());
        Optional.ofNullable(onAppsChange).ifPresent(a -> JavaFxHelper.surroundConsumerWithFx(a).run());
        NotificationsCenter.sendNewAppInstalled($.getApplicationName());
    }
    // [end]

    /*
     * (non-Javadoc)
     * 
     * @see
     * il.ac.technion.cs.smarthouse.system.Savable#populate(java.lang.String)
     */
    @Override
    public void populate(final String jsonString) throws Exception {
        super.populate(jsonString);

        for (final ApplicationMetaData applicationManager : apps)
            initializeApplicationManager(applicationManager);
    }
}
