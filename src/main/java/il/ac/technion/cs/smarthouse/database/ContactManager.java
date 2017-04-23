package il.ac.technion.cs.smarthouse.database;

import java.util.HashMap;
import java.util.Map;

import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.callback.GetCallback;
import org.parse4j.callback.SaveCallback;

import il.ac.technion.cs.smarthouse.system.EmergencyLevel;
import il.ac.technion.cs.smarthouse.system.user_information.Contact;

/** @author Inbal Zukerman
 * @date Apr 22, 2017 */

public class ContactManager {

    static void isContactInDB(Contact c, GetCallback<ParseObject> o) {
        DatabaseManager.getObjectByFields("Contact", c.contactMap(), new GetCallback<ParseObject>() {
            @Override public void done(ParseObject arg0, ParseException arg1) {
                o.done(arg1 == null && arg0 != null ? arg0 : null, null);
            }
        });
    }

    public static void saveContact(Contact c, EmergencyLevel l, SaveCallback o) {
        Map<String, Object> contactMap = c.contactMap();
        contactMap.put("emergencyLevel", (l + ""));

        isContactInDB(c, new GetCallback<ParseObject>() {

            @Override public void done(ParseObject arg0, ParseException arg1) {
                if (arg0 == null)
                    try {
                        DatabaseManager.putValue("Contact", contactMap);
                    } catch (ParseException ¢) {
                        // TODO Auto-generated catch block
                        ¢.printStackTrace();
                    }
            }
        });
    }

    public static Contact getContact(String id) {
        Map<String, Object> $ = new HashMap<>();
        $.put("id", id);
        DatabaseManager.getObjectByFields("Contact", $, new GetCallback<ParseObject>() {
            @Override public void done(ParseObject arg0, ParseException arg1) {
                if (arg0 != null && arg1 == null)
                    for (String key : arg0.keySet())
                        $.put(key, arg0.getString(key));
            }
        });

        return !$.containsKey("name") ? null
                : new Contact((String) $.get("id"), (String) $.get("name"), (String) $.get("phoneNumber"), (String) $.get("email"));
    }

    public static void updateContact(Contact c, EmergencyLevel l) {
        isContactInDB(c, new GetCallback<ParseObject>() {
            @Override public void done(ParseObject arg0, ParseException arg1) {
                Map<String, Object> cm = c.contactMap();

                if (arg0 != null)
                    DatabaseManager.update("Contact", arg0.getObjectId(), cm);
                else
                    saveContact(c, l, new SaveCallback() {
                        @Override public void done(ParseException arg0) {}
                    });
            }
        });

    }

}
