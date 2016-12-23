package il.ac.technion.cs.eldery.networking.messages;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

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
        InetSocketAddress serverAddr;
        try {
            serverAddr = new InetSocketAddress(InetAddress.getByName(ip), port);
        } catch (@SuppressWarnings("unused") final UnknownHostException e1) {
            return null;
        }
        try (SocketChannel client = SocketChannel.open(serverAddr)) {
            final byte[] data = toJson().getBytes();
            final ByteBuffer buffer = ByteBuffer.wrap(data);
            client.write(buffer);
            if (!waitForResponse)
                return null;
            final byte[] responseBuffer = new byte[2048];
            final ByteBuffer dst = ByteBuffer.wrap(responseBuffer);
            client.read(dst);
            return new String(dst.array(), 0, dst.array().length);
        } catch (@SuppressWarnings("unused") final IOException e) {
            return null;
        }
    }
}
