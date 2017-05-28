/**
 * 
 */
package il.ac.technion.cs.smarthouse.system.file_system;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import il.ac.technion.cs.smarthouse.system.dispatcher.PathBuilder;
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
public class FileSystem implements IFileSystem {

    static class FileNode {
        private Object data;
        private Map<String, Consumer<String>> eventHandlers = new HashMap<>();
        private Map<String, FileNode> children = new HashMap<>();

        @SuppressWarnings("unchecked")
        <T> T getData() {
            return (T) data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public Collection<String> getChildrenNames() {
            return children.keySet();
        }

        public FileNode getChild(String name, boolean create) {
            return children.containsKey(name) ? children.get(name)
                            : !create ? null : children.put(name, new FileNode());
        }

        String addEventHandler(Consumer<String> eventHandler) {
            final String id = UuidGenerator.GenerateUniqueIDstring();
            eventHandlers.put(id, eventHandler);
            return id;
        }

        void removeEventHandler(String eventHandlerId) {
            eventHandlers.remove(eventHandlerId);
        }

        public Collection<Consumer<String>> getEventHandlers() {
            return eventHandlers.values();
        }
    }

    private class FileSystemWalkResults {
        FileNode fileNode;
        List<Consumer<String>> eventHandlersOnBranch;

        public FileSystemWalkResults(FileNode fileNode, List<Consumer<String>> eventHandlersOnBranch) {
            this.fileNode = fileNode;
            this.eventHandlersOnBranch = eventHandlersOnBranch;
        }
    }

    private FileNode root = new FileNode();

    private FileSystemWalkResults fileSystemWalk(boolean create, String... path) {
        List<Consumer<String>> eventHandlersOnBranch = new ArrayList<>();

        FileNode node = root;
        eventHandlersOnBranch.addAll(node.getEventHandlers());

        for (String pathNode : PathBuilder.decomposePath(path)) {
            node = node.getChild(pathNode, create);
            if (node == null)
                return new FileSystemWalkResults(null, eventHandlersOnBranch);
            eventHandlersOnBranch.addAll(node.getEventHandlers());
        }

        return new FileSystemWalkResults(node, eventHandlersOnBranch);
    }

    @Override
    public String subscribe(Consumer<String> eventHandler, String... path) {
        return fileSystemWalk(true, path).fileNode.addEventHandler(eventHandler);
    }

    @Override
    public void unsubscribe(String eventHandlerId, String... path) {
        Optional.of(fileSystemWalk(false, path).fileNode).ifPresent(n -> n.removeEventHandler(eventHandlerId));
    }

    @Override
    public void sendMessage(Object data, String... path) {
        FileSystemWalkResults r = fileSystemWalk(true, path);

        r.fileNode.setData(data);

        for (Consumer<String> eventHandler : r.eventHandlersOnBranch)
            eventHandler.accept(data + "");// TODO: should be path+data; TODO:
                                           // should be on a new thread later
    }

    @Override
    public <T> T getData(String... path) {
        return fileSystemWalk(false, path).fileNode == null ? null : fileSystemWalk(false, path).fileNode.getData();
    }

    @Override
    public Collection<String> getChildren(String... path) {
        return fileSystemWalk(false, path).fileNode == null ? null
                        : fileSystemWalk(false, path).fileNode.getChildrenNames();
    }
}
