package il.ac.technion.cs.smarthouse.system.file_system;

import java.util.Collection;
import java.util.function.BiConsumer;

/**
 * An event handler file system interface
 * <p>
 * This type of file system will allow to store data in entries, and listen for
 * changes on each entry.
 * 
 * @author RON
 * @author Inbal Zukerman
 * @since 28-05-2017
 */
public interface FileSystem {

    /**
     * This methods allows to subscribe a new event handler on a certain path.
     * <br>
     * When the path (or any path under it) is updated, the eventHandler will be
     * executed.
     * 
     * @param path
     *            The path which the eventHandler would like to listen on
     * @param eventHandler
     *            An event handler function: (String full_path, Object
     *            data)->{...}
     * @return The eventHandler's ID of the subscribed function in our system.
     *         Use this ID to unsubscribe the listener with
     *         {@link FileSystem#unsubscribe(String)}
     */
    String subscribe(BiConsumer<String, Object> eventHandler, String... path);
    
    /**
     * Same as {@link #subscribe(BiConsumer, String...)}
     * <br>
     * But here, the eventHandler will be called only of the data is an instance of dataClass
     * @param eventHandler
     * @param dataClass
     * @param path
     * @return
     */
    <T> String subscribe(BiConsumer<String, T> eventHandler, Class<T> dataClass, String... path);

    /**
     * This method will allow to unsubscribe a subscribed eventHandler.
     * 
     * @param eventHandlerId
     *            The ID of the subscribed eventHandler in our system
     */
    void unsubscribe(String eventHandlerId);

    /**
     * This method allows to send a message to the file system.
     * <p>
     * The message's data will be saved on the file system.<br>
     * The relevant eventHandlers will be executed.
     * 
     * @param data
     *            The data to send and store in the file system
     * @param path
     *            The path of the update
     */
    void sendMessage(Object data, String... path);

    /**
     * Get the data from a path.
     * <p>
     * If the path doesn't exists, null will be returned.
     * 
     * @param path
     *            The path
     * @return The data
     */
    <T> T getData(String... path);

    /**
     * Get the most recent data that was saved on the subtree under the given
     * path
     * 
     * @param path
     *            The path to the relevant subtree
     * @return The most recent data that was modified in the subtree
     */
    <T> T getMostRecentDataOnBranch(String... path);

    /**
     * Get the names of all of the existing path-nodes under the given path
     * 
     * @param path
     *            The parent path
     * @return the names of the path's children
     */
    Collection<String> getChildren(String... path);

    /**
     * Check if a path exists
     * 
     * @param path
     *            The path
     * @return true if the path exists, or false otherwise
     */
    boolean wasPathInitiated(String... path);
}
