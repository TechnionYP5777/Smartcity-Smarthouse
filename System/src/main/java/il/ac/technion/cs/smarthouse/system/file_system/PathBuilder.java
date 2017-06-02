package il.ac.technion.cs.smarthouse.system.file_system;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A static class that operates on the dispatcher's paths
 * 
 * @author RON
 * @since 28-05-2017
 */
public class PathBuilder {
    /**
     * DELIMITER is the const string we will use to separate between the
     * different parts of a path
     */
    public static final String DELIMITER = ".";

    public static final String SPLIT_REGEX = "\\" + DELIMITER; // todo: rename
                                                               // as
                                                               // DELIMITER_REGEX
                                                               // ?

    /**
     * SEPARATOR is the const string we will use to part the path of the message
     * from the value it updates
     */
    public static final String SEPARATOR = "=";

    public static String buildPath(final String... nodes) {
        return String.join(DELIMITER, Stream.of(nodes).filter(s -> !s.isEmpty()).collect(Collectors.toList()));
    }

    public static String buildPath(final List<String> nodes) {
        return buildPath(nodes.toArray(new String[0]));
    }

    public static List<String> decomposePath(final String... path) {
        return path.length == 0 ? Collections.emptyList() : Arrays.asList(buildPath(path).split(SPLIT_REGEX));
    }

}
