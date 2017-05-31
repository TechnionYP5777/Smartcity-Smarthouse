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
import il.ac.technion.cs.smarthouse.system.file_system.PathBuilder;


/**
 * A sensors handler thread is a class that handles a specific connection with a
 * sensor. The class can parse the different incoming messages and act
 * accordingly.
 * 
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
    public void run() {
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            for (String input = in.readLine(); input != null;) {

                if (input == "") {
                    final String answerMessage = Message.createMessage(MessageType.ANSWER, MessageType.FAILURE);
                    Message.send(answerMessage, out, null);

                    continue;
                }
                log.info("Received message: " + input + "\n");

                if (Message.isInMessage(input, MessageType.REGISTRATION.toString()))
                    handleRegisterMessage(out, input);
                else if (Message.isInMessage(input, MessageType.UPDATE.toString()))
                    handleUpdateMessage(input);
                else
                    log.warn("message could not be parsed");

                input = in.readLine();
            }
        } catch (final IOException e) {
            log.error("I/O error occurred", e);
        } finally {
            try {
                if (out != null)
                    out.close();

                if (in != null)
                    in.close();
            } catch (final IOException e) {
                log.error("I/O error occurred while closing", e);
            }
        }
    }

    private void handleRegisterMessage(final PrintWriter out, final String ¢) {
        // TODO inbal
        final String[] parsedMessage = ¢.split(PathBuilder.SPLIT_REGEX);

        databaseHandler.addSensor(parsedMessage[1].replaceAll(Message.SENSOR_ID, ""));
        log.info("\n\n" + parsedMessage[1] + "\n\n");
        Message.send(Message.createMessage(MessageType.ANSWER, MessageType.SUCCESS), out, null);

    }

    private void handleUpdateMessage(final String m) {
        databaseHandler.handleUpdateMessage(m);

    }

}

