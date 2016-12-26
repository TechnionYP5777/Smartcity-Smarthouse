package il.ac.technion.cs.eldery.system.sensors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import il.ac.technion.cs.eldery.networking.messages.AnswerMessage;
import il.ac.technion.cs.eldery.networking.messages.AnswerMessage.Answer;
import il.ac.technion.cs.eldery.networking.messages.Message;
import il.ac.technion.cs.eldery.networking.messages.MessageFactory;
import il.ac.technion.cs.eldery.networking.messages.RegisterMessage;
import il.ac.technion.cs.eldery.networking.messages.UpdateMessage;
import il.ac.technion.cs.eldery.system.DatabaseHandler;
import il.ac.technion.cs.eldery.system.exceptions.SensorNotFoundException;

/** A sensors handler thread is a class that handles a specific connection with
 * a sensor. The class can parse the different incoming messages and act
 * accordingly.
 * @author Yarden
 * @since 24.12.16 */
public class SensorsHandlerThread extends Thread {
    private final Socket client;
    private final DatabaseHandler databaseHandler;

    public SensorsHandlerThread(final Socket client, final DatabaseHandler databaseHandler) {
        this.client = client;
        this.databaseHandler = databaseHandler;
    }

    @Override public void run() {
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            for (String input = in.readLine(); input != null;) {
                System.out.println(input);
                final Message message = MessageFactory.create(input);
                if (message == null) {
                    out.println(new AnswerMessage(Answer.FAILURE).toJson());
                    continue;
                }
                System.out.println("Received message: " + message + "\n");
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
        } catch (final IOException ¢) {
            ¢.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();

                if (in != null)
                    in.close();
            } catch (final IOException ¢) {
                ¢.printStackTrace();
            }
        }
    }

    private void handleRegisterMessage(final PrintWriter out, final RegisterMessage ¢) {
        databaseHandler.addSensor(¢.sensorId, ¢.sensorCommName, 100);
        out.println(new AnswerMessage(Answer.SUCCESS).toJson());
    }

    private void handleUpdateMessage(final UpdateMessage m) {
        try {
            databaseHandler.getList(m.sensorId).add(m.toJson());
        } catch (final SensorNotFoundException ¢) {
            ¢.printStackTrace();
        }
    }

}
