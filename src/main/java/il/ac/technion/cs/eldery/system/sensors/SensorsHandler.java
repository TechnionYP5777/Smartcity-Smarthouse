package il.ac.technion.cs.eldery.system.sensors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import il.ac.technion.cs.eldery.networking.messages.AnswerMessage;
import il.ac.technion.cs.eldery.networking.messages.AnswerMessage.Answer;
import il.ac.technion.cs.eldery.networking.messages.Message;
import il.ac.technion.cs.eldery.networking.messages.MessageFactory;
import il.ac.technion.cs.eldery.networking.messages.RegisterMessage;
import il.ac.technion.cs.eldery.networking.messages.UpdateMessage;
import il.ac.technion.cs.eldery.system.DatabaseHandler;
import il.ac.technion.cs.eldery.system.exceptions.SensorNotFoundException;

/** A sensors handler is a class dedicated to listening for incoming messages
 * from sensors. The class can parse the different message and handle them
 * accordingly.
 * @author Sharon
 * @author Yarden
 * @since 17.12.16 */
public class SensorsHandler implements Runnable {
    private final DatabaseHandler databaseHandler;

    /** Initializes a new sensors handler object.
     * @param databaseHandler database handler of the system */
    public SensorsHandler(final DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    @Override public void run() {
        try (ServerSocket server = new ServerSocket(40000);
                Socket client = server.accept();
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));) {
            for (String input = in.readLine(); input != null;) {
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
        }
    }

    private void handleRegisterMessage(final PrintWriter out, final RegisterMessage ¢) {
        databaseHandler.addSensor(¢.getSensor().getId(), ¢.getSensor().getCommName(), 100);

        out.println(new AnswerMessage(Answer.SUCCESS).toJson());
    }

    private void handleUpdateMessage(final UpdateMessage m) {
        try {
            databaseHandler.getTable(m.sensorId).addEntry(m.getData());
        } catch (@SuppressWarnings("unused") final SensorNotFoundException ¢) {
            // ¢.printStackTrace();
        }
    }
}
