package il.ac.technion.cs.smarthouse.database;

import java.util.HashMap;
import java.util.Map;

import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;

import il.ac.technion.cs.smarthouse.system.Dispatcher;
import il.ac.technion.cs.smarthouse.system.DispatcherCore;
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

    public static ParseObject addInfo(final InfoType t, final String path, final String value) throws ParseException {
        final Map<String, Object> m = new HashMap<>();
        m.put(pathCol, t.toString().toLowerCase() + Dispatcher.DELIMITER + path);
        m.put(valueCol, value);

        return ServerManager.putValue(parseClass, m);

    }

    // TODO: inbal, should have delete method too...

    /*
     * public static void deleteInfo(final InfoType t) { final
     * ParseQuery<ParseObject> findQuery = ParseQuery.getQuery(parseClass);
     * findQuery.whereContains("info", t.toString().toLowerCase());
     * 
     * try { ServerManager.deleteById(parseClass,
     * findQuery.find().get(0).getObjectId()); } catch (final ParseException e)
     * { // TODO inbal - log or throw e.printStackTrace(); }
     * 
     * }
     */
    
    
    public static String getLastEntry(String... path) {
        final ParseQuery<ParseObject> findQuery = ParseQuery.getQuery(parseClass);

        findQuery.whereContains(pathCol, DispatcherCore.getPathAsString(path).toLowerCase());

        try {
            if (!findQuery.find().isEmpty()) {
                findQuery.orderByDescending("createdAt");
                
                //TODO: inbal - should return only value?
                return findQuery.find().get(0).getString(pathCol) + Dispatcher.SEPARATOR + findQuery.find().get(0).getString(valueCol);
            }
        } catch (ParseException e) {
            // TODO throw? return ""?
            e.printStackTrace();
        }

        return ""; // TODO: inbal.... same question applies..

    }

}
