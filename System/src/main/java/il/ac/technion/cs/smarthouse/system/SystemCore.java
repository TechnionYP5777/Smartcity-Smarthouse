package il.ac.technion.cs.smarthouse.system;

import java.util.Optional;
import java.util.function.BiConsumer;

import org.parse4j.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.Expose;

import il.ac.technion.cs.smarthouse.database.DatabaseManager;
import il.ac.technion.cs.smarthouse.database.ServerManager;
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
    public final SensorsManager sManager = new SensorsManager();

    public final DatabaseManager databaseManager = new DatabaseManager();
    public final SensorsLocalServer sensorsHandler = new SensorsLocalServer(sManager);
    @Expose private final ApplicationsCore applicationsHandler = new ApplicationsCore(this);
    private final FileSystem fileSystem = new FileSystemImpl();
    @Expose protected UserInformation user;
    private boolean userInitialized;

    public void initializeSystemComponents() {
        log.info("Initializing system components...");
        ServerManager.initialize();
        initFileSystemListeners();
        loadDataFromDatabase();
        new Thread(sensorsHandler).start();
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
        sensorsHandler.closeSockets();
    }

    public ApplicationsCore getSystemApplicationsHandler() {
        return applicationsHandler;
    }

    public SensorsManager getSystemDatabaseHandler() {
        return sManager;
    }

    public ServiceManager getSystemServiceManager() {
        return serviceManager;
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public void initFileSystemListeners() {
        fileSystem.subscribe((path, data) -> {
            fileSystem.sendMessage(this.toJsonString(), FileSystemEntries.SYSTEM_DATA_IMAGE.buildPath());
            log.info("System interrupt: SAVE_ME: " + this.toJsonString());
        }, FileSystemEntries.SAVEME.buildPath());

        BiConsumer<String, Object> databaseManagerEventHandler = (path, data) -> {
            try {
                databaseManager.addInfo(path, data);
            } catch (ParseException e) {
                log.error("Message from (" + path + ") could not be saved on the server", e);
            }
        };

        fileSystem.subscribe(databaseManagerEventHandler, FileSystemEntries.SENSORS_DATA.buildPath());

        fileSystem.subscribe(databaseManagerEventHandler, FileSystemEntries.APPLICATIONS_DATA.buildPath());

        fileSystem.subscribe(databaseManagerEventHandler, FileSystemEntries.SYSTEM_DATA_IMAGE.buildPath());

        fileSystem.subscribe((path, data) -> {
            try {
                System.out.println("About to populate");
                String s = databaseManager.getLastEntry(FileSystemEntries.SYSTEM_DATA_IMAGE.buildPath()).split("=")[1]; 
                System.out.println(s);
                System.out.println(">>>> IN LOAD_DATA_IMAGE before populate: " + Optional.ofNullable(user).orElse(new UserInformation("<NO_USER>", "", "", "")).getName());
                this.populate(s);
                System.out.println(">>>> IN LOAD_DATA_IMAGE after populate: " + Optional.ofNullable(user).orElse(new UserInformation("<NO_USER>", "", "", "")).getName());
            } catch (Exception e) {
                // TODO inbal
                e.printStackTrace();
            }
            // TODO: Inbal: load to system from SYSTEM_DATA_IMAGE with populate
        }, FileSystemEntries.LOAD_DATA_IMAGE.buildPath());
    }
    
    private void loadDataFromDatabase() {
        fileSystem.sendMessage(null, FileSystemEntries.LOAD_DATA_IMAGE.buildPath());
    }

}
