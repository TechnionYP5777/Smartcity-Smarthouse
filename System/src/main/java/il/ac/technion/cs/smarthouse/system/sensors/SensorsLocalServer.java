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

import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;

/**
 * 
 * @author Elia Traore
 * @since 17.12.16
 */
public class SensorsLocalServer implements Runnable {
    private static Logger log = LoggerFactory.getLogger(SensorsLocalServer.class);

    private final FileSystem fileSystem;
    private final Map<String, PrintWriter> routingMap = new HashMap<>();

    private List<Closeable> serverSockets = new ArrayList<>();

    public SensorsLocalServer(final FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @Override
    public void run() {
        InstructionsSenderThread.setMapper((id,out)->routingMap.put(id.toLowerCase(),out));
        new Thread(()-> runAddressRequestServer(40001)).start();
        new Thread(()-> runBasicSensorServer(40001)).start();
        new Thread(()-> runInstructionSensorServer(40002)).start();
    }
    
    private void runBasicTcpServer(Integer port, Class<? extends SensorManagingThread> managerThreadClass){
        try (ServerSocket server = new ServerSocket(port)) {
            serverSockets.add(server);
            while (true)
                try {
                    final Socket client = server.accept();
                    Class<?>[] params = SensorManagingThread.class.getDeclaredConstructors()[0].getParameterTypes();
                    managerThreadClass.getConstructor(params).newInstance(client, fileSystem).start();                    
                } catch (final SocketException e) {
                    log.info("Server socket closed, Sensors' server at port "+port+" (TCP) is shutting down");
                    return;
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
            for (DatagramPacket packet = new DatagramPacket(buf, buf.length); true;) {
                try {
                    server.receive(packet);
                    server.send(new DatagramPacket(buf, buf.length, packet.getAddress(), packet.getPort()));
                } catch (final SocketException e) {
                    log.info("Server socket closed, Sensors' server at port "+port+" (UDP) is shutting down");
                    return; // if we closed the sockets we want to shut off the server
                } catch (IOException e) {}
            }
        } catch (SocketException e1) {
            log.warn("I/O error occurred when the socket was opened", e1);
        }
    }
    
    public void sendInstruction(final String id, final String instruction) {
        routingMap.get(id.toLowerCase()).println(instruction);
    }

    public void closeSockets() { 
        serverSockets.stream().forEach(
                        socket -> {
                                    try {
                                        socket.close();
                                    } catch (IOException | NullPointerException e) {
                                        log.warn("I/O exception occurred while closing socket", e);
                                    }
                                }
        );
    }
}
