package il.ac.technion.cs.smarthouse.system.sensors;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import il.ac.technion.cs.smarthouse.networking.messages.UpdateMessage;
import il.ac.technion.cs.smarthouse.sensors.SensorType;
import il.ac.technion.cs.smarthouse.system.DatabaseHandler;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/** A sensors handler is a class dedicated to listening for incoming messages
 * from sensors and sending instructions to them. The class creates two new
 * threads for each sensor (in order to create a bidirectional connection) so
 * the sensors handler can keep accepting new connections.
 * @author Sharon
 * @author Yarden
 * @since 17.12.16 */
public class SensorsHandler implements Runnable {
    private static Logger log = LoggerFactory.getLogger(SensorsHandler.class);
    
    private final DatabaseHandler databaseHandler;
    private final Map<String, PrintWriter> routingMap = new HashMap<>();

    // for testing
    private ServerSocket server;
    private ServerSocket router;

    /** Initializes a new sensors handler object.
     * @param databaseHandler database handler of the system */
    public SensorsHandler(final DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    @Override public void run() {
        try (ServerSocket server1 = new ServerSocket(40001)) {
            this.server = server1;
            while (true)
                try {
                    // System.out.println("sensorhandler about to attempt to accept connection on 40001"); todo: delete after debugging ends
                    final Socket client = server1.accept();
                    new SensorsHandlerThread(client, databaseHandler, type -> {
                        if (type == SensorType.INTERACTIVE)
                            new InteractiveSensorServer((id, out) -> routingMap.put(id, out)).start();
                    }).start();
                } catch (final SocketException e) {
                    log.warn("socket closed, SensorsHandler is shutting down", e);
                    return; // if we closed the sockets we want to shutoff the server
                } catch (final IOException e) {
                    log.error("I/O error occurred while waiting for a connection", e);
                }
        } catch (final IOException e) {
            log.error("I/O error occurred when the socket was opened", e);
        }
    }

    public void sendInstruction(UpdateMessage instruction) {
        routingMap.get(instruction.sensorId).println(instruction.toJson());
    }

    public void closeSockets() { // for testing
        try {
            server.close();
            // TODO: ELIA remove if you don't need this
//            router.close();
        } catch (IOException e) {
            // TODO: Auto-generated catch block
            log.error("I/O error occurred while closing", e);
        }
    }
}
