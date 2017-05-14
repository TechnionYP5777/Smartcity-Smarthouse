package il.ac.technion.cs.smarthouse.system.sensors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.parse4j.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.database.DatabaseManager;
import il.ac.technion.cs.smarthouse.database.InfoType;
import il.ac.technion.cs.smarthouse.database.ServerManager;
import il.ac.technion.cs.smarthouse.networking.messages.Message;
import il.ac.technion.cs.smarthouse.networking.messages.MessageType;
import il.ac.technion.cs.smarthouse.sensors.SensorType;
import il.ac.technion.cs.smarthouse.system.DatabaseHandler;

/**
 * A sensors handler thread is a class that handles a specific connection with a
 * sensor. The class can parse the different incoming messages and act
 * accordingly.
 * 
 * @author Yarden
 * @author Inbal Zukerman
 * @since 24.12.16
 */
public class SensorsHandlerThread extends Thread {
	private static Logger log = LoggerFactory.getLogger(SensorsHandlerThread.class);

	private final Socket client;
	private final DatabaseHandler databaseHandler;
	// private TypeHandler typeHandler;

	public SensorsHandlerThread(final Socket client, final DatabaseHandler databaseHandler,
			final TypeHandler typeHandler) {
		this.client = client;
		this.databaseHandler = databaseHandler;
		// this.typeHandler = typeHandler;
		ServerManager.initialize();
	}

	@Override
	public void run() {
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			out = new PrintWriter(client.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			for (String input = in.readLine(); input != null;) {

				if (input == "") {
					final String answerMessage = Message.createMessage("", MessageType.ANSWER, "FAILURE");
					Message.send(answerMessage, out, null);

					continue;
				}
				log.info("Received message: " + input + "\n");

				if (Message.isInMessage(input, "registration"))
					handleRegisterMessage(out, input);
				else if (Message.isInMessage(input, "UPDATE"))
					handleUpdateMessage(input);
				else
					log.error("message could not be parsed");

				input = in.readLine();
			}
		} catch (final IOException e) {
			log.error("I/O error occurred", e);
		} finally {
			try {
				if (out != null)
					out.close();

				if (in != null)
					in.close();
			} catch (final IOException e) {
				log.error("I/O error occurred while closing", e);
			}
		}
	}

	private void handleRegisterMessage(final PrintWriter out, final String ¢) {
		final String[] parsedMessage = ¢.split("@");
		databaseHandler.addSensor(parsedMessage[0], parsedMessage[1], 100);
		Message.send(Message.createMessage("", MessageType.ANSWER, "SUCCESS"), out, null);

	}

	private void handleUpdateMessage(final String m) {

		try {
			DatabaseManager.addInfo(InfoType.SENSOR_MESSAGE, m);
		} catch (final ParseException e) {
			log.error("Failed to store data", e);
		}

	}

}

interface TypeHandler {
	void accept(SensorType t);
}
