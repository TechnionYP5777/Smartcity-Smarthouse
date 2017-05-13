package il.ac.technion.cs.smarthouse.database;

import java.util.HashMap;
import java.util.Map;

import org.parse4j.ParseException;
import org.parse4j.ParseObject;

/**
 * 
 * @author Inbal Zukerman
 * @date May 13, 2017
 */
public class DatabaseManager {

    public static String parseClass = "mainDB";

	
	public static ParseObject addInfo(InfoType infoType, final String info) throws ParseException{
		Map<String, Object> m = new HashMap<>();
		m.put("info", infoType.toString().toLowerCase() + "@"+ info.toLowerCase());
		
		return ServerManager.putValue(parseClass, m);
		
	}
}

