package il.ac.technion.cs.smarthouse.database;

import java.util.HashMap;
import java.util.Map;

import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;

import il.ac.technion.cs.smarthouse.system.EmergencyLevel;

/**
 * 
 * @author Inbal Zukerman
 * @date May 13, 2017
 */
public class DatabaseManager {

    public static String parseClass = "mainDB";

	
	public static ParseObject addInfo(InfoType t, final String info) throws ParseException{
		Map<String, Object> m = new HashMap<>();
		m.put("info", t.toString().toLowerCase() + "@"+ info);
		
		return ServerManager.putValue(parseClass, m);
		
	}
	
	public static ParseObject addContactInfo(String id,String name, String phone, String email) throws ParseException{
		Map<String, Object> m = new HashMap<>();
		m.put("info", InfoType.USER$CONTACT.toString().toLowerCase() + "@"+ id + "@" + name + "@" + phone + "@" + email);
		
		return ServerManager.putValue(parseClass, m);
		
	}
	
	public static ParseObject addContactInfo(String id,String name, String phone, String email, EmergencyLevel elevel) throws ParseException{
		Map<String, Object> m = new HashMap<>();
		m.put("info", InfoType.USER$CONTACT.toString().toLowerCase() + "@"+ id + "@" + name + "@" + phone + "@" + email + "@" +elevel.toString());
		
		return ServerManager.putValue(parseClass, m);
		
	}
	
	public static void deleteContactInfo(String id){
		ParseQuery<ParseObject> findQuery = ParseQuery.getQuery(parseClass);
		findQuery.whereContains("info", InfoType.USER$CONTACT.toString().toLowerCase());
		findQuery.whereContains("info", id);
		try {
			ServerManager.deleteById(parseClass, findQuery.find().get(0).getObjectId());
		} catch (ParseException e) {
			// TODO inbal - log or throw
			e.printStackTrace();
		}
		
	}
	
	public static void deleteInfo(InfoType t){
		ParseQuery<ParseObject> findQuery = ParseQuery.getQuery(parseClass);
		findQuery.whereContains("info", t.toString().toLowerCase());
		try {
			ServerManager.deleteById(parseClass, findQuery.find().get(0).getObjectId());
		} catch (ParseException e) {
			// TODO inbal - log or throw
			e.printStackTrace();
		}
		
	}
}

