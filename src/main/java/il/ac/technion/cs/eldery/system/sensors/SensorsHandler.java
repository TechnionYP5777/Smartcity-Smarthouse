package il.ac.technion.cs.eldery.system.sensors;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

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
 * @since 17.12.16 */
public class SensorsHandler implements Runnable {
    private final DatabaseHandler databaseHandler;

    /** Initializes a new sensors handler object.
     * @param databaseHandler database handler of the system */
    public SensorsHandler(final DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    @Override public void run() {
        try (DatagramSocket socket = new DatagramSocket(40000)) {
            for (final DatagramPacket packet = new DatagramPacket(new byte[2048], new byte[2048].length);;) {
                socket.receive(packet);
                final Message message = MessageFactory.create(new String(packet.getData(), 0, packet.getLength()));
                if (message == null) {
                    new AnswerMessage(Answer.FAILURE).send(packet.getAddress().getHostAddress(), packet.getPort(), false);
                    continue;
                }
                switch (message.getType()) {
                    case REGISTRATION:
                        handleRegisterMessage(packet, (RegisterMessage) message);
                        break;
                    case UPDATE:
                        handleUpdateMessage((UpdateMessage) message);
                        break;
                    default:
                }
            }
        } catch (final IOException ¢) {
            ¢.printStackTrace();
        }
    }

    private void handleRegisterMessage(final DatagramPacket p, final RegisterMessage ¢) {
        databaseHandler.addSensor(¢.getSensor().getId(), ¢.getSensor().getCommName(), 100);

        new AnswerMessage(Answer.SUCCESS).send(p.getAddress().getHostAddress(), p.getPort(), false);
    }

    private void handleUpdateMessage(final UpdateMessage m) {
        try {
            databaseHandler.getTable(m.sensorId).addEntry(m.getData());
        } catch (final SensorNotFoundException ¢) {
            ¢.printStackTrace();
        }
    }
}
