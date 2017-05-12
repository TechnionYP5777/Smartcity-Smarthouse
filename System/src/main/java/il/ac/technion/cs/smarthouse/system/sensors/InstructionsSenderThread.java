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

/** An instructions sender thread is a class that allows sending instructions
 * from the system to a specific sensor.
 * @author Yarden
 * @author Inbal Zukerman
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
               // final Message message = MessageFactory.create(input);
                if (input == "") {
                	String answerMessage = Message.createMessage("", "", MessageType.ANSWER, "FAILURE");
                	Message.send(answerMessage, out, null);
                    continue;
                }
                log.info("Received message: " + input + "\n");
                if (input.contains("registration"))
                    handleRegisterMessage(out, input);
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

    private void handleRegisterMessage(final PrintWriter out, final String ¢) {
        String [] parts = ¢.split("@");
    	mapper.store(parts[0], out);
        String answerMessage = Message.createMessage("", "", MessageType.ANSWER	, "SUCCESS");
        Message.send(answerMessage, out, null);
    }
}
