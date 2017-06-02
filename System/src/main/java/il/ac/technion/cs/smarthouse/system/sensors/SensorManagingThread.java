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

import il.ac.technion.cs.smarthouse.networking.messages.MessageType;
import il.ac.technion.cs.smarthouse.networking.messages.SensorMessage;
import il.ac.technion.cs.smarthouse.networking.messages.SensorMessage.IllegalMessageBaseExecption;
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
                final SensorMessage message;
                try{
                    message = new SensorMessage(input);
                }catch (IllegalMessageBaseExecption e) {
                    log.debug(e+"");
                    try {
                        new SensorMessage(MessageType.FAILURE_ANSWER).send(out, null);
                    } catch (IllegalMessageBaseExecption e1) {}
                    continue;
                }
//                log.info("Received message: " + message + "\n");
                handleSensorMessage(message);
            }
        } catch (final IOException e) {
        } finally {
           closeResources();
        }
    }
    
    protected abstract void handleSensorMessage(final SensorMessage msg);
}
