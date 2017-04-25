package il.ac.technion.cs.smarthouse.system.sensors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import il.ac.technion.cs.smarthouse.networking.messages.AnswerMessage;
import il.ac.technion.cs.smarthouse.networking.messages.AnswerMessage.Answer;
import il.ac.technion.cs.smarthouse.networking.messages.Message;
import il.ac.technion.cs.smarthouse.networking.messages.MessageFactory;
import il.ac.technion.cs.smarthouse.networking.messages.RegisterMessage;
import il.ac.technion.cs.smarthouse.networking.messages.UpdateMessage;
import il.ac.technion.cs.smarthouse.sensors.SensorType;
import il.ac.technion.cs.smarthouse.system.DatabaseHandler;
import il.ac.technion.cs.smarthouse.system.exceptions.SensorNotFoundException;

/** A sensors handler thread is a class that handles a specific connection with
 * a sensor. The class can parse the different incoming messages and act
 * accordingly.
 * @author Yarden
 * @since 24.12.16 */
public class SensorsHandlerThread extends Thread {
    private static Logger log = LoggerFactory.getLogger(SensorsHandlerThread.class);

    private final Socket client;
    private final DatabaseHandler databaseHandler;
    private TypeHandler typeHandler;

    public SensorsHandlerThread(final Socket client, final DatabaseHandler databaseHandler, final TypeHandler typeHandler) {
        this.client = client;
        this.databaseHandler = databaseHandler;
        this.typeHandler = typeHandler;
    }

    @Override public void run() {
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            for (String input = in.readLine(); input != null;) {
                final Message message = MessageFactory.create(input);
                if (message == null) {
                    new AnswerMessage(Answer.FAILURE).send(out, null);
                    continue;
                }
                log.info("Received message: " + message + "\n");
                switch (message.getType()) {
                    case REGISTRATION:
                        handleRegisterMessage(out, (RegisterMessage) message);
                        break;
                    case UPDATE:
                        handleUpdateMessage((UpdateMessage) message);
                        break;
                    default:
                }
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

    private void handleRegisterMessage(final PrintWriter out, final RegisterMessage ¢) {
        databaseHandler.addSensor(¢.sensorId, ¢.sensorCommName, 100);
        new AnswerMessage(Answer.SUCCESS).send(out, null);
        typeHandler.accept(¢.sensorType);
    }

    private void handleUpdateMessage(final UpdateMessage m) {
        final JsonObject json = new JsonObject();
        m.getData().entrySet().forEach(entry -> json.addProperty(entry.getKey(), entry.getValue()));

        try {
            databaseHandler.getList(m.sensorId).add(json + "");
        } catch (final SensorNotFoundException e) {
            log.debug("Failed to store data, no matching sensor was found", e);
        }
    }

}

interface TypeHandler {
    void accept(SensorType t);
}
