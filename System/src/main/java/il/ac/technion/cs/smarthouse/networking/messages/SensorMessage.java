package il.ac.technion.cs.smarthouse.networking.messages;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import il.ac.technion.cs.smarthouse.sensors.Sensor;
import il.ac.technion.cs.smarthouse.system.sensors.SensorHandlerThread;

/**
 * @author Elia Traore
 * @since 01.06.17
 * */
public class SensorMessage extends Message {
    private static Logger log = LoggerFactory.getLogger(SensorMessage.class);
    public enum PathType {
        INFO_SENDING("INFO_SENDING"), 
        INSTRUCTION_RECEIVING("INSTRUCTION_RECEIVING");
        
        private final String type;
        private PathType(String type){
            this.type = type;
        }
        
        public static PathType fromString(final String from){
            final String fromLower = from.toLowerCase();
            List<PathType> $ = Arrays.asList(PathType.values())
                                    .stream()
                                    .filter(mt -> mt.type.equals(fromLower))
                                    .collect(Collectors.toList());
            $.add(null);
            return $.get(0);
        }
    }
    public class IllegalMessageBaseExecption extends Exception{
        
        public IllegalMessageBaseExecption(MessageType type){
            super("Tried to create "+type+" message, without sensor information.");
        }
        
        public IllegalMessageBaseExecption(MessageType type, Sensor sensor){
            super("Tried to create "+type+" message, with unneeded information:\n"+sensor+".");
        }
        
        public IllegalMessageBaseExecption(String json){
            super("Tried to create message object from json:\n"+json+"\nbut failed.");
        }
    }
    
    private static final List<MessageType> answerTypes = Arrays.asList(MessageType.SUCCESS_ANSWER, MessageType.FAILURE_ANSWER);
    private static final List<MessageType> noneAnswerTypes = Arrays.asList(MessageType.REGISTRATION, MessageType.UPDATE);
    
    private final String sensorId, commName;
    private Map<String, String> data;
    
    /** initialize a message of successful/failed answer
     * @throws IllegalMessageBaseExecption if the given type is MessageType.REGISTRATION or MessageType.UPDATE
     * */
    public SensorMessage(MessageType type) throws IllegalMessageBaseExecption {
        super(type);
        if(noneAnswerTypes.contains(type))
            throw new IllegalMessageBaseExecption(type);
        sensorId = null;
        commName = null;
        data = null;
    }
    
    /** initialize a message of sensor registration or data update
     * @throws IllegalMessageBaseExecption if the given type is MessageType.SUCCESS_ANSWER or MessageType.FAILURE_ANSWER
     * */
    public SensorMessage(MessageType type, Sensor sensor) throws IllegalMessageBaseExecption {
        super(type);
        if(answerTypes.contains(type))
            throw new IllegalMessageBaseExecption(type, sensor);
        sensorId = sensor.getId();
        commName = sensor.getCommname();
        data = new HashMap<>();
        
        if(MessageType.UPDATE.equals(type))
            return;
        
        Map<PathType, List<String>> $ = new HashMap<>();
        $.put(PathType.INFO_SENDING, sensor.getObservationSendingPaths());
        $.put(PathType.INSTRUCTION_RECEIVING, sensor.getInstructionRecievingPaths());
        for(PathType pathType : PathType.values())
            Optional.ofNullable($.get(pathType))
            .map(l -> l.stream())
            .ifPresent(paths -> paths.forEach(path -> data.put(path, pathType+"")) );
        log.info("created msg:"+toJson());
        
    }

    /** initialize a message from json 
     * @throws IllegalMessageBaseExecption if the message interpretation fails
     * */
    public SensorMessage(String json) throws IllegalMessageBaseExecption {
        SensorMessage msg;
        try{
            msg = new Gson().fromJson(json, SensorMessage.class);
        }catch (JsonSyntaxException e){
            throw new IllegalMessageBaseExecption(json);
        }
        this.type = msg.getType();
        this.sensorId = msg.getSensorId();
        this.commName = msg.getSensorCommName();
        this.data = msg.getData();
    }

    
    @Override public String toString() {
        return "SensorMessage: " +toJson();
    }

    
    public String getSensorId() {
        return sensorId;
    }

    public String getSensorCommName() {
        return commName;
    }
    
    private List<String> getPaths(PathType pathType){
        return data.keySet().stream().filter(
                        path -> data.get(path).equals(pathType+"")).collect(Collectors.toList());
    }

    public List<String> getObservationSendingPaths() {
        return getPaths(PathType.INFO_SENDING);
    }

    public List<String> getInstructionRecievingPaths() {
        return getPaths(PathType.INSTRUCTION_RECEIVING);
    }
    
    public Map<String, String> getData(){
        return data;
    }
    
    public Boolean isSuccesful(){
        return answerTypes.contains(type) ? MessageType.SUCCESS_ANSWER.equals(type) : null ;
    }
    
    public SensorMessage setData(Map<String,String> data){
        this.data = data;
        return this;
    }
}
