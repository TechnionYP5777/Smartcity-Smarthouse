package il.ac.technion.cs.eldery.system.sensors;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class SensorHandler implements Runnable {
    @SuppressWarnings("rawtypes") private Map<String, SensorInfo> sensors = new HashMap<>();

    @SuppressWarnings("rawtypes") public Map<String, SensorInfo> getSensors() {
        return sensors;
    }

    @Override public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(100);
            byte[] buffer = new byte[2048];
            
            for (DatagramPacket packet = new DatagramPacket(buffer, buffer.length); true;) {
                socket.receive(packet);
                String message = new String(buffer, 0, packet.getLength());
                
                // TODO: Sharon, convert to message and handle accordingly
                
                packet.setLength(buffer.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
