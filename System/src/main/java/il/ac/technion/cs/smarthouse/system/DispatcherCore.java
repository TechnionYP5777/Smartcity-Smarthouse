package il.ac.technion.cs.smarthouse.system;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.utils.UuidGenerator;

/**
 * 
 * @author Inbal Zukerman
 * @date May 23, 2017
 */
public class DispatcherCore implements Dispatcher{

	private static final Map<String, Map<String, Consumer<String>>> listeners = new HashMap<>();
	private static Logger log = LoggerFactory.getLogger(DispatcherCore.class);

	public static String getPathAsString(String... pathNodes) {
		return String.join(Dispatcher.DELIMITER, pathNodes);
	}

	public String subscribe(Consumer<String> subscriber, String... path){
		if (!listeners.containsKey(getPathAsString(path)))
			listeners.put(getPathAsString(path), new HashMap<>());

		final String id = UuidGenerator.GenerateUniqueIDstring();

		listeners.get(getPathAsString(path)).put(id, subscriber);
		return id;
	}

	public void unsubscribe(String subscriberId, String... path){
		if (!listeners.containsKey(getPathAsString(path)))
			log.error("Key Word was not found");
			// TODO: inbal - shoud throw too?
		listeners.get(getPathAsString(path)).remove(subscriberId);
	}

	public void sendMessage(InfoType infoType, String value, String... path){
		
	}

}
