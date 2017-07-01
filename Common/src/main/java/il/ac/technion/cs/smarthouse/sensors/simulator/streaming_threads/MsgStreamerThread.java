package il.ac.technion.cs.smarthouse.sensors.simulator.streaming_threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.sensors.simulator.GenericSensor;

/**
 * @author Elia Traore
 * @since Jun 22, 2017
 */

public abstract class MsgStreamerThread extends Thread {
	private static Logger log = LoggerFactory.getLogger(MsgStreamerThread.class);

	GenericSensor sensor;
	Long streamingInterval;
	private Boolean keepStreaming = true;

	protected MsgStreamerThread(GenericSensor sensor, Long streamingInterval) {
		this.sensor = sensor;
		this.streamingInterval = streamingInterval;
	}

	@Override
	public void interrupt() {
		keepStreaming = false;
		log.debug("streamer was interruped. Stopping.");
		super.interrupt();
	}

	@Override
	public void run() {
		if (!canStartStreaming())
			return;
		log.info("streamer initialized.");
		while (keepStreaming) {
			send();
			try {
				Thread.sleep(streamingInterval);
			} catch (InterruptedException e) {
				keepStreaming = false;
			}
		}
	}

	/**
	 * Sends a single message to the system
	 */
	abstract void send();

	/**
	 * Will be called before initializing the streaming to assure theres a point
	 * 
	 * @return <code>true</code> if the streaming can begin.
	 */
	abstract Boolean canStartStreaming();
}
