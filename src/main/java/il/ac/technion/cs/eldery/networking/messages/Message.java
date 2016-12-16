package il.ac.technion.cs.eldery.networking.messages;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import com.google.gson.Gson;

/** @author Sharon
 * @author Yarden
 * @since 11.12.16 */
public abstract class Message {
    private MessageType type;

    public Message(MessageType type) {
        this.type = type;
    }

    /** @return type of this message */
    public MessageType getType() {
        return this.type;
    }

    /** Converts the contents of this message into JSON format.
     * @return JSON formatted string */
    public String toJson() {
        return new Gson().toJson(this);
    }

    /** Sends the message to the specified destination.
     * @param ip the IP address of the destination
     * @param port the port of the destination
     * @param waitForResponse <code> true </code> if the sender expects a
     *        response, <code> false </code> otherwise
     * @return the response from the destination, if requested. If an error
     *         occurred or if a response was not requested, null will be
     *         returned. */
    public String send(String ip, int port, boolean waitForResponse) {
        // TODO: Yarden, receive a socket
        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] buffer = this.toJson().getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ip), port);
            socket.send(packet);
            if (!waitForResponse)
                return null;
            // TODO: Yarden, add timeout
            byte[] responseBuffer = new byte[2048];
            DatagramPacket receivedPacket = new DatagramPacket(responseBuffer, responseBuffer.length);
            socket.receive(receivedPacket);
            return new String(receivedPacket.getData(), 0, receivedPacket.getLength());
        } catch (@SuppressWarnings("unused") IOException e) {
            return null;
        }
    }
}
