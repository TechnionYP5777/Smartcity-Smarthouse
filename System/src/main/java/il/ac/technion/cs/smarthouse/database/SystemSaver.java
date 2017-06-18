package il.ac.technion.cs.smarthouse.database;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import org.parse4j.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.database.cloud_server.DatabaseManager;
import il.ac.technion.cs.smarthouse.database.cloud_server.DatabaseManager.DataEntry;
import il.ac.technion.cs.smarthouse.database.local_cache.LocalSaver;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.cores.ChildCore;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemImpl;
import il.ac.technion.cs.smarthouse.utils.TimeCounter;
import il.ac.technion.cs.smarthouse.utils.TimedListener;

/**
 * All system saving stuff is in this class
 * <p>
 * The system is saved both on server and locally.
 * <br>
 * The system is loaded from the local DB if possible, and from the cloud if not.
 * 
 * @author RON
 * @since 18-06-2017
 */
public class SystemSaver extends ChildCore {
    private static Logger log = LoggerFactory.getLogger(SystemSaver.class);

    private static final long MILISEC_TO_SAVE = TimeUnit.MINUTES.toMillis(5);

    private final DatabaseManager databaseManager = new DatabaseManager();
    private boolean useCloudServer;
    private boolean useLocalDatabase;
    private TimedListener timedSaveme;

    public SystemSaver(final SystemCore systemCore, final FileSystemImpl fs) {
        super(systemCore);
    }

    public SystemSaver init(final boolean useCloudServer1, final boolean useLocalDatabase1,
                    final boolean initSavemeListener) {
        this.useCloudServer = useCloudServer1;
        this.useLocalDatabase = useLocalDatabase1;

        if (initSavemeListener)
            initFileSystemSavemeListener();

        timedSaveme = new TimedListener(() -> saveSystem(), LocalTime.now().plusSeconds(10), MILISEC_TO_SAVE);

        if (initSavemeListener)
            timedSaveme.start();

        return this;
    }

    public boolean loadSystem() {
        if (loadSystemFromLocalDatabase())
            return true;
        return loadSystemFromCloud();
    }

    public void saveSystem() {
        saveSystemToLocalDatabase();
        saveSystemToCloud();
    }

    private void initFileSystemSavemeListener() {
        systemCore.getFileSystem().subscribe((path, data) -> saveSystem(), FileSystemEntries.SAVEME.buildPath());
    }

    private boolean loadSystemFromCloud() {
        if (!useCloudServer)
            return false;
        
        try {
            final TimeCounter t = new TimeCounter();
            DataEntry d = databaseManager.getLastEntry(FileSystemEntries.SYSTEM_DATA_IMAGE.buildPath());
            if (d == null)
                return false;
            json2system(d.data);
            log.info("Loaded system from the database cloud... Total time: " + t.getTimePassedMillis() + " [ms]");
            return true;
        } catch (Exception e) {
            log.error("Cloud server problem", e);
        }

        return false;
    }

    private void saveSystemToCloud() {
        if (!useCloudServer)
            return;
        try {
            final TimeCounter t = new TimeCounter();
            databaseManager.deleteInfo(FileSystemEntries.SYSTEM_DATA_IMAGE.buildPath());
            databaseManager.addInfo(FileSystemEntries.SYSTEM_DATA_IMAGE.buildPath(), system2json());
            log.info("Saved system image to the cloud server... Total time: " + t.getTimePassedMillis() + " [ms]");
        } catch (ParseException e) {
            log.error("System image could not be saved on the cloud server... system is unchanged", e);
        }
    }

    private boolean loadSystemFromLocalDatabase() {
        if (!useLocalDatabase)
            return false;

        String data = LocalSaver.readData();
        if (data != null)
            try {
                json2system(data);
                return true;
            } catch (Exception e) {
                log.error("Couldn't load data from local database... system is unchanged", e);
            }
        return false;
    }

    private void saveSystemToLocalDatabase() {
        if (useLocalDatabase)
            LocalSaver.saveData(system2json());
    }

    private void json2system(String json) throws Exception {
        systemCore.populate(json);
    }

    private String system2json() {
        return systemCore.toJsonString();
    }

    @Override
    public void close() {
        super.close();
        timedSaveme.kill();
    }
}
