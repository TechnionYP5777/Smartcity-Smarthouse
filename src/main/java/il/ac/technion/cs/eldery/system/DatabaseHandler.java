package il.ac.technion.cs.eldery.system;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import il.ac.technion.cs.eldery.system.applications.SamplesTable;
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
    
    private Map<String, ListenableTable> sensors = new HashMap<>();
    
    
    public void addSensor(String sensorID, int sizeLimit){
        this.sensors.put(sensorID, new ListenableTable<>());
    }
    
    /** Adds a listener to a certain sensor, to be called on <strong>any</strong> update from that sensor
     *  @param sensorCommercialName The name of sensor, agreed upon in an external platform
     *  @param notifee The consumer to be called on a change, with the new data
     *  @return The id of the listener, to be used in any future refernce to it
     * */
    public String addListener(String sensorID, Consumer<SamplesTable> notifee){
        
    }
    
    /** Remove a previously added listener
     * @param listenerId The id given when the listener was added to the system
     * */
    public void removeListener(String listenerId){
        
    }
    
    /**Queries the info of a sensor. 
     * @param sensorCommercialName The name of sensor, agreed upon in an external platform
     * @return the most updated data of the sensor, or Optional.empty() if the request couldn't be completed for any reason
     * */
    public Optional<SamplesTable> getLastEntryOf(final String sensorID){
        
    }
    
    public Table<String, String> getTable(){
        
    }
}
