package il.ac.technion.cs.smarthouse.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;

import il.ac.technion.cs.smarthouse.system.EmergencyLevel;
import il.ac.technion.cs.smarthouse.system.user_information.Contact;

/** @author Inbal Zukerman
 * @date Apr 22, 2017 */

public class ContactManager {

    public static boolean isContactInDB(Contact c) {

        return DatabaseManager.getObjectByFields("Contact", c.contactIdentifiresMap()) != null;
    }

    public static void saveContact(Contact c, EmergencyLevel l) {
        Map<String, Object> contactMap = c.contactMap();
        contactMap.put("emergencyLevel", l == null ? "" : l + "");

        if (!isContactInDB(c))
            try {
                DatabaseManager.putValue("Contact", contactMap);
            } catch (ParseException ¢) {
                ¢.printStackTrace();
            }

    }

    public static Contact getContact(String id) {
        Map<String, Object> $ = new HashMap<>();
        $.put("id", id);
        ParseObject temp = DatabaseManager.getObjectByFields("Contact", $);
        return temp == null ? null
                : new Contact(temp.getString("id"), temp.getString("name"), temp.getString("phoneNumber"), temp.getString("email"));
    }

    private static List<ParseObject> getObjectsByElevel(EmergencyLevel l) {
        // TODO: l is null?
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Contact");

        query.whereEqualTo("emergencyLevel", l + "");

        try {

            if (query.find() != null) {

                return query.find();
            }

        } catch (ParseException e) {
            // TODO: log error

        }
        return null;
    }

    public static List<Contact> getContacts(EmergencyLevel l) {
        List<ParseObject> contactsObjs = getObjectsByElevel(l);

        List<Contact> res = new ArrayList<>();
        if (contactsObjs == null) {
            return res;
        }
        for (ParseObject contactObj : contactsObjs)
            res.add(new Contact(contactObj));

        return res;
    }

    public static void updateContact(Contact c) {

        if (!isContactInDB(c))
            saveContact(c, null);
        else
            DatabaseManager.update("Contact", DatabaseManager.getObjectByFields("Contact", c.contactIdentifiresMap()).getObjectId(), c.contactMap());

    }

    public static void updateEmergencyLevel(Contact c, EmergencyLevel l) {

        if (!isContactInDB(c))
            saveContact(c, l);
        else {
            Map<String, Object> $ = new HashMap<>();
            $.put("emergencyLevel", l == null ? "" : l + "");
            DatabaseManager.update("Contact", DatabaseManager.getObjectByFields("Contact", c.contactMap()).getObjectId(), $);
        }

    }

    public static void deleteContact(String id) {
        Map<String, Object> $ = new HashMap<>();
        $.put("id", id);
        ParseObject temp = DatabaseManager.getObjectByFields("Contact", $);
        if (temp != null)
            DatabaseManager.deleteById("Contact", temp.getObjectId());
    }

}
