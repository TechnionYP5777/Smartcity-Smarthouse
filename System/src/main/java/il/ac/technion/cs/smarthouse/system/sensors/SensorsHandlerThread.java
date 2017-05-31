package il.ac.technion.cs.smarthouse.system.sensors;

import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.networking.messages.Message;
import il.ac.technion.cs.smarthouse.networking.messages.MessageType;
import il.ac.technion.cs.smarthouse.system.DatabaseHandler;
import il.ac.technion.cs.smarthouse.system.Dispatcher;

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
public class SensorsHandlerThread extends SensorManagingThread {
    private static Logger log = LoggerFactory.getLogger(SensorsHandlerThread.class);

    public SensorsHandlerThread(final Socket client, final DatabaseHandler databaseHandler) {
        super(client, databaseHandler);
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
    
    private void handleRegisterMessage(final String ¢) {
        final String[] parsedMessage = ¢.split("\\" + Dispatcher.DELIMITER);

        databaseHandler.addSensor(parsedMessage[1].replaceAll(Message.SENSOR_ID, ""));
        log.info("\n\n" + parsedMessage[1] + "\n\n");
        Message.send(Message.createMessage(MessageType.ANSWER, MessageType.SUCCESS), out, null);

    }

    private void handleUpdateMessage(final String m) {
        databaseHandler.handleUpdateMessage(m);

    }

}

