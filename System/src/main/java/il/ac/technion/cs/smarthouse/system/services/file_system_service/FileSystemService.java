package il.ac.technion.cs.smarthouse.system.services.file_system_service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.file_system.PathBuilder;
import il.ac.technion.cs.smarthouse.system.services.Service;

public class FileSystemService extends Service {
    private final FileSystem fileSystem;

    public FileSystemService(final SystemCore $) {
        super($);
        fileSystem = $.getFileSystem();
    }

    private String encapsulatePath(final String... path) {
        return FileSystemEntries.SENSORS_DATA.buildPath(path);
    }

    public String subscribe(final BiConsumer<String, Object> eventHandler, final String... path) {
        return fileSystem.subscribe((path1, data) -> {
            final List<String> l = PathBuilder.decomposePath(path1).stream()
                            .collect(Collectors.toCollection(ArrayList::new));
            l.remove(0);
            l.remove(l.size() - 1);
            eventHandler.accept(PathBuilder.buildPath(l), data);
        }, encapsulatePath(path));
    }

    public void unsubscribe(final String subscriberId) {
        fileSystem.unsubscribe(subscriberId);
    }

    public <T> T getData(final String... path) {
        return fileSystem.getMostRecentDataOnBranch(encapsulatePath(path));
    }

}
