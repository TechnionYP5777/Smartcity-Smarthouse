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
import il.ac.technion.cs.smarthouse.system.DatabaseHandler;
import il.ac.technion.cs.smarthouse.system.Dispatcher;

/**
 * An instructions sender thread is a class that allows sending instructions
 * from the system to a specific sensor.
 * 
 * @author Elia Traore
 * @author Yarden
 * @author Inbal Zukerman
 * @since 30.3.17
 */
public class InstructionsSenderThread extends SensorManagingThread {
    static OutputMapper mapper;
    
    public static void setMapper(OutputMapper m){mapper = m;}
    
    public InstructionsSenderThread(Socket client, DatabaseHandler databaseHandler) {
        super(client, databaseHandler);
    }

    private static Logger log = LoggerFactory.getLogger(InstructionsSenderThread.class);

    @Override
    protected void processInputLine(String input) {
        if (input == "") {
            final String answerMessage = Message.createMessage(MessageType.ANSWER, MessageType.FAILURE);
            Message.send(answerMessage, out, null);
            return;
        }
        log.info("Received message: " + input + "\n");
        if (input.contains("registration"))
            handleRegisterMessage(input);
        
    }

    private void handleRegisterMessage(final String ¢) {
        String sensorId = ¢.split("\\" + Dispatcher.DELIMITER)[2].replaceAll("sensorid\\-|=", "");//todo: change to constants somehow like Message.SENSOR_ID
        mapper.store(sensorId, out);
        Message.send(Message.createMessage(MessageType.ANSWER, MessageType.SUCCESS), out, null);
    }
}
