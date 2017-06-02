package il.ac.technion.cs.smarthouse.system.file_system;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;

import il.ac.technion.cs.smarthouse.system.Savable;
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
public class FileSystemImpl implements FileSystem, Savable {
    private static final Logger log = LoggerFactory.getLogger(FileSystemImpl.class);

    static class FileNode implements Savable {
        @Expose private final String myName;
        @Expose private Object data;
        @Expose private Object mostRecentDataOnBranch;
        private final Map<String, BiConsumer<String, Object>> eventHandlers = new HashMap<>();
        @Expose Map<String, FileNode> children = new HashMap<>();

        public FileNode(final String name) {
            myName = name;
        }

        @SuppressWarnings("unchecked")
        <T> T getData() {
            return (T) data;
        }

        @SuppressWarnings("unchecked")
        public <T> T getMostRecentDataOnBranch() {
            return (T) mostRecentDataOnBranch;
        }

        public void setMostRecentDataOnBranch(final Object mostRecentDataOnBranch) {
            this.mostRecentDataOnBranch = mostRecentDataOnBranch;
        }

        public void setData(final Object data) {
            this.data = data;
        }

        public Collection<String> getChildrenNames() {
            return children.keySet();
        }

        public FileNode getChild(final String name, final boolean create) {
            if (!children.containsKey(name) && create)
                children.put(name, new FileNode(name));
            return children.get(name);
        }

        String addEventHandler(final BiConsumer<String, Object> eventHandler) {
            final String id = UuidGenerator.GenerateUniqueIDstring();
            eventHandlers.put(id, eventHandler);
            return id;
        }

        void removeEventHandler(final String eventHandlerId) {
            eventHandlers.remove(eventHandlerId);
        }

        public Collection<BiConsumer<String, Object>> getEventHandlers() {
            return eventHandlers.values();
        }

        private void print(final int depth, final PrintWriter w) {
            for (int i = 0; i < depth; ++i)
                w.print("\t");
            w.println("[" + myName + ", " + data + ", " + eventHandlers.size() + "]");
            for (final FileNode child : children.values())
                child.print(depth + 1, w);
        }

        @Override
        public String toString() {
            final StringWriter writer = new StringWriter();
            print(0, new PrintWriter(writer));
            return writer.toString();
        }

        @Override
        public void populate(final String jsonString) throws Exception {
            for (final Entry<String, JsonElement> e : new JsonParser().parse(jsonString).getAsJsonObject().entrySet()) {
                final Field f = getClass().getDeclaredField(e.getKey());
                f.setAccessible(true);

                if (!"children".equals(f.getName()))
                    f.set(this, gsonBuilder.create().fromJson(e.getValue(), f.getGenericType()));
                else
                    for (final Entry<String, JsonElement> e2 : e.getValue().getAsJsonObject().entrySet())
                        getChild(e2.getKey(), true).populate(e2.getValue().toString());
            }
        }
    }

    private class FileSystemWalkResults {
        FileNode fileNode;
        List<BiConsumer<String, Object>> eventHandlersOnBranch;

        FileSystemWalkResults(final FileNode fileNode, final List<BiConsumer<String, Object>> eventHandlersOnBranch) {
            this.fileNode = fileNode;
            this.eventHandlersOnBranch = eventHandlersOnBranch;
        }
    }

    @Expose private final FileNode root = new FileNode("<ROOT>");
    private final Map<String, FileNode> listenersBuffer = new HashMap<>();

    /**
     * Performs a walk on the file system tree
     * 
     * @param create
     *            If true, missing nodes from the path, will be created.<br>
     *            If false, and a node on the path is missing, then null will be
     *            returned as the result for the FileNode
     * @param newDataToAdd
     *            If not null, the new data will be added to the last node on
     *            the path. <br>
     *            Also, the mostRecentDataOnBranch will be updated. <br>
     *            If null, nothing will happen to the nodes' data
     * @param path
     *            The path to walk on
     * @return The results of the walk:<br>
     *         The FileNode at the end of the walk, and all of the eventHandlers
     *         on path (including the last node)
     */
    private FileSystemWalkResults fileSystemWalk(final boolean create, final Object newDataToAdd,
                    final String... path) {
        final List<BiConsumer<String, Object>> eventHandlersOnBranch = new ArrayList<>();

        FileNode node = root;

        eventHandlersOnBranch.addAll(node.getEventHandlers());

        if (newDataToAdd != null)
            node.setMostRecentDataOnBranch(newDataToAdd);

        for (final String pathNode : PathBuilder.decomposePath(path)) {
            node = node.getChild(pathNode, create);

            if (node == null)
                return new FileSystemWalkResults(null, eventHandlersOnBranch);

            if (newDataToAdd != null)
                node.setMostRecentDataOnBranch(newDataToAdd);

            eventHandlersOnBranch.addAll(node.getEventHandlers());
        }

        if (newDataToAdd != null)
            node.setData(newDataToAdd);

        return new FileSystemWalkResults(node, eventHandlersOnBranch);
    }

    @Override
    public String subscribe(BiConsumer<String, Object> eventHandler, String... path) {
        log.info("FileSystem: subscribed on " + PathBuilder.buildPath(path) + " | Subscriber is " + new Throwable().getStackTrace()[1].getClassName());
        FileNode n = fileSystemWalk(true, null, path).fileNode;
        String id = n.addEventHandler(eventHandler);
        listenersBuffer.put(id, n);
        return id;
    }

    @Override
    public void unsubscribe(final String eventHandlerId) {
        Optional.of(listenersBuffer.get(eventHandlerId)).ifPresent(n -> n.removeEventHandler(eventHandlerId));
    }

    @Override
    public void sendMessage(Object data, String... path) {
        for (BiConsumer<String, Object> eventHandler : fileSystemWalk(true, data, path).eventHandlersOnBranch) {
            log.info("FileSystem: sending message on " + PathBuilder.buildPath(path) + " | Sender is " + new Throwable().getStackTrace()[1].getClassName());
            eventHandler.accept(PathBuilder.buildPath(path), data);
        }
    }

    @Override
    public <T> T getData(final String... path) {
        return fileSystemWalk(false, null, path).fileNode == null ? null
                        : fileSystemWalk(false, null, path).fileNode.getData();
    }

    @Override
    public <T> T getMostRecentDataOnBranch(final String... path) {
        return fileSystemWalk(false, null, path).fileNode == null ? null
                        : fileSystemWalk(false, null, path).fileNode.getMostRecentDataOnBranch();
    }

    @Override
    public Collection<String> getChildren(final String... path) {
        return fileSystemWalk(false, null, path).fileNode == null ? Collections.emptyList()
                        : fileSystemWalk(false, null, path).fileNode.getChildrenNames();
    }

    @Override
    public boolean wasPathInitiated(final String... path) {
        return fileSystemWalk(false, null, path).fileNode != null;
    }

    @Override
    public String toString() {
        return "Total number of listeners: " + listenersBuffer.size() + "\n" + root.toString();
    }

    public String toString(final String... pathToFirstNode) {
        return Optional.ofNullable(fileSystemWalk(false, null, pathToFirstNode).fileNode).orElse(root).toString();
    }

    public void deleteFromPath(final String... path) {
        Optional.ofNullable(fileSystemWalk(false, null, path).fileNode).ifPresent(n -> n.children.clear());
    }
}
