package il.ac.technion.cs.smarthouse.system.file_system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    ROOT("", null, false),
        APPLICATIONS_DATA("applications_data", ROOT, false),
        
        SENSORS("sensors", ROOT, false),
            COMMERCIAL_NAME(null, SENSORS, true),
                SENSOR_ID(null, COMMERCIAL_NAME, true),
                    LOCATION("location", SENSOR_ID, true),
                    DONE_SENDING_MSG("done", SENSOR_ID, true),
                    LISTENERS_OF_SENSOR("listeners_of_sensor", SENSOR_ID,true),
                    
        /**
         * Don't forget to add the id at the end of the path when needed! 
         */
        SENSORS_DATA("sensors_data", ROOT, false),
            SENSORS_DATA_FULL__WITH_SENSOR_ID(null, SENSORS_DATA, false),
        
        SYSTEM("system", ROOT, false),
            SYSTEM_INTERNALS("internals", SYSTEM, false),
                SAVEME("saveme", SYSTEM_INTERNALS, true),
                LOAD_DATA_IMAGE("load", SYSTEM_INTERNALS, true),
            SYSTEM_DATA_IMAGE("data_image", SYSTEM, true),
            
        TESTS("tests", ROOT, false),
            TESTS_SENSORS_DATA("sensors_data_tests", TESTS, false)
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

    private static Logger log = LoggerFactory.getLogger(FileSystemEntries.class);

    private String name;
    private FileSystemEntries parent;
    private boolean isSuffix;

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
    private FileSystemEntries(String name, FileSystemEntries parent, boolean isSuffix) {
        this.name = name;
        this.parent = parent;
        this.isSuffix = false;// isSuffix; // TODO: suffix support is disabled
    }

    private String buildPathAux(List<String> base) {
        String out = name;

        if (name == null) {
            if (base.isEmpty()) {
                RuntimeException r = new RuntimeException("the given path is not big enough");
                log.error("Error while building the path", r);
                throw r;
            }
            out = base.get(0);
            base.remove(0);
        }

        return out;
    }

    private String buildPathRecurcive(List<String> base) {
        return parent == null ? buildPathAux(base)
                        : PathBuilder.buildPath(parent.buildPathRecurcive(base), buildPathAux(base));
    }

    /**
     * builds the path with the correct formatting.
     * <p>
     * look in {@link FileSystemEntryTest} for examples
     * 
     * @param base
     * @return
     */
    public String buildPath(String... base) {
        ArrayList<String> l = PathBuilder.decomposePath(base).stream().collect(Collectors.toCollection(ArrayList::new));
        if (!isSuffix)
            return PathBuilder.buildPath(buildPathRecurcive(l), PathBuilder.buildPath(l.toArray(new String[0])));
        if (!l.isEmpty())
            log.warn("Path (" + Arrays.toString(base) + ") was longer than expected");
        return PathBuilder.buildPath(buildPathRecurcive(l));
    }

    @Override
    public String toString() {
        return name == null ? "<EMPTY>" : name;
    }
}
