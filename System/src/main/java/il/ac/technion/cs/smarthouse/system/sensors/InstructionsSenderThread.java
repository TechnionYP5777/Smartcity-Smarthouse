package il.ac.technion.cs.smarthouse.system.sensors;

import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.networking.messages.MessageType;
import il.ac.technion.cs.smarthouse.networking.messages.SensorMessage;
import il.ac.technion.cs.smarthouse.networking.messages.SensorMessage.IllegalMessageBaseExecption;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;

/**
 * An instructions sender thread is a class that allows sending instructions
 * from the system to a specific sensor.
 * 
 * @author Elia Traore
 * @author Yarden
 * @since 30.3.17
 */
public class InstructionsSenderThread extends SensorManagingThread {
	private static Logger log = LoggerFactory.getLogger(InstructionsSenderThread.class);
	private static String instructionSeperator = "##";

	public InstructionsSenderThread(final Socket client, final FileSystem fs) {
		super(client, fs);
	}

	private void notifySensor(final String path, final Object data) {
		if (data != null)
			out.println(path + instructionSeperator + data);
	}

	@Override
	protected void handleSensorMessage(final SensorMessage msg) {
		if (!MessageType.REGISTRATION.equals(msg.getType()))
			log.error("\n\t" + getClass() + " shouldn't receive an update msg.");
		else {
			msg.getInstructionRecievingPaths().forEach(path -> {
				final String legalPath = FileSystemEntries.LISTENERS_OF_SENSOR.buildPath(msg.getSensorCommName(),
						msg.getSensorId(), path);
				filesystem.subscribe((p, data) -> notifySensor(path, data), legalPath);
			});
			try {
				new SensorMessage(MessageType.SUCCESS_ANSWER).send(out, null);
			} catch (final IllegalMessageBaseExecption e) {
			}
			msg.getInstructionRecievingPaths().forEach(path -> {
				final String legalPath = FileSystemEntries.LISTENERS_OF_SENSOR.buildPath(msg.getSensorCommName(),
						msg.getSensorId(), path);
				notifySensor(path, filesystem.getData(legalPath));
			});
		}
	}

	public static String getInstructionSeperatorRegex() {
		return instructionSeperator;
	}
}
