package il.ac.technion.cs.smarthouse.sensors.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Supplier;

import il.ac.technion.cs.smarthouse.sensors.InteractiveSensor;
import il.ac.technion.cs.smarthouse.sensors.PathType;
import il.ac.technion.cs.smarthouse.sensors.simulator.streaming_threads.MsgStreamerThread;
import il.ac.technion.cs.smarthouse.sensors.simulator.streaming_threads.RangeStreamer;
import il.ac.technion.cs.smarthouse.sensors.simulator.streaming_threads.SupplierStreamer;


/**
 * @author Elia Traore
 * @since Jun 17, 2017
 */
@SuppressWarnings("rawtypes")
public class GenericSensor {
	private static Logger log = LoggerFactory.getLogger(GenericSensor.class);

	private Map<PathType, Set<Consumer<String>>> loggers = new HashMap<>();
	private Map<PathType, Map<String, Class>> paths = new HashMap<>();
	private Boolean interactive = false, connected = false;
	private Long pollInterval = TimeUnit.SECONDS.toMillis(1), 
			streamingInterval = TimeUnit.SECONDS.toMillis(20);

	private Map<String, List> lastReceivedRanges;
	private Supplier<Map<String, Object>> msgsGenerator;

	private InteractiveSensor sensor;

	private MsgStreamerThread msgStreamer;

	GenericSensor() {
	}

	GenericSensor(GenericSensor other) {
		Optional.ofNullable(other.loggers).ifPresent(otherloggers -> loggers = new HashMap<>(otherloggers));
		Stream.of(PathType.values()).filter(t -> other.paths.containsKey(t))
									.forEach(t -> paths.put(t, new HashMap<>(other.paths.get(t))));
		interactive = other.interactive;
		connected = other.connected;
		pollInterval = other.pollInterval;
		streamingInterval = other.streamingInterval;
		
		Optional.ofNullable(other.lastReceivedRanges).ifPresent(otherRanges -> lastReceivedRanges = new HashMap<>(otherRanges));
		msgsGenerator = other.msgsGenerator;
	}

	private void connectIfNeeded() {
		if (connected)
			return;
		log.info("GenericSensor " + this + " trying connect sensor for sending");
		while (!sensor.register())
			;
		if (interactive) {
			log.info("GenericSensor " + this + " trying connect sensor for receiveing");
			while (!sensor.registerInstructions())
				;
			sensor.pollInstructions(pollInterval);
		}
		connected = true;
		log.info("GenericSensor " + this + "  connected!");
	}

	// ------------------------ setters --------------------------------------
	void addPath(PathType t, String path, Class pathClass) {
		if (!paths.containsKey(t))
			paths.put(t, new HashMap<>());
		paths.get(t).put(path, pathClass);
		interactive |= PathType.INSTRUCTION_RECEIVING.equals(t);
	}

	void addLogger(PathType t, Consumer<String> logger) {
		if (!loggers.containsKey(t))
			loggers.put(t, new HashSet<>());
		loggers.get(t).add(logger);
	}

	void setPollingInterval(Long milliseconds) {
		pollInterval = milliseconds;
	}

	void setStreamInterval(Long milliseconds) {
		streamingInterval = milliseconds;
	}

	void setSensor(InteractiveSensor s) {
		sensor = s;
	}

	void setRanges(Map<String, List> ranges) {
		lastReceivedRanges = ranges;
	}

	void setMsgsSupplier(Supplier<Map<String, Object>> m){
		msgsGenerator = m;
	}
	// ------------------------ getters --------------------------------------
	List<String> getPaths(PathType t) {
		return Optional.ofNullable(paths.get(t)).map(ps -> new ArrayList<>(ps.keySet())).orElse(new ArrayList<>());
	}

	InteractiveSensor getSensor() {
		return sensor;
	}

	String name(){
		return "A sensor called: " +getAlias() + " ("+ getCommname()+", "+getId()+")";
	}
	// ------------------------ logging --------------------------------------
	void logInstruction(String path, String inst) {
		Optional.ofNullable(loggers.get(PathType.INSTRUCTION_RECEIVING)).ifPresent(ls -> ls
				.forEach(logger -> logger.accept(name()+ 
													"\n\tReceived instruction:\t"+ inst+
													"\n\tOn path:\t" + path)));
	}

	void logMsgSent(Map<String, String> data) {
		final List<String> $ = new ArrayList<>();
		$.add(name());
		$.add("\tSending following msg:");
		data.keySet().forEach(path -> $.add("\t\tpath:" + path + "\tvalue:" + data.get(path)));

		$.stream().reduce((x, y) -> x + "\n" + y)
				.ifPresent(formatedData -> Optional.ofNullable(loggers.get(PathType.INFO_SENDING))
						.ifPresent(ls -> ls.forEach(logger -> logger.accept(formatedData))));
	}

	// ------------------------ public method -------------------------------
	/** blocks until an instruction is sent */
	public void waitForInstruction() {
		while (!sensor.operate());
	}

	/** blocking method */
	public GenericSensor connect() {
		connectIfNeeded();
		return this;
	}

	public String getCommname() {
		return sensor.getCommname();
	}

	public String getId() {
		return sensor.getId();
	}

	public String getAlias() {
		return sensor.getAlias();
	}
	
	public List<String> getObservationSendingPaths() {
		return sensor.getObservationSendingPaths();
	}

	public List<String> getInstructionRecievingPaths() {
		return sensor.getInstructionRecievingPaths();
	}
	
	public Map<String, Class> getPathsWithClasses(PathType t){
		return paths.get(t);
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof GenericSensor && this.getId().equals(((GenericSensor) o).getId());
	}

	// ----------- Data sending methods -----------
	/**
	 * will also connect if sensor not connected yet
	 */
	public void sendMessage(Map<String, Object> data) {// todo: change to
														// package level?
		connectIfNeeded();

		if (!paths.containsKey(PathType.INFO_SENDING))
			return;
		Map<String, String> d = new HashMap<>();
		data.keySet().forEach(k -> d.put(k, data.get(k) + ""));
		sensor.updateSystem(d);
		logMsgSent(d);
	}

	private void stream(final MsgStreamerThread t){
		if (!paths.containsKey(PathType.INFO_SENDING))
			return;

		Optional.ofNullable(msgStreamer).ifPresent(streamer -> {
			streamer.interrupt();
			try {
				streamer.join();
			} catch (InterruptedException e) {
				log.warn("\n\tSimulator was interrupted will waiting for a msg streamer");
			}
		});

		msgStreamer = t;
		msgStreamer.start();
	}
	
	/**
	 * the list object is interperated by the path (key type) if the type is
	 * string or boolean, the list contains all the legal values if the type is
	 * double or integer, the list contains (low,high) so that low <= legal
	 * values < high
	 */
	public void streamMessages(final Map<String, List> ranges) {
		lastReceivedRanges = ranges;
		stream(new RangeStreamer(this, streamingInterval, lastReceivedRanges));
		
	}
	
	public void streamMessages(final Supplier<Map<String, Object>> msgsGenerator){
		this.msgsGenerator = msgsGenerator;
		stream(new SupplierStreamer(this, streamingInterval, msgsGenerator));
	}

	/**
	 * streams with the default given values: 
	 * first tries through supplier, and if none exists through ranges
	 */
	public void startStreaming() {
		if (msgsGenerator != null)
			streamMessages(msgsGenerator);
		else if (lastReceivedRanges != null)
			streamMessages(lastReceivedRanges);
	}

	public void stopStreaming(){
		Optional.ofNullable(msgStreamer).ifPresent(t -> t.interrupt());
	}
}
