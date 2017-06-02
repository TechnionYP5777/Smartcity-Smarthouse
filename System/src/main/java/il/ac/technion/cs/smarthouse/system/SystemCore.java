package il.ac.technion.cs.smarthouse.system;

import java.util.function.BiConsumer;

import org.parse4j.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.Expose;

import il.ac.technion.cs.smarthouse.database.DatabaseManager;
import il.ac.technion.cs.smarthouse.system.applications.ApplicationsCore;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemImpl;
import il.ac.technion.cs.smarthouse.system.sensors.SensorsLocalServer;
import il.ac.technion.cs.smarthouse.system.services.ServiceManager;
import il.ac.technion.cs.smarthouse.system.user_information.UserInformation;

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
    @Expose protected UserInformation user;
    private boolean userInitialized;

    public void initializeSystemComponents() {
        log.info("Initializing system components...");
        //loadSystemFromCloud();
        //initFileSystemListeners();
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
    }

    public ApplicationsCore getSystemApplicationsHandler() {
        return applicationsHandler;
    }

    public ServiceManager getSystemServiceManager() {
        return serviceManager;
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public void initFileSystemListeners() {
        BiConsumer<String, Object> databaseManagerEventHandler_saveSystem = (path, data) -> {
            try {
                final double startTime = System.nanoTime();
                databaseManager.deleteInfo(FileSystemEntries.SYSTEM_DATA_IMAGE.buildPath());
                databaseManager.addInfo(FileSystemEntries.SYSTEM_DATA_IMAGE.buildPath(), this.toJsonString());
                log.info("Saved system image to the cloud server... Total time: " + (System.nanoTime() - startTime) / 1000000 + " [ms]");
            } catch (ParseException e) {
                log.error("System image could not be saved on the cloud server", e);
            }
        };

        BiConsumer<String, Object> databaseManagerEventHandler_addDataFromPath = (path, data) -> {
            try {
                final double startTime = System.nanoTime();
                databaseManager.addInfo(path, data);
                log.info("Saved data (from path: " + path + ")  to the cloud server... Total time: " + (System.nanoTime() - startTime) / 1000000
                                + " [ms]");
            } catch (ParseException e) {
                log.error("Data from (" + path + ") could not be saved on the cloud server", e);
            }
        };

        fileSystem.subscribe(databaseManagerEventHandler_saveSystem, FileSystemEntries.SAVEME.buildPath());
        fileSystem.subscribe(databaseManagerEventHandler_addDataFromPath, FileSystemEntries.SENSORS_DATA.buildPath());
        fileSystem.subscribe(databaseManagerEventHandler_addDataFromPath,
                        FileSystemEntries.APPLICATIONS_DATA.buildPath());
    }

    private void loadSystemFromCloud() {
        try {
            final double startTime = System.nanoTime();
            this.populate(databaseManager.getLastEntry(FileSystemEntries.SYSTEM_DATA_IMAGE.buildPath()).data);
            fileSystem.deleteFromPath(FileSystemEntries.SENSORS.buildPath());
            fileSystem.deleteFromPath(FileSystemEntries.SYSTEM.buildPath());
            log.info("Loaded system from the database cloud... Total time: " + (System.nanoTime() - startTime) / 1000000
                            + " [ms]");
        } catch (Exception e) {
            log.error("Could not load data from the cloud server", e);
        }
    }

}
