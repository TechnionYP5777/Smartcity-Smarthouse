package il.ac.technion.cs.smarthouse.database;

import java.util.HashMap;
import java.util.Map;

import org.parse4j.Parse;
import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.parse4j.callback.SaveCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This class implements an API to work with the database on the server.
 * @author Inbal Zukerman
 * @date Mar 31, 2017 */

public abstract class DatabaseManager {

    private static Logger log = LoggerFactory.getLogger(DatabaseManager.class);

    public static final String serverUrl = "http://sc-smarthouse.herokuapp.com/parse";
    public static final String appId = "myAppId";
    public static final String restAPIKey = "ag9h-84j3-ked2-94j5";

    private static boolean init;

    public static void initialize() {

        log.info("Initializing Database");
        if (init)
            return;

        Parse.initialize(appId, restAPIKey, serverUrl);
        init = true;
    }

    /** This method checks if an object is saved in the database
     * @param objectClass
     * @param id
     * @return true if is in the DB, false otherwise */
    public static boolean isInDB(final String objectClass, final String id) {
        Map<String, Object> vals = new HashMap<>();
        vals.put("objectId", id);
        return DatabaseManager.getObjectByFields(objectClass, vals) != null;
    }

    /** @param objectClass
     * @param fields Map any field name (string) to an object which will be
     *        saved as the ParseObject
     * @return The ParseObject which was created
     * @throws ParseException */
    public static ParseObject putValue(final String objectClass, final Map<String, Object> fields) {
        final ParseObject $ = new ParseObject(objectClass);
        for (final String key : fields.keySet())
            $.put(key, fields.get(key));
        try {

            $.save();
        } catch (ParseException e) {
            log.error("A parse exception has happened", e);
        }
        return $;
    }

    /** @param objectClass
     * @param fields Map any field name (string) to an object which will be
     *        saved as the ParseObject
     * @param c callback which will let us get the result */
    public static void putValue(final String objectClass, final Map<String, Object> fields, final SaveCallback c) {
        final ParseObject obj = new ParseObject(objectClass);
        for (final String key : fields.keySet())
            obj.put(key, fields.get(key));

        obj.saveInBackground(c);
    }

    /** This method deletes an object from class @objectClass with @id */
    public static void deleteById(final String objectClass, final String id) {
        if (!isInDB(objectClass, id))
            return;
        final ParseObject obj = new ParseObject(objectClass);
        obj.setObjectId(id);
        try {
            obj.delete();
        } catch (final ParseException ¢) {
            log.error("A parse exception has happened", ¢);
        }
    }

    /** Retrieves an item from the server
     * @param objectClass
     * @param id The item's id
     * @return ParseObject Result of query if it was successful, null o.w.
     * @throws ParseException */
    public static ParseObject getValue(final String objectClass, final String id) {
        try {
            return ParseQuery.getQuery(objectClass).get(id);
        } catch (final ParseException ¢) {
            log.error("A parse exception has happened", ¢);
        }
        return null;
    }

    /** This method updates an object with new values.
     * @param objectClass The class of the object which should be updated
     * @param id The id of the object which should be updated
     * @param values The new Values to be saved in the object's fields. Fields
     *        which are not included in this mapping will remain untouched. */
    public static void update(final String objectClass, final String id, final Map<String, Object> values) {

        try {
            final ParseObject res = ParseQuery.getQuery(objectClass).get(id);
            if (res == null)
                return;
            for (final String key : values.keySet())
                res.put(key, values.get(key));
            res.save();

        } catch (final ParseException e) {
            log.error("A parse exception has happened", e);
        }

    }

    /** This method returns (in the callback) the object in objectClass with
     * values matching to the values mapping
     * @param objectClass
     * @param values Map any field name to a value */
    public static ParseObject getObjectByFields(final String objectClass, final Map<String, Object> values) {
        final ParseQuery<ParseObject> query = ParseQuery.getQuery(objectClass);
        for (final String key : values.keySet())
            query.whereEqualTo(key, values.get(key));

        try {

            if (query.find() != null)
                return query.find().get(0);

        } catch (final ParseException e) {
            log.error("A parse exception has happened", e);

        }
        return null;

    }

}
