package il.ac.technion.cs.smarthouse.system.file_system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@formatter:off
/**
 * An enum for all predefined entries in the file system
 * 
 * @author RON
 * @author Inbal Zukerman
 * @since 28-05-2017
 */
public enum FileSystemEntries {
    ROOT("", null),
        APPLICATIONS_DATA("applications_data", ROOT),
        
        SENSORS("sensors", ROOT),
            COMMERCIAL_NAME(null, SENSORS, PathComponentType.SOFT_SUFFIX),
                SENSOR_ID(null, COMMERCIAL_NAME, PathComponentType.SOFT_SUFFIX),
                    LOCATION("location", SENSOR_ID, PathComponentType.HARD_SUFFIX),
                    ALIAS("alias", SENSOR_ID, PathComponentType.HARD_SUFFIX),
                    DONE_SENDING_MSG("done", SENSOR_ID, PathComponentType.HARD_SUFFIX),
                    LISTENERS_OF_SENSOR("listeners_of_sensor", SENSOR_ID),
                    
        /**
         * Don't forget to add the id at the end of the path when needed! 
         */
        SENSORS_DATA("sensors_data", ROOT),
            SENSORS_DATA_FULL__WITH_SENSOR_ID(null, SENSORS_DATA),
        
        SYSTEM("system", ROOT),
            SYSTEM_INTERNALS("internals", SYSTEM),
                SAVEME("saveme", SYSTEM_INTERNALS, PathComponentType.HARD_SUFFIX),
                LOAD_DATA_IMAGE("load", SYSTEM_INTERNALS, PathComponentType.HARD_SUFFIX),
            SYSTEM_DATA_IMAGE("data_image", SYSTEM, PathComponentType.HARD_SUFFIX),
            
        TESTS("tests", ROOT),
            TESTS_SENSORS_DATA("sensors_data_tests", TESTS)
    ;
        
    /*
     * File System Layout:
     * 
     *  /
     *  ├───applications_data
     *  ├───sensors
     *  │   ├───COMMERCIAL_NAME_1
     *  │   │   ├───SENSOR_ID_1
     *  │   │   │   ├───done
     *  │   │   │   ├───location
     *  │   │   │   └───listeners_of_sensor
     *  │   │   └───SENSOR_ID_2
     *  │   │       ├───done
     *  │   │       ├───location
     *  │   │       └───listeners_of_sensor
     *  │   └───COMMERCIAL_NAME_2
     *  │       └───SENSOR_ID_3
     *  │           ├───done
     *  │           ├───location
     *  │           └───listeners_of_sensor
     *  ├───sensors_data
     *  │   └───DATA_FIELD_1
     *  │       └───DATA_FIELD_2
     *  │           └───SENSOR_ID_1
     *  └───system
     *      ├───data_image
     *      └───internals
     *          ├───load
     *          └───saveme
     */
  //@formatter:on
    
    private static enum PathComponentType {
        HARD_SUFFIX, SOFT_SUFFIX, PREFIX
    }

    private static Logger log = LoggerFactory.getLogger(FileSystemEntries.class);

    private String name;
    private FileSystemEntries parent;
    private PathComponentType pathType;

    /**
     * A new file system entry
     * 
     * @param name
     *            The name of the entry.<br>
     *            If null, it will be back-filled
     * @param parent
     *            The parent FileSystemEntries in the hierarchy
     * @param isSuffix
     *            if false, the given path in the
     *            {@link FileSystemEntries#buildPath(String...)} will be
     *            appended to the entry.<br>
     *            If true, it won't (only back-filling will be allowed)
     */
    private FileSystemEntries(final String name, final FileSystemEntries parent, final PathComponentType pathType) {
        this.name = name;
        this.parent = parent;
        this.pathType = pathType;
    }
    
    private FileSystemEntries(final String name, final FileSystemEntries parent) {
        this(name, parent, PathComponentType.PREFIX);
    }
    
    private List<FileSystemEntries> getBranch() {
        final List<FileSystemEntries> l = new ArrayList<>();
        
        for (FileSystemEntries p = this; p != null; p = p.parent)
            l.add(0, p);
        
        return l;
    }

    /**
     * builds the path with the correct formatting.
     * <p>
     * look in {@link FileSystemEntryTest} for examples
     * 
     * @param base
     * @return
     */
    public String buildPath(final String... base) {
        final List<String> baseSplited = PathBuilder.decomposePath(base);
        final List<String> l = new ArrayList<>();
        final List<FileSystemEntries> branch = getBranch();
        
        int baseIdx = 0;
        for (FileSystemEntries e : branch)
            if (e.name != null)
                l.add(e.name);
            else {
                if (baseIdx >= baseSplited.size()) {
                    final RuntimeException r = new RuntimeException("Path (" + baseSplited + ") was not long enough");
                    log.error("Error while building the path", r);
                    throw r;
                }
                l.add(baseSplited.get(baseIdx++));
            }
        
        final List<String> pathToAppend = baseSplited.subList(baseIdx, baseSplited.size());
        
        if (!pathToAppend.isEmpty()) {
            final RuntimeException r = new RuntimeException("Path (" + baseSplited + ") was longer than expected");
            
            switch (pathType) {
                case SOFT_SUFFIX:
                    log.warn("Warning while building the path", r);
                    break;
                case HARD_SUFFIX:
                    log.error("Error while building the path", r);
                    throw r;
                default:
                    break;
            }
        }
        
        l.addAll(pathToAppend);
        
        return PathBuilder.buildPath(l);
    }
    
    public String getPartFromPath(final String... path) {
        int idx = 0;
        FileSystemEntries p = this;
        for (idx = 0; p.parent != ROOT; ++idx)
            p = p.parent;
        return PathBuilder.decomposePath(path).get(idx);
    }
    
    public boolean isValidPath(final String... path) {
        final List<String> l = PathBuilder.decomposePath(path);
        final List<FileSystemEntries> branch = getBranch();
        
        for (int ib = 0, ip = 0; ib < branch.size() && ip < l.size(); ++ib, ++ip) {
            if (branch.get(ib).name == null)
                continue;
            if (branch.get(ib).name.isEmpty()) {
                --ip;
                continue;
            }
            if (!branch.get(ib).name.equals(l.get(ip)))
                return false;
        }
        
        return true;
    }
}
