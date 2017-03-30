package il.ac.technion.cs.eldery.system.sensors;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import il.ac.technion.cs.eldery.networking.messages.UpdateMessage;
import il.ac.technion.cs.eldery.system.DatabaseHandler;

/** A sensors handler is a class dedicated to listening for incoming messages
 * from sensors and sending instructions to them. The class creates two new
 * threads for each sensor (in order to create a bidirectional connection) so
 * the sensors handler can keep accepting new connections.
 * @author Sharon
 * @author Yarden
 * @since 17.12.16 */
public class SensorsHandler implements Runnable {
    private final DatabaseHandler databaseHandler;
    private final Map<String, PrintWriter> routingMap = new HashMap<>();

    /** Initializes a new sensors handler object.
     * @param databaseHandler database handler of the system */
    public SensorsHandler(final DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    @Override public void run() {
        try (ServerSocket server = new ServerSocket(40001); ServerSocket router = new ServerSocket(40002)) {
            while (true)
                try {
                    @SuppressWarnings("resource") final Socket client = server.accept();
                    new SensorsHandlerThread(client, databaseHandler).start();
                    @SuppressWarnings("resource") final Socket sensor = router.accept();
                    new InstructionsSenderThread(sensor, (id, out) -> routingMap.put(id, out)).start();
                } catch (final IOException ¢) {
                    ¢.printStackTrace();
                }
        } catch (final IOException ¢) {
            ¢.printStackTrace();
        }
    }

    public void sendInstruction(UpdateMessage instruction) {
        routingMap.get(instruction.sensorId).println(instruction.toJson());
    }
}
