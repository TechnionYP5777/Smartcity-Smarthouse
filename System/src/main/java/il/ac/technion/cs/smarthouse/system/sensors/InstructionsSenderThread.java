package il.ac.technion.cs.smarthouse.system.sensors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.networking.messages.Message;
import il.ac.technion.cs.smarthouse.networking.messages.MessageType;
import il.ac.technion.cs.smarthouse.networking.messages.SensorMessage;
import il.ac.technion.cs.smarthouse.networking.messages.SensorMessage.IllegalMessageBaseExecption;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.file_system.PathBuilder;

/**
 * An instructions sender thread is a class that allows sending instructions
 * from the system to a specific sensor.
 * 
 * @author Elia Traore
 * @author Yarden
 * @since 30.3.17
 */
public class InstructionsSenderThread extends SensorManagingThread {
    private static Logger log = LoggerFactory.getLogger(InstructionsSenderThread.class);

    public InstructionsSenderThread(Socket client, FileSystem fs) {
        super(client, fs);
    }

    @Override
    protected void handleSensorMessage(SensorMessage msg) {
        if (MessageType.REGISTRATION.equals(msg.getType())){
            for(String path : msg.getInstructionRecievingPaths()){
                filesystem.subscribe((p, data)->out.println(p+" "+data), 
                                    FileSystemEntries.LISTENERS_OF_SENSOR.buildPath(msg.getSensorCommName(),msg.getSensorId()));
            }
            try {
                new SensorMessage(MessageType.SUCCESS_ANSWER).send(out, null);
            } catch (IllegalMessageBaseExecption e) {}
        }else{
            log.error(getClass()+" object shouldn't receive an update msg.");
        }
    }
}
