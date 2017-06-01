package il.ac.technion.cs.smarthouse.system.sensors;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.networking.messages.Message;
import il.ac.technion.cs.smarthouse.networking.messages.MessageType;
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
 * @author Inbal Zukerman
 * @since 24.12.16
 */
public class SensorsHandlerThread extends SensorManagingThread { //todo: change to SensorHandlerThread
    private static Logger log = LoggerFactory.getLogger(SensorsHandlerThread.class);

    public SensorsHandlerThread(final Socket client, final FileSystem fs) {
        super(client, fs);
    }

    @Override
    protected void processInputLine(String input) {
        if (input == "") {
            final String answerMessage = Message.createMessage(MessageType.ANSWER, MessageType.FAILURE);
            Message.send(answerMessage, out, null);
            return;
        }
        
//        log.info("Received message: " + input + "\n");

        if (Message.isInMessage(input, MessageType.REGISTRATION.toString()))
            handleRegisterMessage(input);
        else if (Message.isInMessage(input, MessageType.UPDATE.toString()))
            handleUpdateMessage(input);
        else
            log.warn("message could not be parsed");
    }
    
    private void handleRegisterMessage(final String msg) {
        //Assumed format:  registration.sensorid-<sensorId>
        final String[] parsedMessage = msg.split(PathBuilder.SPLIT_REGEX);

        Message.send(Message.createMessage(MessageType.ANSWER, MessageType.SUCCESS), out, null);
    }

    private void handleUpdateMessage(final String m) {
        log.info("\n******\n"+Thread.currentThread().getStackTrace()[1]+" "+m+"\n******");
        final String[] parsedMessage = m.split(PathBuilder.SPLIT_REGEX);
        List<String> path = new ArrayList<>();
        path.add(FileSystemEntries.SENSORS_DATA.toString());
        path.add(parsedMessage[0].replace((MessageType.UPDATE.toString() + PathBuilder.DELIMITER).toLowerCase(), ""));

        filesystem.sendMessage(parsedMessage[1], PathBuilder.buildPath(path));

    }

}
