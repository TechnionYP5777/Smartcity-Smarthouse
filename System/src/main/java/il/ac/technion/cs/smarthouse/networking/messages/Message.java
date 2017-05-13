package il.ac.technion.cs.smarthouse.networking.messages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a general message that can be sent from a sensor to the
 * system or vice versa.
 * 
 * @author Sharon
 * @author Yarden
 * @since 11.12.16
 */
public abstract class Message {
	
	private static Logger log = LoggerFactory.getLogger(Message.class);

	
	public static String createMessage(final String sensorId, final String sensorCommName, MessageType t,
			String info) {
		String message = (sensorId == "" || sensorCommName == "" ? "" : sensorId + "@" + sensorCommName + "@") + t.toString();
		if (info != "")
			message += "@" + info;

		return message.toLowerCase();
	}

	/** TODO: inbal, update documentation
	 * Sends the message to the specified destination.
	 * 
	 * @param out
	 *            a PrintWrite object that was created from a socket connected
	 *            to the destination
	 * @param in
	 *            a BufferedReader object that was created from a socket
	 *            connected to the destination.If a response from the
	 *            destination is not requested, <code> null </code> should be
	 *            sent.
	 * @return the response from the destination, if requested. If an error
	 *         occurred or if a response was not requested, <code> null </code>
	 *         will be returned.
	 */
	public static String send(final String message, final PrintWriter out, final BufferedReader $) {
		if (out == null)
			return null;

		out.println(message);
		if ($ != null)
			try {
				return $.readLine();
			} catch (final IOException e) {
				log.error("I/O error occurred", e);
				return null;
			}
		return null;
	}

	
	
	public static boolean isInMessage(String message, String part){
		return message.toLowerCase().contains(part.toLowerCase());
	}

}
