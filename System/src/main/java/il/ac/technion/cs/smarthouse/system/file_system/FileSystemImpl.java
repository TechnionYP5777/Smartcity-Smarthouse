/**
 * 
 */
package il.ac.technion.cs.smarthouse.system.file_system;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorApiImpl;
import il.ac.technion.cs.smarthouse.utils.UuidGenerator;

/**
 * An event handler file system implementation class.
 * <P>
 * This implementation uses a file tree for storing the data and eventHandlers
 * 
 * @author RON
 * @author Inbal Zukerman
 * @since 28-05-2017
 */
public class FileSystemImpl implements FileSystem {
    
    private static final Logger log = LoggerFactory.getLogger(FileSystemImpl.class);

    static class FileNode {
        private Object data;
        private Object mostRecentDataOnBranch;
        private Map<String, BiConsumer<String, Object>> eventHandlers = new HashMap<>();
        private Map<String, FileNode> children = new HashMap<>();

        @SuppressWarnings("unchecked")
        <T> T getData() {
            return (T) data;
        }
        
        @SuppressWarnings("unchecked")
        public <T> T getMostRecentDataOnBranch() {
            return (T) mostRecentDataOnBranch;
        }
        
        public void setMostRecentDataOnBranch(Object mostRecentDataOnBranch) {
            this.mostRecentDataOnBranch = mostRecentDataOnBranch;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public Collection<String> getChildrenNames() {
            return children.keySet();
        }

        public FileNode getChild(String name, boolean create) {
            if (!children.containsKey(name) &&create)
                children.put(name, new FileNode());
            return children.get(name);
        }

        String addEventHandler(BiConsumer<String, Object> eventHandler) {
            final String id = UuidGenerator.GenerateUniqueIDstring();
            eventHandlers.put(id, eventHandler);
            return id;
        }

        void removeEventHandler(String eventHandlerId) {
            eventHandlers.remove(eventHandlerId);
        }

        public Collection<BiConsumer<String, Object>> getEventHandlers() {
            return eventHandlers.values();
        }
    }

    private class FileSystemWalkResults {
        FileNode fileNode;
        List<BiConsumer<String, Object>> eventHandlersOnBranch;

        FileSystemWalkResults(FileNode fileNode, List<BiConsumer<String, Object>> eventHandlersOnBranch) {
            this.fileNode = fileNode;
            this.eventHandlersOnBranch = eventHandlersOnBranch;
        }
    }

    private FileNode root = new FileNode();

    private FileSystemWalkResults fileSystemWalk(boolean create, Object newDataToAdd, String... path) {
        List<BiConsumer<String, Object>> eventHandlersOnBranch = new ArrayList<>();

        FileNode node = root;
        
        eventHandlersOnBranch.addAll(node.getEventHandlers());
        
        if (newDataToAdd != null)
            node.setMostRecentDataOnBranch(newDataToAdd);

        for (String pathNode : PathBuilder.decomposePath(path)) {
            node = node.getChild(pathNode, create);
            
            if (node == null)
                return new FileSystemWalkResults(null, eventHandlersOnBranch);
            
            if (newDataToAdd != null)
                node.setMostRecentDataOnBranch(newDataToAdd);
            
            eventHandlersOnBranch.addAll(node.getEventHandlers());
        }
        
        

        return new FileSystemWalkResults(node, eventHandlersOnBranch);
    }

    @Override
    public String subscribe(BiConsumer<String, Object> eventHandler, String... path) {
        log.info("subscribed on " + PathBuilder.buildPath(path));
        return fileSystemWalk(true, null, path).fileNode.addEventHandler(eventHandler);
    }

    @Override
    public void unsubscribe(String eventHandlerId, String... path) {
        Optional.of(fileSystemWalk(false, null, path).fileNode).ifPresent(n -> n.removeEventHandler(eventHandlerId));
    }

    @Override
    public void sendMessage(Object data, String... path) {
        FileSystemWalkResults r = fileSystemWalk(true, data, path);

        r.fileNode.setData(data);
        
        for (BiConsumer<String, Object> eventHandler : r.eventHandlersOnBranch) {
            log.info("firing on " + PathBuilder.buildPath(path));
            eventHandler.accept(PathBuilder.buildPath(path), data); // TODO: should be on a new thread later
        }
    }

    @Override
    public <T> T getData(String... path) {
        return fileSystemWalk(false, null, path).fileNode == null ? null : fileSystemWalk(false, null, path).fileNode.getData();
    }
    
    @Override
    public <T> T getMostRecentDataOnBranch(String... path) {
        return fileSystemWalk(false, null, path).fileNode == null ? null : fileSystemWalk(false, null, path).fileNode.getMostRecentDataOnBranch();
    }

    @Override
    public Collection<String> getChildren(String... path) {
        return fileSystemWalk(false, null, path).fileNode == null ? Collections.emptyList()
                        : fileSystemWalk(false, null, path).fileNode.getChildrenNames();
    }

    @Override
    public boolean wasPathInitiated(String... path) {
        return fileSystemWalk(false, null, path).fileNode != null;
    }
}
