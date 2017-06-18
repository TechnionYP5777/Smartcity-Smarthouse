package il.ac.technion.cs.smarthouse.system.database;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import org.parse4j.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.database.DatabaseManager;
import il.ac.technion.cs.smarthouse.database.DatabaseManager.DataEntry;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.cores.ChildCore;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemImpl;
import il.ac.technion.cs.smarthouse.utils.TimeCounter;
import il.ac.technion.cs.smarthouse.utils.TimedListener;

public class SystemSaver extends ChildCore {
    private static Logger log = LoggerFactory.getLogger(SystemSaver.class);
    
    private static final long MILISEC_TO_SAVE = TimeUnit.MINUTES.toMillis(5);
    
    private final DatabaseManager databaseManager = new DatabaseManager();
    private final FileSystemImpl fileSystem;
    private boolean useCloudServer;
    private TimedListener timedSaveme;
    
    public SystemSaver(final SystemCore systemCore, final FileSystemImpl fs) {
        super(systemCore);
        fileSystem = fs;
    }
    
    public SystemSaver init(final boolean useCloudServer1, final boolean initSavemeListener) {
        this.useCloudServer = useCloudServer1;
        if (initSavemeListener)
            initFileSystemSavemeListener();
        
        timedSaveme = new TimedListener(()->saveSystem(), LocalTime.now().plusSeconds(10), MILISEC_TO_SAVE);
        
        if (initSavemeListener)
            timedSaveme.start();
        
        return this;
    }
    
    public boolean loadSystem() {
        if (useCloudServer && loadSystemFromCloud())
            return true;
        return loadSystemFromLocalDatabase();
    }
    
    public void saveSystem() {
        saveSystemToLocalDatabase();
        if (useCloudServer)
            saveSystemToCloud();
    }
    
    private void initFileSystemSavemeListener() {
        systemCore.getFileSystem().subscribe((path, data) -> saveSystem(), FileSystemEntries.SAVEME.buildPath());
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
            databaseManager.addInfo(FileSystemEntries.SYSTEM_DATA_IMAGE.buildPath(), system2json());
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
        LocalSaver.saveData(system2json());
    }

    private void loadSystemFromJson(String json) throws Exception {
        System.out.println(fileSystem);
        systemCore.populate(json);
        System.out.println(fileSystem);
//        fileSystem.deleteFromPath(FileSystemEntries.SENSORS.buildPath());
//        fileSystem.deleteFromPath(FileSystemEntries.SYSTEM.buildPath());
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
