package il.ac.technion.cs.smarthouse.database;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
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
public class DatabaseManager implements DatabaseAPI {

    public static String parseClass = "mainDB";
    public static String pathCol = "path";
    public static String valueCol = "value";

    private final ServerManager serverManager = new ServerManager();

    private static Logger log = LoggerFactory.getLogger(DatabaseManager.class);

    @Override
    public ParseObject addInfo(final InfoType t, final String path, final String value) throws ParseException {
        serverManager.initialize();

        final Map<String, Object> m = new HashMap<>();
        m.put(pathCol, t.toString().toLowerCase() + Dispatcher.DELIMITER + path);
        m.put(valueCol, value);

        return serverManager.putValue(parseClass, m);

    }

    @Override
    public void deleteInfo(final InfoType infoType) {
        serverManager.initialize();

        final ParseQuery<ParseObject> findQuery = ParseQuery.getQuery(parseClass);
        findQuery.whereStartsWith(pathCol, infoType.toString().toLowerCase());

        try {
            for (final ParseObject iterator : findQuery.find())
                serverManager.deleteById(parseClass, iterator.getObjectId());

        } catch (final ParseException e) {
            log.error("No matching object was found on the server", e);
        }

    }

    @Override
    public void deleteInfo(final String... path) {
        serverManager.initialize();

        final ParseQuery<ParseObject> findQuery = ParseQuery.getQuery(parseClass);
        findQuery.whereMatches(pathCol, DispatcherCore.getPathAsString(path).toLowerCase());

        try {
            for (final ParseObject iterator : findQuery.find())
                serverManager.deleteById(parseClass, iterator.getObjectId());

        } catch (final ParseException e) {
            log.error("No matching object was found on the server", e);
        }

    }

    @Override
    public String getLastEntry(final String... path) {

        final ParseQuery<ParseObject> findQuery = ParseQuery.getQuery(parseClass);

        findQuery.whereStartsWith(pathCol, DispatcherCore.getPathAsString(path).toLowerCase()); //TODO inbal, not prefix

        try {
            if (findQuery.find() != null) {
                findQuery.orderByDescending("createdAt");

                return findQuery.find().get(0).getString(pathCol) + Dispatcher.SEPARATOR
                                + findQuery.find().get(0).getString(valueCol);
            }
        } catch (final ParseException e) {
            log.error("A Parse exception has occured", e);
        }

        return ""; // TODO: inbal - should throw?

    }
    
    @Override
    public Collection<String> getPathChildren(String... path){
        final ParseQuery<ParseObject> findQuery = ParseQuery.getQuery(parseClass);

        findQuery.whereStartsWith(pathCol, DispatcherCore.getPathAsString(path).toLowerCase());
        

        Collection<String> res = new ArrayList<>(); //TODO: inbal
        try {
            for (final ParseObject iterator : findQuery.find()){
                res.add(iterator.getString(pathCol).replaceAll(DispatcherCore.getPathAsString(path), ""));  // TODO inbal
            }
               

        } catch (final ParseException e) {
            //TODO: inbal
        }
        
        return res;
    }

}
