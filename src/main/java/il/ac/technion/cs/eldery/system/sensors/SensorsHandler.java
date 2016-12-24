package il.ac.technion.cs.eldery.system.sensors;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import il.ac.technion.cs.eldery.system.DatabaseHandler;

/** A sensors handler is a class dedicated to listening for incoming messages
 * from sensors. The class creates a new thread for each connection so the
 * sensors handler can keep accepting new connections.
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
        try (ServerSocket server = new ServerSocket(40000)) {
            while (true)
                try (Socket client = server.accept()) {
                    new SensorsHandlerThread(client, databaseHandler).start();
                } catch (final IOException ¢) {
                    ¢.printStackTrace();
                }
        } catch (final IOException ¢) {
            ¢.printStackTrace();
        }
    }
}
