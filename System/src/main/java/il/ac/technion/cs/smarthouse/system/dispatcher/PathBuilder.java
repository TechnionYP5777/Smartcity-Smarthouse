package il.ac.technion.cs.smarthouse.system.dispatcher;

import java.util.Arrays;
import java.util.List;

/**
 * A static class that operates on the dispatcher's paths
 * @author RON
 * @since 28-05-2017
 */
public class PathBuilder {
    /**
     * DELIMITER is the const string we will use to separate between the
     * different parts of a path
     */
    public static final String DELIMITER = ".";
    
    public static String buildPath(Object... nodes) {
        return buildPath(Arrays.stream(nodes).map(Object::toString).toArray(String[]::new));
    }
    
    public static String buildPath(String... nodes) {
        return String.join(DELIMITER, nodes);
    }
    
    public static List<String> decomposePath(String... path) {
        return Arrays.asList(buildPath(path).split(DELIMITER));
    }
    
    public static String buildPathForSensorsData(String basePath, String sensorId) {
        return buildPath(FileSystemEntries.SENSORS_DATA + "", basePath, sensorId);
    }
    
}
