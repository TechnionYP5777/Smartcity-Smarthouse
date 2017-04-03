package il.ac.technion.cs.smarthouse.database;

import java.util.Map;

import org.parse4j.Parse;
import org.parse4j.ParseException;

import org.parse4j.ParseObject;
import org.parse4j.callback.DeleteCallback;
import org.parse4j.callback.SaveCallback;

/** @author Inbal Zukerman
 * @date Mar 31, 2017 */

public abstract class DatabaseManager {
    // public static final String serverUrl =
    // "https://smartcity-smarthouse.herokuapp.com/parse";
    public static final String appId = "smartcity-smarthouse";
    public static final String restAPIKey = "ag9h-84j3-ked2-94j5";

    private static boolean init;

    private DatabaseManager() {

    }

    public static void initialize() {

        if (init)
            return;
        Parse.initialize(appId, restAPIKey);
        init = true;
    }

    /** @param objectClass
     * @param fields Map any field name (string) to an object which will be
     *        saves as the ParseObject
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
     *        saves as the ParseObject
     * @param c callback which will let us get the result */
    public static void putValue(final String objectClass, Map<String, Object> fields, SaveCallback c) {
        final ParseObject obj = new ParseObject(objectClass);
        for (String key : fields.keySet())
            obj.put(key, fields.get(key));
        obj.saveInBackground(c);
    }

    /** This method deletes an object in the background from class @objectClass
     * with @id */
    public static void deleteById(final String objectClass, String id, DeleteCallback c) {
        final ParseObject obj = new ParseObject(objectClass);
        obj.setObjectId(id);
        obj.deleteInBackground(c);
    }


}
