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

/** An instructions sender thread is a class that allows sending instructions
 * from the system to a specific sensor.
 * @author Yarden
 * @since 30.3.17 */
public class InstructionsSenderThread extends Thread {
    private final Socket client;
    private StoreThread storage;

    public InstructionsSenderThread(final Socket client, final StoreThread storage) {
        this.client = client;
        this.storage = storage;
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
                    new AnswerMessage(Answer.FAILURE).send(out, null);
                    continue;
                }
                System.out.println("Received message: " + message + "\n");
                switch (message.getType()) {
                    case REGISTRATION:
                        handleRegisterMessage(out, (RegisterMessage) message);
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
        storage.store(¢.sensorId, out);
        new AnswerMessage(Answer.SUCCESS).send(out, null);
    }
}

interface StoreThread {
    void store(String id, PrintWriter out);
}
