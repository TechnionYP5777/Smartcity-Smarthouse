/**
 * 
 */
package il.ac.technion.cs.smarthouse.system.file_system;

import java.util.function.Consumer;

import org.parse4j.ParseException;
import org.parse4j.ParseObject;

import il.ac.technion.cs.smarthouse.database.DatabaseAPI;
import il.ac.technion.cs.smarthouse.system.Dispatcher;
import il.ac.technion.cs.smarthouse.system.InfoType;

/**
 * @author RON
 * @since 28-05-2017
 */
public class FileSystem implements DatabaseAPI, Dispatcher {

    @Override
    public String subscribe(Consumer<String> subscriber, String... path) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void unsubscribe(String subscriberId, String... path) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void sendMessage(InfoType infoType, String value, String... path) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ParseObject addInfo(String info) throws ParseException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteInfo(InfoType infoType) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteInfo(String... path) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getLastEntry(String... path) {
        // TODO Auto-generated method stub
        return null;
    }

}
