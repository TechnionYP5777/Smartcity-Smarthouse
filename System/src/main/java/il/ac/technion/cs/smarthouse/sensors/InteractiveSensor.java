package il.ac.technion.cs.smarthouse.sensors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.networking.messages.Message;
import il.ac.technion.cs.smarthouse.networking.messages.MessageType;

/**
 * This class represents a sensor that can get instructions and operate
 * accordingly.
 * 
 * @author Yarden
 * @since 31.3.17
 */
public abstract class InteractiveSensor extends Sensor {
	private static Logger log = LoggerFactory.getLogger(InteractiveSensor.class);

	private class InstructionChecker extends TimerTask {
		InstructionChecker() {
		}

		@Override
		public void run() {
			operate();
			new Timer().schedule(this, 0, period);
		}
	}

	protected int instPort;
	protected Socket instSocket;
	protected PrintWriter instOut;
	protected BufferedReader instIn;
	protected InstructionHandler handler;
	protected long period;

	public InteractiveSensor(final String id, final String systemIP, final int systemPort, final int instPort) {
		super(id, systemIP, systemPort);
		this.instPort = instPort;
		handler = null;

		sType = SensorType.INTERACTIVE;
	}

	/**
	 * Registers the sensor its instructions TCP connection with the system.
	 * This method must be called before any instructions are sent.
	 * 
	 * @return <code>true</code> if registration was successful,
	 *         <code>false</code> otherwise
	 */
	public boolean registerInstructions() {
		try {
			instSocket = new Socket(InetAddress.getByName(systemIP), instPort);
			instOut = new PrintWriter(instSocket.getOutputStream(), true);
			instIn = new BufferedReader(new InputStreamReader(instSocket.getInputStream()));
		} catch (final IOException e) {
			log.error("I/O error occurred when the sensor's instructions socket was created", e);
		}
		final String $ = Message.send(Message.createMessage(id, MessageType.REGISTRATION, sType.toString()), instOut,
				instIn);
		return $ != null && $.contains("success");
	}

	/**
	 * Sets the operation to be made when instruction is received. This method
	 * must be called before sending instructions.
	 */
	public void setInstructionHandler(final InstructionHandler h) {
		handler = h;
	}

	/**
	 * If there is an incoming instruction, extracts it and executes it.
	 * 
	 * @return <code>true</code> if the instruction was completed successfully
	 *         (or if there is no waiting instruction), <code>false</code>
	 *         otherwise
	 */
	public boolean operate() {

		try {
			return instIn.ready() && handler.applyInstruction(instIn.readLine());
		} catch (final IOException e) {
			log.error("I/O error occurred", e);
			return false;
		}
	}

	/**
	 * Checks for waiting instructions by polling the connection repeatedly
	 * every specified period of time.
	 * 
	 * @param p
	 *            The polling period, in milliseconds
	 */
	public void pollInstructions(final long p) {
		period = p;
		new Timer().schedule(new InstructionChecker(), 0, period);
	}
}
