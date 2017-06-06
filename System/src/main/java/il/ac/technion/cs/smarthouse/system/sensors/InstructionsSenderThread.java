package il.ac.technion.cs.smarthouse.system.sensors;

import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.networking.messages.MessageType;
import il.ac.technion.cs.smarthouse.networking.messages.SensorMessage;
import il.ac.technion.cs.smarthouse.networking.messages.SensorMessage.IllegalMessageBaseExecption;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;

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
    private static String instructionSeperator = "##";

    public InstructionsSenderThread(final Socket client, final FileSystem fs) {
        super(client, fs);
    }

    @Override
    protected void handleSensorMessage(final SensorMessage msg) {
        if (!MessageType.REGISTRATION.equals(msg.getType()))
            log.error(getClass() + " shouldn't receive an update msg.");
        else {
            //todo: ELIA check for waiting instructions first
            for (final String path : msg.getInstructionRecievingPaths())
                filesystem.subscribe((p, data) -> out.println(p + instructionSeperator + data), FileSystemEntries.LISTENERS_OF_SENSOR
                                .buildPath(msg.getSensorCommName(), msg.getSensorId()));
            try {
                new SensorMessage(MessageType.SUCCESS_ANSWER).send(out, null);
            } catch (final IllegalMessageBaseExecption e) {}
        }
    }
    
    public static String getInstructionSeperatorRegex(){
        return instructionSeperator;
    }
}
