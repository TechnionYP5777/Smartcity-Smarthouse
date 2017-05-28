package il.ac.technion.cs.smarthouse.system.file_system;

/**
 * An enum for all predefined entries in the file system / dispatcher
 * 
 * @author RON
 * @author Inbal Zukerman
 * @since 28-05-2017
 */
public enum FileSystemEntries {
    SENSORS("sensors"),
    SENSORS_DATA("sensors_data"),
    SYSTEM("system"),
    SYSTEM_INTERNALS(PathBuilder.buildPath(SYSTEM, "internals")),
    SYSTEM_INTERNALS_SAVEME(PathBuilder.buildPath(SYSTEM_INTERNALS, "saveme")),
    SYSTEM_DATA_IMAGE(PathBuilder.buildPath(SYSTEM, "data_image")),
    APPLICATIONS_DATA("applications_data");
    
    /*
     * File System Layout:
     * 
     *  /
     *  ├───applications_data
     *  ├───sensors
     *  │   ├───COMMERCIAL_NAME_1
     *  │   │   ├───SENSOR_ID_1
     *  │   │   │   ├───done
     *  │   │   │   └───location
     *  │   │   └───SENSOR_ID_2
     *  │   │       ├───done
     *  │   │       └───location
     *  │   └───COMMERCIAL_NAME_2
     *  │       └───SENSOR_ID_3
     *  │           ├───done
     *  │           └───location
     *  ├───sensors_data
     *  │   └───DATA_FIELD_1
     *  │       └───DATA_FIELD_2
     *  │           └───SENSOR_ID_1
     *  └───system
     *      ├───data_image
     *      ├───internals
     *      └───saveme
     */
    
    private String path;

    private FileSystemEntries(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}
