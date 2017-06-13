/**
 * 
 */
package il.ac.technion.cs.smarthouse.applications.dashboard.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Elia Traore
 * @since Jun 12, 2017
 */
public class InfoCollector {
	
	String unit;
	Map<String, String> entries = new HashMap<>();
	
	public InfoCollector addInfoEntry(String path, String name){
		entries.put(path, name);
		return this;
	}
	public InfoCollector setUnit(String u){
		unit = u;
		return this;
	}
	
	/** 
	 * @return a map where the key is a given path and the values are names
	 * */
	public Map<String,String> getInfoEntries(){
		return new HashMap<>(entries);
	}
	public String getUnit(){
		return unit;
	}

}