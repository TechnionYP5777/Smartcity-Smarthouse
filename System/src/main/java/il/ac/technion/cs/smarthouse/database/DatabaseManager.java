package il.ac.technion.cs.smarthouse.database;

import java.util.HashMap;
import java.util.Map;

import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;

import il.ac.technion.cs.smarthouse.system.DispatcherCore;
import il.ac.technion.cs.smarthouse.system.EmergencyLevel;
import il.ac.technion.cs.smarthouse.system.InfoType;

/**
 * 
 * @author Inbal Zukerman
 * @date May 13, 2017
 */
public class DatabaseManager {

    public static String parseClass = "mainDB";
    public static String pathCol = "path"; // TODO inbal replace to match this
    public static String valueCol = "value"; // TODO inbal replace to match this

    public static ParseObject addInfo(final InfoType t, final String info) throws ParseException {
        final Map<String, Object> m = new HashMap<>();
        m.put("info", t.toString().toLowerCase() + "." + info);

        return ServerManager.putValue(parseClass, m);

    }

    public static ParseObject addContactInfo(final String id, final String name, final String phone, final String email)
                    throws ParseException {
        //TODO: inbal!
        final Map<String, Object> m = new HashMap<>();
        m.put("info", InfoType.CONTACT.toString().toLowerCase() + "@" + id + "@" + name + "@" + phone + "@"
                        + email);

        return ServerManager.putValue(parseClass, m);

    }

    public static ParseObject addContactInfo(final String id, final String name, final String phone, final String email,
                    final EmergencyLevel elevel) throws ParseException {
        //TODO: inbal!
        final Map<String, Object> m = new HashMap<>();
        m.put("info", InfoType.CONTACT.toString().toLowerCase() + "@" + id + "@" + name + "@" + phone + "@" + email
                        + "@" + elevel.toString());

        return ServerManager.putValue(parseClass, m);

    }

    public static void deleteContactInfo(final String id) {
        //TODO: inbal!
        final ParseQuery<ParseObject> findQuery = ParseQuery.getQuery(parseClass);
        findQuery.whereContains("info", InfoType.CONTACT.toString().toLowerCase());
        findQuery.whereContains("info", id);
        try {
            ServerManager.deleteById(parseClass, findQuery.find().get(0).getObjectId());
        } catch (final ParseException e) {
            // TODO inbal - log or throw
            e.printStackTrace();
        }

    }

    public static void deleteInfo(final InfoType t) {
        final ParseQuery<ParseObject> findQuery = ParseQuery.getQuery(parseClass);
        findQuery.whereContains("info", t.toString().toLowerCase());

        try {
            ServerManager.deleteById(parseClass, findQuery.find().get(0).getObjectId());
        } catch (final ParseException e) {
            // TODO inbal - log or throw
            e.printStackTrace();
        }

    }

    public static String getLastEntry(String... path) {
        final ParseQuery<ParseObject> findQuery = ParseQuery.getQuery(parseClass);

        findQuery.whereContains("info", DispatcherCore.getPathAsString(path).toLowerCase());

        try {
            if (!findQuery.find().isEmpty()) {
                findQuery.orderByDescending("createdAt");
                return findQuery.find().get(0).getString("info");
            }
        } catch (ParseException e) {
            // TODO throw? return ""?
            e.printStackTrace();
        }

        return ""; // TODO: inbal.... same question applies..

    }

}
