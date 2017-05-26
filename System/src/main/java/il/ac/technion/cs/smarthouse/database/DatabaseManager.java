package il.ac.technion.cs.smarthouse.database;

import java.util.HashMap;
import java.util.Map;

import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public static String pathCol = "path";
    public static String valueCol = "value";

    private static Logger log = LoggerFactory.getLogger(DatabaseManager.class);

    public static ParseObject addInfo(final InfoType t, final String path, final String value) throws ParseException {
        final Map<String, Object> m = new HashMap<>();
        m.put(pathCol, t.toString().toLowerCase() + Dispatcher.DELIMITER + path);
        m.put(valueCol, value);

        return ServerManager.putValue(parseClass, m);

    }

    public static void deleteInfo(InfoType infoType) {
        final ParseQuery<ParseObject> findQuery = ParseQuery.getQuery(parseClass);
        findQuery.whereStartsWith(pathCol, infoType.toString().toLowerCase());

        try {
            ServerManager.deleteById(parseClass, findQuery.find().get(0).getObjectId());
        } catch (final ParseException e) {
            log.error("No matching object was found on the server", e);
        }

    }

    public static void deleteInfo(String... path) {
        final ParseQuery<ParseObject> findQuery = ParseQuery.getQuery(parseClass);
        findQuery.whereMatches(pathCol, DispatcherCore.getPathAsString(path).toLowerCase());

        try {
            for (ParseObject iterator : findQuery.find()) {
                ServerManager.deleteById(parseClass, iterator.getObjectId());
            }

        } catch (final ParseException e) {
            log.error("No matching object was found on the server", e);
        }

    }

    public static String getLastEntry(String... path) {
        final ParseQuery<ParseObject> findQuery = ParseQuery.getQuery(parseClass);

        findQuery.whereContains(pathCol, DispatcherCore.getPathAsString(path).toLowerCase());

        try {
            if (!findQuery.find().isEmpty()) {
                findQuery.orderByDescending("createdAt");

                // TODO: inbal - should return only value?
                return findQuery.find().get(0).getString(pathCol) + Dispatcher.SEPARATOR
                                + findQuery.find().get(0).getString(valueCol);
            }
        } catch (ParseException e) {
            log.error("A Parse exception has occured", e);
        }

        return ""; // TODO: inbal - should throw?

    }

}
