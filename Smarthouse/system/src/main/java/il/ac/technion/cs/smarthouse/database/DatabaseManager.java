package il.ac.technion.cs.smarthouse.database;

import java.util.List;
import java.util.Map;

import org.parse4j.Parse;
import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.parse4j.callback.FindCallback;
import org.parse4j.callback.GetCallback;
import org.parse4j.callback.SaveCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Inbal Zukerman
 * @date Mar 31, 2017 */

public abstract class DatabaseManager {

    private static Logger log = LoggerFactory.getLogger(DatabaseManager.class);

    public static final String serverUrl = "http://sc-smarthouse.herokuapp.com/parse";
    public static final String appId = "myAppId";
    public static final String restAPIKey = "ag9h-84j3-ked2-94j5";

    private static boolean init;

    private DatabaseManager() {

    }

    public static void initialize() {

        log.info("Initializing Database");
        if (init)
            return;

        Parse.initialize(appId, restAPIKey, serverUrl);
        init = true;
    }

    /** @param objectClass
     * @param fields Map any field name (string) to an object which will be
     *        saved as the ParseObject
     * @return The ParseObject which was created
     * @throws ParseException */
    public static ParseObject putValue(final String objectClass, Map<String, Object> fields) throws ParseException {
        final ParseObject $ = new ParseObject(objectClass);
        for (String key : fields.keySet())
            $.put(key, fields.get(key));
        $.save();
        return $;
    }

    /** @param objectClass
     * @param fields Map any field name (string) to an object which will be
     *        saved as the ParseObject
     * @param c callback which will let us get the result */
    public static void putValue(final String objectClass, Map<String, Object> fields, SaveCallback c) {
        final ParseObject obj = new ParseObject(objectClass);
        for (String key : fields.keySet())
            obj.put(key, fields.get(key));
        obj.saveInBackground(c);
    }

    /** This method deletes an object in the background from class @objectClass
     * with @id */
    public static void deleteById(final String objectClass, String id) {
        final ParseObject obj = new ParseObject(objectClass);
        obj.setObjectId(id);
        try {
            obj.delete();
        } catch (ParseException ¢) {
            // TODO Auto-generated catch block
            ¢.printStackTrace();
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
        } catch (ParseException ¢) {
            log.error("A parse exception has happened", ¢);
        }
        return null;
    }

    /** This method updates an object with new values.
     * @param objectClass The class of the object which should be updated
     * @param id The id of the object which should be updated
     * @param values The new Values to be saved in the object's fields. Fields
     *        which are not included in this mapping will remain untouched. */
    public static void update(final String objectClass, final String id, Map<String, Object> values) {

        ParseQuery.getQuery(objectClass).getInBackground(id, new GetCallback<ParseObject>() {
            @Override @SuppressWarnings("synthetic-access") public void done(ParseObject arg0, ParseException arg1) {
                if (arg0 == null || arg1 != null)
                    return;
                for (String key : values.keySet())
                    arg0.put(key, values.get(key));
                try {
                    arg0.save();
                } catch (ParseException ¢) {
                    log.error("A parse exception has happened", ¢);
                }
            }
        });
    }

    /** This method returns (in the callback) the object in objectClass with
     * values matching to the values mapping
     * @param objectClass
     * @param values Map any field name to a value
     * @param o GetCallback from which the objects will be retrieved */
    public static void getObjectByFields(final String objectClass, Map<String, Object> values, GetCallback<ParseObject> o) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(objectClass);
        for (String key : values.keySet())
            query.whereEqualTo(key, values.get(key));
        query.limit(1);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override public void done(List<ParseObject> arg0, ParseException arg1) {
                if (arg1 != null || arg0 == null)
                    o.done(null, arg1);
                else
                    o.done(arg0.get(0), null);

            }
        });
    }

}
