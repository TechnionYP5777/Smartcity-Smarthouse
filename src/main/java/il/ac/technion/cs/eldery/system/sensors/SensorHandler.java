package il.ac.technion.cs.eldery.system.sensors;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

public class SensorHandler implements Runnable {
    @SuppressWarnings("rawtypes") private Map<String, SensorInfo> sensors = new HashMap<>();

    @SuppressWarnings("rawtypes") public Map<String, SensorInfo> getSensors() {
        return sensors;
    }

    @SuppressWarnings({"unused" }) @Override public void run() {
        try (DatagramSocket socket = new DatagramSocket(100)) {
            byte[] buffer = new byte[2048];

            for (DatagramPacket packet = new DatagramPacket(buffer, buffer.length);;) {
                socket.receive(packet);
                String message = new String(buffer, 0, packet.getLength());
                packet.setLength(buffer.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
