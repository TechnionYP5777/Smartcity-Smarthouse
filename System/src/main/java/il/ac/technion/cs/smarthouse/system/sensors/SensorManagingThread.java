package il.ac.technion.cs.smarthouse.system.sensors;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.networking.messages.AnswerMessage;
import il.ac.technion.cs.smarthouse.networking.messages.Message;
import il.ac.technion.cs.smarthouse.networking.messages.MessageFactory;
import il.ac.technion.cs.smarthouse.networking.messages.RegisterMessage;
import il.ac.technion.cs.smarthouse.networking.messages.UpdateMessage;
import il.ac.technion.cs.smarthouse.networking.messages.AnswerMessage.Answer;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;

/**
 * @author Elia Traore
 * @since 30.5.17
 */
public abstract class SensorManagingThread extends Thread {
    private static Logger log = LoggerFactory.getLogger(SensorManagingThread.class);

    protected Socket client;
    protected PrintWriter out;
    protected BufferedReader in;
    protected final FileSystem filesystem;
    private final List<Closeable> resources;

    public SensorManagingThread(final Socket client, final FileSystem filesystem) {
        this.client = client;
        this.filesystem = filesystem;
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException | NullPointerException e) {
            log.error("I/O error occurred during managing thread initializing", e);
        }
        this.resources = Arrays.asList(in,out,client);
    }
    
    private void closeResources(){
        resources.stream()
                    .forEach(c -> {
                                    try {
                                        c.close();
                                    } catch (IOException | NullPointerException e) {
                                }
                            }
                    );
    }
    
    @Override
    public void run() {
        if(resources.contains(null)){
            closeResources();
            return;
        }
        
        try {
            for (String input = in.readLine(); input != null; input = in.readLine()) {
                final Message message = MessageFactory.create(input);
                if (message == null) {
                    new AnswerMessage(Answer.FAILURE).send(out, null);
                    return;
                }
//                log.info("Received message: " + message + "\n");
                switch (message.getType()) {
                    case REGISTRATION:
                        handleRegisterMessage((RegisterMessage) message);
                        break;
                    case UPDATE:
                        handleUpdateMessage((UpdateMessage) message);
                        break;
                    default:
                        log.warn("message could not be parsed");
                }
            }
        } catch (final IOException e) {
        } finally {
           closeResources();
        }
    }

    protected abstract void handleRegisterMessage(final RegisterMessage msg);
    
    protected abstract void handleUpdateMessage(final UpdateMessage msg);
}
