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
     * This methods allows to subscribe to new messages on a certain path.
     * 
     * @param path
     *            The path on which the eventHandler would like to listen
     * @param eventHandler
     *            an event handler function: (String path, Object data)->{...}
     * @return The eventHandler's ID in our system (for future use)
     */
    String subscribe(BiConsumer<String, Object> eventHandler, String... path);

    /**
     * This method will allow a subscriber to unsubscribe to a certain path
     * 
     * @param eventHandlerId
     *            The ID of the eventHandler in our system
     * @param path
     *            The path on which the eventHandler would like to stop listen
     */
    void unsubscribe(String eventHandlerId);

    /**
     * This method allows to send a message
     * 
     * @param data
     *            The data to send and store in the file system
     * @param path
     *            The path of the update
     */
    void sendMessage(Object data, String... path);

    /**
     * This method allows to query the last record saved in the file system on a
     * specific path
     * 
     * @param path
     *            The path to find the last entry of
     * @return The last entry's data
     */
    <T> T getData(String... path);
    
    <T> T getMostRecentDataOnBranch(String... path);

    Collection<String> getChildren(String... path);
    
    boolean wasPathInitiated(String... path);
}
