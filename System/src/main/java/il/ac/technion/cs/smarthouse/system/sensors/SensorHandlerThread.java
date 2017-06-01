package il.ac.technion.cs.smarthouse.system.sensors;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.networking.messages.AnswerMessage;
import il.ac.technion.cs.smarthouse.networking.messages.Message;
import il.ac.technion.cs.smarthouse.networking.messages.MessageFactory;
import il.ac.technion.cs.smarthouse.networking.messages.MessageType;
import il.ac.technion.cs.smarthouse.networking.messages.RegisterMessage;
import il.ac.technion.cs.smarthouse.networking.messages.UpdateMessage;
import il.ac.technion.cs.smarthouse.networking.messages.AnswerMessage.Answer;
import il.ac.technion.cs.smarthouse.system.SensorLocation;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.file_system.PathBuilder;

/**
 * A sensors handler thread is a class that handles a specific connection with a
 * sensor. The class can parse the different incoming messages and act
 * accordingly.
 * 
 * @author Elia Traore
 * @author Yarden
 * @since 24.12.16
 */
public class SensorHandlerThread extends SensorManagingThread { //todo: change to SensorHandlerThread
    private static Logger log = LoggerFactory.getLogger(SensorHandlerThread.class);
    private Map<String, String> legalSystemPaths = new HashMap<>();//short path & full path
    private Map<String, Integer> dataStatsCalculator = new HashMap<>(); // short path & counter
    private String donePath;
    
    public SensorHandlerThread(final Socket client, final FileSystem fs) {
        super(client, fs);
    }
    
    @Override protected void handleRegisterMessage(final RegisterMessage msg) {
        filesystem.sendMessage(SensorLocation.UNDEFINED, 
                                FileSystemEntries.LOCATION.buildPath(msg.getSensorCommName(),msg.getSensorId()));

        donePath = FileSystemEntries.DONE_SENDING_MSG.buildPath(msg.getSensorCommName(),msg.getSensorId());
        msg.getObservationSendingPaths()
            .stream()
            .forEach(p -> legalSystemPaths.put(p, FileSystemEntries.SENSORS_DATA.buildPath(p ,msg.getSensorId())));
        msg.getObservationSendingPaths()
            .stream()
            .forEach(p -> dataStatsCalculator.put(p, 0));
        
        new AnswerMessage(Answer.SUCCESS).send(out, null);
    }
    
    private void updateDonePathIfNeeded(String path, String id){
        Integer $ = dataStatsCalculator.getOrDefault(path, 0);
        dataStatsCalculator.put(path, $+1);
        
        $ = dataStatsCalculator.values().stream().mapToInt(x->x).sum();
        if($ < (legalSystemPaths.size()))
            return;

        //enough to notify done
        filesystem.sendMessage(null, donePath);//todo: null is legit?
        log.info("sent done on: "+donePath);
        
        if( $ > legalSystemPaths.size() && $ % legalSystemPaths.size() != 0)
            log.warn("Might have missed a path update of sensor "+ id+ ". Current stats:"+ dataStatsCalculator);
        dataStatsCalculator.keySet().stream().forEach(k -> dataStatsCalculator.put(k, 0));
    }
    
    @Override protected void handleUpdateMessage(final UpdateMessage msg) {
        for(String path : msg.getData().keySet()){
            filesystem.sendMessage(msg.getData().get(path), legalSystemPaths.get(path));
            log.info("sent: "+msg.getData().get(path)+" on path: "+legalSystemPaths.get(path));
            updateDonePathIfNeeded(path, msg.getSensorId());
        }

    }

}
