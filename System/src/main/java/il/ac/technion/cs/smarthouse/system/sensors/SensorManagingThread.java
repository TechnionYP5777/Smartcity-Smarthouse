package il.ac.technion.cs.smarthouse.system.sensors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.DatabaseHandler;


/**
 * @author Elia Traore
 * @since 30.5.17
 */
public abstract class SensorManagingThread extends Thread {
    private static Logger log = LoggerFactory.getLogger(SensorManagingThread.class);
    
    protected final DatabaseHandler databaseHandler;
    protected Socket client;
    protected PrintWriter out;
    protected BufferedReader in;

    public SensorManagingThread(final Socket client, final DatabaseHandler databaseHandler) {
        this.client = client;
        this.databaseHandler = databaseHandler;
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException | NullPointerException e) {
            log.error("I/O error occurred during managing thread initializing", e);
        }
    }
    
    @Override
    public void run() {
        if(in == null || out == null)
            return;
        
        try {
            for (String input = in.readLine(); input != null; input = in.readLine()) {
                processInputLine(input);
            }
        } catch (final IOException e) {
        } finally {
            try {
                if(in != null) in.close();
                if(out != null) out.close();
                client.close();
            } catch (final IOException e) {
            }
        }
    }

    protected abstract void processInputLine(String input);
}
