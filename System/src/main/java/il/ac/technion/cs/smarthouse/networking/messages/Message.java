package il.ac.technion.cs.smarthouse.networking.messages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.Dispatcher;

/**
 * This class represents a general message that can be sent from a sensor to the
 * system or vice versa.
 * 
 * @author Sharon
 * @author Yarden
 * @author Inbal Zukerman
 * @since 11.12.16
 */
public abstract class Message {

	public static final String SENSOR_ID = "sensorid-";
	private static Logger log = LoggerFactory.getLogger(Message.class);

	public static String createMessage(final MessageType t, final String info, final String sensorId) {
		String message = t.toString() + Dispatcher.DELIMITER;
		if(info != "")
			message += info + Dispatcher.DELIMITER ;
		return (message + SENSOR_ID + sensorId).toLowerCase();
	}

	public static String createMessage(final MessageType t, final MessageType status) {
		return (t.toString() + Dispatcher.DELIMITER + status.toString()).toLowerCase();
	}

	/**
	 * TODO: inbal, update documentation
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

	public static boolean isInMessage(final String message, final String part) {
		return message.toLowerCase().contains(part.toLowerCase());
	}

	public static boolean isSuccessMessage(final String message) {
		return message.toLowerCase().contains(MessageType.SUCCESS.toString().toLowerCase());
	}

	public static boolean isFailureMessage(final String message) {
		return message.toLowerCase().contains(MessageType.FAILURE.toString().toLowerCase());
	}

}
