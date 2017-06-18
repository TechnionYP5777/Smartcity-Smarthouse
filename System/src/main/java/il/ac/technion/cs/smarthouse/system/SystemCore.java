package il.ac.technion.cs.smarthouse.system;

import java.util.function.BiConsumer;

import org.parse4j.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.Expose;

import il.ac.technion.cs.smarthouse.database.DatabaseManager;
import il.ac.technion.cs.smarthouse.database.DatabaseManager.DataEntry;
import il.ac.technion.cs.smarthouse.database.LocalSaver;
import il.ac.technion.cs.smarthouse.notification_center.NotificationsCenter;
import il.ac.technion.cs.smarthouse.system.applications.ApplicationsCore;
import il.ac.technion.cs.smarthouse.system.dashboard.DashboardCore;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemImpl;
import il.ac.technion.cs.smarthouse.system.sensors.SensorsLocalServer;
import il.ac.technion.cs.smarthouse.system.services.ServiceManager;
import il.ac.technion.cs.smarthouse.system.user_information.UserInformation;
import il.ac.technion.cs.smarthouse.utils.TimeCounter;

/**
 * Hold the databases of the smart house, and allow sensors and applications to
 * store and read information about the changes in the environment
 */
public class SystemCore implements Savable {
    private static Logger log = LoggerFactory.getLogger(SystemCore.class);

    public final ServiceManager serviceManager = new ServiceManager(this);

    public final DatabaseManager databaseManager = new DatabaseManager();
    @Expose private final ApplicationsCore applicationsHandler = new ApplicationsCore(this);
    @Expose private final FileSystemImpl fileSystem = new FileSystemImpl();
    public final SensorsLocalServer sensorsLocalServer = new SensorsLocalServer(fileSystem);
    private final DashboardCore dashboardCore = new DashboardCore(this);
    @Expose protected UserInformation user;
    private boolean userInitialized;

    public void initializeSystemComponents(final boolean useSensorsServer, final boolean useCloudServer,
                    final boolean initRegularListeners) {
        log.info("Initializing system components...");
        if (useCloudServer)
            if (!loadSystemFromLocalDatabase())
                loadSystemFromCloud();
        if (initRegularListeners)
            initFileSystemListeners();
        if (useSensorsServer)
            new Thread(sensorsLocalServer).start();
    }

    public UserInformation getUser() {
        return user;
    }

    public void initializeUser(final String name, final String id, final String phoneNumber, final String homeAddress) {
        user = new UserInformation(name, id, phoneNumber, homeAddress);
        userInitialized = true;
    }

    public boolean isUserInitialized() {
        return userInitialized;
    }

    public void shutdown() {
        sensorsLocalServer.closeSockets();
        NotificationsCenter.close();
    }

    public ApplicationsCore getSystemApplicationsHandler() {
        return applicationsHandler;
    }

    public DashboardCore getSystemDashboardCore() {
        return dashboardCore;
    }

    public ServiceManager getSystemServiceManager() {
        return serviceManager;
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public void initFileSystemListeners() {
        final BiConsumer<String, Object> databaseManagerEventHandler_saveSystem = (path, data) -> {
            saveSystemToLocalDatabase();
            saveSystemToCloud();
        };

//        final BiConsumer<String, Object> databaseManagerEventHandler_addDataFromPath = (path, data) -> {
//            try {
//                final TimeCounter t = new TimeCounter();
//                databaseManager.addInfo(path, data);
//                log.info("Saved data (from path: " + path + ") to the cloud server... Total time: "
//                                + t.getTimePassedMillis() + " [ms]");
//            } catch (final ParseException e) {
//                log.error("Data from (" + path + ") could not be saved on the cloud server", e);
//            }
//        };

        fileSystem.subscribe(databaseManagerEventHandler_saveSystem, FileSystemEntries.SAVEME.buildPath());
        // fileSystem.subscribe(databaseManagerEventHandler_addDataFromPath,
        // FileSystemEntries.SENSORS_DATA.buildPath());
        // fileSystem.subscribe(databaseManagerEventHandler_addDataFromPath,
        // FileSystemEntries.APPLICATIONS_DATA.buildPath());
    }

    private boolean loadSystemFromCloud() {
        try {
            final TimeCounter t = new TimeCounter();
            DataEntry d = databaseManager.getLastEntry(FileSystemEntries.SYSTEM_DATA_IMAGE.buildPath());
            if (d == null)
                return false;
            loadSystemFromJson(d.data);
            log.info("Loaded system from the database cloud... Total time: " + t.getTimePassedMillis() + " [ms]");
            return true;
        } catch (Exception e) {
            String err = "Cloud server problem";
            log.error(err, e);
            throw new RuntimeException(err, e);
        }
    }

    private void saveSystemToCloud() {
        try {
            final TimeCounter t = new TimeCounter();
            databaseManager.deleteInfo(FileSystemEntries.SYSTEM_DATA_IMAGE.buildPath());
            databaseManager.addInfo(FileSystemEntries.SYSTEM_DATA_IMAGE.buildPath(), this.toJsonString());
            log.info("Saved system image to the cloud server... Total time: " + t.getTimePassedMillis() + " [ms]");
        } catch (ParseException e) {
            log.error("System image could not be saved on the cloud server... system is unchanged", e);
        }
    }

    private boolean loadSystemFromLocalDatabase() {
        String data = LocalSaver.readData();
        if (data != null)
            try {
                loadSystemFromJson(data);
                return true;
            } catch (Exception e) {
                log.error("Couldn't load data from local database... system is unchanged", e);
            }
        return false;
    }

    private void saveSystemToLocalDatabase() {
        LocalSaver.saveData(this.toJsonString());
    }

    private void loadSystemFromJson(String json) throws Exception {
        this.populate(json);
        fileSystem.deleteFromPath(FileSystemEntries.SENSORS.buildPath());
        fileSystem.deleteFromPath(FileSystemEntries.SYSTEM.buildPath());
    }

}
