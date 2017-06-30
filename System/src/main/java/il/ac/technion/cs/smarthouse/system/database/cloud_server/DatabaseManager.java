package il.ac.technion.cs.smarthouse.system.database.cloud_server;

import java.util.HashMap;
import java.util.Map;

import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.file_system.PathBuilder;

/**
 * @author Inbal Zukerman
 * @since May 13, 2017
 * 
 *        This class supply the methods to communicate with the Parse server and
 *        manage it.
 */
public class DatabaseManager implements DatabaseAPI {

    public static String parseClass = "mainDB";
    public static String pathCol = "path";
    public static String valueCol = "value";

    private final ServerManager serverManager = new ServerManager();

    private static Logger log = LoggerFactory.getLogger(DatabaseManager.class);

    public static class DataEntry {
        public String path;
        public String data;

        public DataEntry(final String p, final String d) {
            path = p;
            data = d;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * il.ac.technion.cs.smarthouse.system.database.cloud_server.DatabaseAPI#
     * addInfo(java.lang.String, java.lang.Object)
     */
    @Override
    public ParseObject addInfo(final String path, final Object value) throws ParseException {
        serverManager.initialize();

        final Map<String, Object> m = new HashMap<>();
        m.put(pathCol, path);
        m.put(valueCol, value.toString());

        return serverManager.putValue(parseClass, m);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * il.ac.technion.cs.smarthouse.system.database.cloud_server.DatabaseAPI#
     * deleteInfo(il.ac.technion.cs.smarthouse.system.file_system.
     * FileSystemEntries)
     */
    @Override
    public void deleteInfo(final FileSystemEntries fsEntry) {
        serverManager.initialize();

        final ParseQuery<ParseObject> findQuery = ParseQuery.getQuery(parseClass);

        findQuery.whereStartsWith(pathCol, fsEntry.buildPath().toLowerCase());

        try {
            if (findQuery.find() == null)
                return;
            for (final ParseObject iterator : findQuery.find())
                serverManager.deleteById(parseClass, iterator.getObjectId());

        } catch (final ParseException e) {
            log.error("\n\tNo matching object was found on the server", e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * il.ac.technion.cs.smarthouse.system.database.cloud_server.DatabaseAPI#
     * deleteInfo(java.lang.String[])
     */
    @Override
    public void deleteInfo(final String... path) {
        serverManager.initialize();

        final ParseQuery<ParseObject> findQuery = ParseQuery.getQuery(parseClass);
        findQuery.whereMatches(pathCol, PathBuilder.buildPath(path).toLowerCase());

        try {
            if (findQuery.find() == null)
                return;
            for (final ParseObject iterator : findQuery.find())
                serverManager.deleteById(parseClass, iterator.getObjectId());

        } catch (final ParseException e) {
            log.error("\n\tNo matching object was found on the server", e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * il.ac.technion.cs.smarthouse.system.database.cloud_server.DatabaseAPI#
     * getLastEntry(java.lang.String[])
     */
    @Override
    public DataEntry getLastEntry(final String... path) {

        final ParseQuery<ParseObject> findQuery = ParseQuery.getQuery(parseClass);

        findQuery.whereStartsWith(pathCol, PathBuilder.buildPath(path).toLowerCase());

        try {
            if (findQuery.find() != null) {
                findQuery.orderByDescending("createdAt");
                return new DataEntry(findQuery.find().get(0).getString(pathCol),
                                findQuery.find().get(0).getString(valueCol));

            }
        } catch (final ParseException e) {
            log.error("\n\tA Parse exception has occured", e);
        }
        return new DataEntry("", null);

    }

}
