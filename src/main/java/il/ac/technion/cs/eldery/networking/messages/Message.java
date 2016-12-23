package il.ac.technion.cs.eldery.networking.messages;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import com.google.gson.Gson;

/** @author Sharon
 * @author Yarden
 * @since 11.12.16 */
public abstract class Message {
    private final MessageType type;

    /** Creates a new message object.
     * @param type type of this message */
    public Message(final MessageType type) {
        this.type = type;
    }

    /** @return type of this message */
    public MessageType getType() {
        return type;
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
    public String send(final String ip, final int port, final boolean waitForResponse) {
        try (DatagramSocket socket = new DatagramSocket()) {
            final byte[] buffer = toJson().getBytes();
            System.out.println("DSAFSA: " + new String(buffer));
            System.out.println(buffer.length);
            socket.send(new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ip), port));
            if (!waitForResponse)
                return null;
            // TODO: Yarden, add timeout
            final byte[] responseBuffer = new byte[2048];
            final DatagramPacket $ = new DatagramPacket(responseBuffer, responseBuffer.length);
            socket.setSoTimeout(10000);
            try {
                socket.receive($);
            } catch (@SuppressWarnings("unused") final SocketTimeoutException e) {
                return null;
            }
            return new String($.getData(), 0, $.getLength());
        } catch (@SuppressWarnings("unused") final IOException e) {
            return null;
        }
    }
}
