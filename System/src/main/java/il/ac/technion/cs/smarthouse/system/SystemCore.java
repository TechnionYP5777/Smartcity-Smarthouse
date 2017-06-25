package il.ac.technion.cs.smarthouse.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.Expose;

import il.ac.technion.cs.smarthouse.notification_center.NotificationsCenter;
import il.ac.technion.cs.smarthouse.system.applications.ApplicationsCore;
import il.ac.technion.cs.smarthouse.system.dashboard.DashboardCore;
import il.ac.technion.cs.smarthouse.system.database.SystemSaver;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemImpl;
import il.ac.technion.cs.smarthouse.system.mapping_information.MappingInformation;
import il.ac.technion.cs.smarthouse.system.sensors.SensorsLocalServer;
import il.ac.technion.cs.smarthouse.system.services.ServiceManager;
import il.ac.technion.cs.smarthouse.system.user_information.UserInformation;

/**
 * Hold the databases of the smart house, and allow sensors and applications to
 * store and read information about the changes in the environment
 */
public class SystemCore implements Savable {
    private static Logger log = LoggerFactory.getLogger(SystemCore.class);

    private SystemMode systemMode = SystemMode.USER_MODE;

    public final ServiceManager serviceManager = new ServiceManager(this);

    @Expose private final ApplicationsCore applicationsHandler = new ApplicationsCore(this);
    @Expose private final FileSystemImpl fileSystem = new FileSystemImpl();
    private SystemSaver systemSaver = new SystemSaver(this, fileSystem);
    public final SensorsLocalServer sensorsLocalServer = new SensorsLocalServer(fileSystem);
    private final DashboardCore dashboardCore = new DashboardCore(this);
    @Expose protected UserInformation user;
    @Expose protected MappingInformation house = new MappingInformation();
    private boolean userInitialized;

    public void initializeSystemComponents(final boolean useSensorsServer, final boolean useCloudServer,
                    final boolean useLocalDatabase, final boolean initRegularListeners) {
        log.info("\n\tInitializing system components...");

        systemSaver.init(useCloudServer, useLocalDatabase, true);
        systemSaver.loadSystem();

        if (initRegularListeners)
            initFileSystemListeners();
        if (useSensorsServer)
            new Thread(sensorsLocalServer).start();
    }

    public UserInformation getUser() {
        return user;
    }
    
    public MappingInformation getHouse() {
        return house;
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
        systemSaver.saveSystem();
        systemSaver.close();
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

    public SystemMode getSystemMode() {
        return systemMode;
    }

    public void setSystemMode(SystemMode m) {
        this.systemMode = m;
    }

    public void initFileSystemListeners() {
        // TODO: add some listeners here
    }
}
