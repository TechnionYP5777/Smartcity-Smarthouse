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

    public static boolean isContactInDB(Contact c) {
        return DatabaseManager.getObjectByFields("Contact", c.contactMap()) != null;
    }

    public static void saveContact(Contact c, EmergencyLevel l) {
        Map<String, Object> contactMap = c.contactMap();
        contactMap.put("emergencyLevel", l + "");

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

    // TODO: it is inconvenient to send each time the elevel
    public static void updateContact(Contact c, EmergencyLevel l) {
        if (!isContactInDB(c))
            saveContact(c, l);
        else {
            Map<String, Object> cm = c.contactMap();
            DatabaseManager.update("Contact", DatabaseManager.getObjectByFields("Contact", cm).getObjectId(), cm);
        }

    }
    
    public static void deleteContact(String id){
        Map<String, Object> $ = new HashMap<>();
        $.put("id", id);
        ParseObject temp = DatabaseManager.getObjectByFields("Contact", $);
        if(temp != null){
            DatabaseManager.deleteById("Contact", temp.getObjectId());
        }
    }

}
