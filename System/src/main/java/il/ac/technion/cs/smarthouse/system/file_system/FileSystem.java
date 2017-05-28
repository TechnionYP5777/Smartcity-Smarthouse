/**
 * 
 */
package il.ac.technion.cs.smarthouse.system.file_system;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.parse4j.ParseException;
import org.parse4j.ParseObject;

import il.ac.technion.cs.smarthouse.database.DatabaseAPI;
import il.ac.technion.cs.smarthouse.system.Dispatcher;
import il.ac.technion.cs.smarthouse.system.InfoType;
import il.ac.technion.cs.smarthouse.system.dispatcher.PathBuilder;
import il.ac.technion.cs.smarthouse.utils.Tuple;
import il.ac.technion.cs.smarthouse.utils.UuidGenerator;

/**
 * @author RON
 * @since 28-05-2017
 */
public class FileSystem {
    
    static class FileNode {
        private Object data;
        private Map<String, Consumer<String>> eventHandlers = new HashMap<>();
        private Map<String, FileNode> children = new HashMap<>();
        
        @SuppressWarnings("unchecked")
        <T> T getData() {
            return (T)data;
        }
        
        public void setData(Object data) {
            this.data = data;
        }
        
        Map<String, FileNode> getChildren() {
            return children; //TODO: RON
        }
        
        String addEventHandler(Consumer<String> eventHandler) {
            final String id = UuidGenerator.GenerateUniqueIDstring();
            eventHandlers.put(id, eventHandler);
            return id;
        }
        
        public Map<String, Consumer<String>> getEventHandlers() {
            return eventHandlers;
        }
    }
    
    private FileNode root = new FileNode();
    
    private Tuple<FileNode, List<Consumer<String>>> getFileNodeByPath(String... path) {
        List<Consumer<String>> eventHandlersOnBranch = new ArrayList<>();
        
        FileNode node = root;
        eventHandlersOnBranch.addAll(node.getEventHandlers().values());
        
        for (String pathNode : PathBuilder.decomposePath(path)) {
            node = node.getChildren().get(pathNode);
            eventHandlersOnBranch.addAll(node.getEventHandlers().values());
        }
        
        return new Tuple<>(node, eventHandlersOnBranch);
    }
    
    
    public String subscribe(Consumer<String> eventHandler, String... path) {
        return getFileNodeByPath(path).left.addEventHandler(eventHandler);
    }
    
    public void unsubscribe(String eventHandlerId, String... path) {
        getFileNodeByPath(path).left.getEventHandlers().remove(eventHandlerId);
    }
    
    public void sendMessage(Object data, String... path) {
        Tuple<FileNode, List<Consumer<String>>> t = getFileNodeByPath(path);
        t.left.setData(data);
        for (Consumer<String> eventHandler : t.right)
            eventHandler.accept(data + "");//TODO: should be path+data; TODO: should be on a new thread later
    }
    
    public <T> T getData(String... path) {
        return getFileNodeByPath(path).left.getData();
    }

}
