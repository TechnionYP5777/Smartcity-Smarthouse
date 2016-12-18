package il.ac.technion.cs.eldery.system;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import il.ac.technion.cs.eldery.system.sensors.SensorInfo;
import il.ac.technion.cs.eldery.utils.ListenableTable;
import il.ac.technion.cs.eldery.utils.Table;


/**
 * The API required by ApplicationHandler in order to allow it desired functionalities.
 * @author Elia
 * @author Inbal Zukerman
 * @since Dec 13, 2016
 */
public class DatabaseHandler {
    
    private Map<String, ListenableTable<String, String>> sensors = new HashMap<>();
    
    
    public void addSensor(String sensorID, int sizeLimit){
        this.sensors.put(sensorID, new ListenableTable<String, String>( sizeLimit ));
    }
    
    /** Adds a listener to a certain sensor, to be called on <strong>any</strong> update from that sensor
     *  @param sensorCommercialName The name of sensor, agreed upon in an external platform
     *  @param notifee The consumer to be called on a change, with the new data
     *  @return The id of the listener, to be used in any future reference to it
     * */
    public String addListener(String sensorID, Consumer<Table<String, String>> notifee){
        this.sensors.get(sensorID).addListener(notifee);
        
        return null; //TODO
    }
    
    /** Remove a previously added listener
     * @param listenerId The id given when the listener was added to the system
     * */
    public void removeListener(String listenerId){
        //TODO
    }
    
    /**Queries the info of a sensor. 
     * @param sensorCommercialName The name of sensor, agreed upon in an external platform
     * @return the most updated data of the sensor, or Optional.empty() if the request couldn't be completed for any reason
     * */
    public Optional<Table<String, String>> getLastEntryOf(final String sensorID){
        return null; //TODO
    }
    
    public Table<String, String> getTable(String sensorID){
        return null; //TODO
    }
}
