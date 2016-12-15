package il.ac.technion.cs.eldery.system.sensors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import il.ac.technion.cs.eldery.utils.Table;

/**
 * 
 * @author Inbal Zukerman
 * @since 16.12.2016
 */
public class SensorInfo{
    
    private String sensorId;
    private Table<Object, Object> information = new Table<>();
    List<Consumer<HashMap<Object, Object>>> listeners = new ArrayList<>();
    
    public SensorInfo(String sensorId) {
        this.sensorId = sensorId;
    }
    
    public SensorInfo(String sensorId, int infoLimit) {
        this.sensorId = sensorId;
        this.information.changeMaxCapacity(infoLimit);
    }
    
    private SensorInfo(String sensorId, Table<Object, Object> information){
        this.sensorId = sensorId;
        this.information = information;
    }
    
    public void addListener(Consumer<HashMap<Object, Object>> listener){
        this.listeners.add(listener);
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }
    
    public void addRecord(HashMap<Object, Object> record){
        this.information.addEntry(record);
    }
    
    public int getIfoLimit(){
        return this.information.getMaxCapacity();
    }
    
    public int getNumRecords(){
        return this.information.getCurrentCapacity();
    }
    
    public void changeInfoLimit(int newLimit){
        this.information.changeMaxCapacity(newLimit);
    }
    
    public void unlimitInfo(){
        this.information.disableCapacityLimit();
    }
    
    public SensorInfo getLastKRecords (int k){
        return new SensorInfo(this.sensorId, this.information.receiveKLastEntries(k));
    }
    
    public Map<Object, Object> getLastRecord(){
        return this.information.getLastEntry();
    }
   
    public Object getLastRecordAtCol(Object colName){
        return this.information.getLastEntryAtCol(colName);
    }
}
