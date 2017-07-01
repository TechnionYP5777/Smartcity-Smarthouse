package il.ac.technion.cs.smarthouse.sensors;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.List;

/**
 * @author Elia Traore
 * @since Jun 17, 2017
 */
// TODO: RENAME!
public enum PathType {
    INFO_SENDING("INFO_SENDING"),
    INSTRUCTION_RECEIVING("INSTRUCTION_RECEIVING");

    private final String type;

    private PathType(final String type) {
        this.type = type;
    }

    public static PathType fromString(final String from) {
        final String fromLower = from.toLowerCase();
        final List<PathType> $ = Arrays.asList(PathType.values()).stream().filter(mt -> mt.type.equals(fromLower))
                        .collect(Collectors.toList());
        $.add(null);
        return $.get(0);
    }
}