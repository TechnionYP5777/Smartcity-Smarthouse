package il.ac.technion.cs.smarthouse.system.sensors;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.sensors.SensorType;
import il.ac.technion.cs.smarthouse.system.DatabaseHandler;

/**
 * A sensors handler is a class dedicated to listening for incoming messages
 * from sensors and sending instructions to them. The class creates two new
 * threads for each sensor (in order to create a bidirectional connection) so
 * the sensors handler can keep accepting new connections.
 * 
 * @author Elia Traore
 * @author Yarden
 * @since 17.12.16
 */
public class SensorsLocalServer implements Runnable {
    private static Logger log = LoggerFactory.getLogger(SensorsLocalServer.class);

    private final DatabaseHandler databaseHandler;
    private final Map<String, PrintWriter> routingMap = new HashMap<>();

    private List<Closeable> serverSockets = new ArrayList<>();
    /**
     * Initializes a new sensors handler object.
     * 
     * @param databaseHandler
     *            database handler of the system
     */
    public SensorsLocalServer(final DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    @Override
    public void run() {
        InstructionsSenderThread.setMapper((id,out)->routingMap.put(id,out));
        new Thread(()-> runBasicSensorServer(40001)).start();
        new Thread(()-> runInstructionSensorServer(40002)).start();
        new Thread(()-> runAddressRequestServer(40001)).start();
    }
    
    private void runBasicTcpServer(Integer port, Class<? extends SensorManagingThread> managerThreadClass){
        try (ServerSocket server = new ServerSocket(port)) {
            serverSockets.add(server);
            while (true)
                try {
                    final Socket client = server.accept();
                    Class<?>[] params = SensorManagingThread.class.getDeclaredConstructors()[0].getParameterTypes();
                    managerThreadClass.getConstructor(params).newInstance(client, databaseHandler).start();                    
                } catch (final SocketException e) {
                    log.info("socket closed, Sensors' server at port "+port+" is shutting down");
                    return; // if we closed the sockets we want to shut off the server
                } catch (final IOException e) {
//                    log.warn("I/O error occurred while waiting for a connection", e);  no need to log this - it spams the log
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException |
                                InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    log.error("Error occured during the reflection process creating a single sensor managing thread.", e);
                } 
        } catch (final IOException e) {
            log.warn("I/O error occurred when the socket was opened", e);
        }
    }
    
    
    private void runBasicSensorServer(Integer port){
        runBasicTcpServer(port, SensorsHandlerThread.class);
    }
    
    private void runInstructionSensorServer(Integer port){
        runBasicTcpServer(port, InstructionsSenderThread.class);
    }
    
    private void runAddressRequestServer(Integer port){
        try(DatagramSocket server = new DatagramSocket(port)){
            serverSockets.add(server);
            
            byte[] buf = new byte[8];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            while (true) {
                try {
                    server.receive(packet);
                    server.send(new DatagramPacket(buf, buf.length, packet.getAddress(), packet.getPort()));
                } catch (IOException e) {
                }
                
            }
        } catch (SocketException e1) {
            log.warn("I/O error occurred when the socket was opened", e1);
        }
    }
    

    public void sendInstruction(final String id, final String instruction) {
        routingMap.get(id).println(instruction);
    }

    public void closeSockets() { 
        serverSockets.stream().forEach(socket -> {
                                                    try {
                                                        socket.close();
                                                    } catch (IOException e) {
                                                        log.warn("I/O exception occurred while closing socket", e);
                                                    }
                                                }
        );
    }
}
