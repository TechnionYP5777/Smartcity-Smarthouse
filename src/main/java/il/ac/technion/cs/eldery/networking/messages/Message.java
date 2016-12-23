package il.ac.technion.cs.eldery.networking.messages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
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
     * @return the response from the destination, if exists. If an error
     *         occurred or if there was no response, null will be returned. */
    public String send(final String ip, final int port) {
        try (Socket socket = new Socket(InetAddress.getByName(ip), port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
            out.println(this.toJson());
            return in.readLine();
        } catch (@SuppressWarnings("unused") final IOException e) {
            return null;
        }
    }
}
