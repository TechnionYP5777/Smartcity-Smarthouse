package il.ac.technion.cs.smarthouse.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.EmergencyLevel;
import il.ac.technion.cs.smarthouse.system.user_information.Contact;

/** This class implements an API to manage contacts in the database on the
 * server.
 * @author Inbal Zukerman
 * @date Apr 22, 2017 */

public class ContactManager {

    private static Logger log = LoggerFactory.getLogger(ContactManager.class);

    /** This method checks if a contact is saved in the database
     * @param c The contact to be checked if saved in DB */
    public static boolean isContactInDB(final Contact c) {

        return DatabaseManager.getObjectByFields("Contact", c.contactIdentifiresMap()) != null;
    }

    /** This methods saves a contact in the DB
     * @param c the contact to be saved in the DB
     * @param l the emergency level of the new contact, might be null */
    public static void saveContact(final Contact c, final EmergencyLevel l) {
        final Map<String, Object> contactMap = c.contactMap();
        contactMap.put("emergencyLevel", l == null ? "" : l + "");

        if (!isContactInDB(c))
            DatabaseManager.putValue("Contact", contactMap);

    }

    /** Retrieves a contact by his id from the DB
     * @param id the contact's id
     * @return the contact by the data from the DB, or null if was not found */
    public static Contact getContact(final String id) {
        final Map<String, Object> $ = new HashMap<>();
        $.put("id", id);
        final ParseObject temp = DatabaseManager.getObjectByFields("Contact", $);
        return temp == null ? null : new Contact(temp);
    }

    /** help method which retrieves all save objects with a certain emergency
     * level */
    private static List<ParseObject> getObjectsByElevel(final EmergencyLevel l) {

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Contact");
        query.whereEqualTo("emergencyLevel", l == null ? "" : l + "");

        try {

            if (query.find() != null)
                return query.find();

        } catch (final ParseException e) {
            log.error("A parse exception has happened", e);

        }
        return null;
    }

    /** This method returns all contacts saved with a certain emergency level
     * @param l required emergency level */
    public static List<Contact> getContacts(final EmergencyLevel l) {
        final List<ParseObject> contactsObjs = getObjectsByElevel(l);

        final List<Contact> res = new ArrayList<>();
        if (contactsObjs != null)
            for (final ParseObject contactObj : contactsObjs)
                res.add(new Contact(contactObj));
        return res;
    }

    /** Update a contact on the database. If the contact is not saved on the DB,
     * he will be saved with a null emergency level
     * @param c the contact to save */
    public static void updateContact(final Contact c) {

        if (!isContactInDB(c))
            saveContact(c, null);
        else
            DatabaseManager.update("Contact", DatabaseManager.getObjectByFields("Contact", c.contactIdentifiresMap()).getObjectId(), c.contactMap());
    }

    /** Update a contact's emergency level. If the contact is not saved on the
     * DB, he will be saved.
     * @param c the contact to save
     * @param l the new emergency level */
    public static void updateEmergencyLevel(final Contact c, final EmergencyLevel l) {

        if (!isContactInDB(c))
            saveContact(c, l);
        else {
            final Map<String, Object> $ = new HashMap<>();
            $.put("emergencyLevel", l == null ? "" : l + "");
            DatabaseManager.update("Contact", DatabaseManager.getObjectByFields("Contact", c.contactMap()).getObjectId(), $);
        }

    }

    /** Delete a contact from the database.
     * @param id the ID of the contact to be deleted */
    public static void deleteContact(final String id) {
        final Map<String, Object> $ = new HashMap<>();
        $.put("id", id);
        final ParseObject temp = DatabaseManager.getObjectByFields("Contact", $);
        if (temp != null)
            DatabaseManager.deleteById("Contact", temp.getObjectId());
    }

}
