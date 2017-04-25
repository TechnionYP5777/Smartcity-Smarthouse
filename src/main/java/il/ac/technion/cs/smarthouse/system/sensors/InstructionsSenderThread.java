package il.ac.technion.cs.smarthouse.system.sensors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.networking.messages.AnswerMessage;
import il.ac.technion.cs.smarthouse.networking.messages.AnswerMessage.Answer;
import il.ac.technion.cs.smarthouse.networking.messages.Message;
import il.ac.technion.cs.smarthouse.networking.messages.MessageFactory;
import il.ac.technion.cs.smarthouse.networking.messages.MessageType;
import il.ac.technion.cs.smarthouse.networking.messages.RegisterMessage;

/** An instructions sender thread is a class that allows sending instructions
 * from the system to a specific sensor.
 * @author Yarden
 * @since 30.3.17 */
public class InstructionsSenderThread extends Thread {
    private static Logger log = LoggerFactory.getLogger(InstructionsSenderThread.class);

    private final Socket client;
    private OutputMapper mapper;

    public InstructionsSenderThread(final Socket client, final OutputMapper mapper) {
        this.client = client;
        this.mapper = mapper;
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
                if (message.getType() == MessageType.REGISTRATION)
                    handleRegisterMessage(out, (RegisterMessage) message);
                input = in.readLine();
            }
        } catch (final IOException ¢) {
            log.error("I/O error occurred", ¢);
        } finally {
            try {
                if (out != null)
                    out.close();

                if (in != null)
                    in.close();
            } catch (final IOException ¢) {
                log.error("I/O error occurred while closing", ¢);
            }
        }
    }

    private void handleRegisterMessage(final PrintWriter out, final RegisterMessage ¢) {
        mapper.store(¢.sensorId, out);
        new AnswerMessage(Answer.SUCCESS).send(out, null);
    }
}
