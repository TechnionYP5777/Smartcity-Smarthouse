package il.ac.technion.cs.smarthouse.sensors.simulator.streaming_threads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import il.ac.technion.cs.smarthouse.sensors.PathType;
import il.ac.technion.cs.smarthouse.sensors.simulator.GenericSensor;

/**
 * @author Elia Traore
 * @since Jun 22, 2017
 */

@SuppressWarnings("rawtypes")
public class RangeStreamer extends MsgStreamerThread {
	private Map<String, List> ranges;

	public RangeStreamer(GenericSensor sensor, Long streamingInterval, final Map<String, List> ranges) {
		super(sensor, streamingInterval);
		this.ranges = ranges;
	}

	private Object random(String path) {
		Class c = sensor.getPathsWithClasses(PathType.INFO_SENDING).get(path);
		List vals = ranges.get(path);
		return Integer.class.isAssignableFrom(c)
				? ThreadLocalRandom.current().nextInt((Integer) vals.get(0), (Integer) vals.get(1))
				: Double.class.isAssignableFrom(c)
						? ThreadLocalRandom.current().nextDouble((Double) vals.get(0), (Double) vals.get(1))
						: vals.get(ThreadLocalRandom.current().nextInt(vals.size()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.technion.cs.smarthouse.sensors.simulator.streaming_threads.
	 * MsgStreamerThread#send()
	 */
	@Override
	void send() {
		Map<String, Object> data = new HashMap<>();
		ranges.keySet().stream().filter(p -> sensor.getPathsWithClasses(PathType.INFO_SENDING).containsKey(p))
				.forEach(path -> data.put(path, random(path)));
		sensor.sendMessage(data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.technion.cs.smarthouse.sensors.simulator.streaming_threads.
	 * MsgStreamerThread#canStartStreaming()
	 */
	/**
	 * checks that the ranges are legal
	 */
	@Override
	@SuppressWarnings("unchecked")
	Boolean canStartStreaming() {
		final List<Boolean> $ = new ArrayList<>();

		sensor.getPathsWithClasses(PathType.INFO_SENDING).forEach((path, c) -> {
			List vals = ranges.get(path);
			Boolean sizeOk = !Integer.class.isAssignableFrom(c) && !Double.class.isAssignableFrom(c)
					|| vals.size() == 2,
					valsOk = (Boolean) vals.stream().map(o -> c.isAssignableFrom(o.getClass()))
							.reduce((x, y) -> (Boolean) x && (Boolean) x).orElse(true);
			$.add(sizeOk && valsOk);
		});

		return $.stream().reduce((x, y) -> x && y).orElse(false);
	}

}
