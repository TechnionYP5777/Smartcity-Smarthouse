package il.ac.technion.cs.smarthouse.system.dispatcher;

/**
 * An enum for all predefined entries in the file system / dispatcher
 * 
 * @author RON
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

    private String path;

    private FileSystemEntries(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}
