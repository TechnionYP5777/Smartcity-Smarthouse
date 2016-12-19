package il.ac.technion.cs.eldery.system.sensors;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

import il.ac.technion.cs.eldery.networking.messages.AnswerMessage;
import il.ac.technion.cs.eldery.networking.messages.Message;
import il.ac.technion.cs.eldery.networking.messages.MessageFactory;
import il.ac.technion.cs.eldery.networking.messages.RegisterMessage;
import il.ac.technion.cs.eldery.networking.messages.UpdateMessage;
import il.ac.technion.cs.eldery.system.DatabaseHandler;
import il.ac.technion.cs.eldery.system.exceptions.SensorNotFoundException;
import il.ac.technion.cs.eldery.utils.Table;
import il.ac.technion.cs.eldery.networking.messages.AnswerMessage.Answer;

public class SensorsHandler implements Runnable {
    private DatabaseHandler databaseHandler;
    
    public SensorsHandler(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    @Override public void run() {
        try (DatagramSocket socket = new DatagramSocket(100)) {
            byte[] buffer = new byte[2048];

            for (DatagramPacket packet = new DatagramPacket(buffer, buffer.length);;) {
                socket.receive(packet);
                Message message = MessageFactory.create(new String(buffer, 0, packet.getLength()));

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
        } catch (IOException ¢) {
            ¢.printStackTrace();
        }
    }

    private void handleRegisterMessage(DatagramPacket p, RegisterMessage ¢) {
        databaseHandler.addSensor(¢.getSensor().getId(), 100);

        new AnswerMessage(Answer.SUCCESS).send(p.getAddress().getHostAddress(), p.getPort(), false);
    }

    private void handleUpdateMessage(UpdateMessage m) {
        try {
            databaseHandler.getTable(m.getSensor().getId()).addEntry(m.getData());
        } catch (SensorNotFoundException e) {
            e.printStackTrace();
        }
    }
}
