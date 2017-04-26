package il.ac.technion.cs.smarthouse.database;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.parse4j.ParseException;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;

import il.ac.technion.cs.smarthouse.system.EmergencyLevel;
import il.ac.technion.cs.smarthouse.system.user_information.Contact;

/** @author Inbal Zukerman
 * @date Apr 24, 2017 */
public class ContactManagerTest {

    private final Contact contactA = new Contact("123", "Alon", "0508080123", "alon@gmail.com");

    @BeforeClass public static void init() {

        DatabaseManager.initialize();

    }

    @Test public void testContactManager() {

        assert !ContactManager.isContactInDB(contactA);

        ContactManager.saveContact(contactA, EmergencyLevel.EMAIL_EMERGENCY_CONTACT);

        ParseQuery<ParseObject> countQuery = ParseQuery.getQuery("Contact");
        for (String key : contactA.contactMap().keySet())
            countQuery.whereEqualTo(key, contactA.contactMap().get(key));
        try {
            Assert.assertEquals(1, countQuery.count());
        } catch (ParseException e) {
            assert null != null;
        }

        assert ContactManager.isContactInDB(contactA);

        ParseObject temp = DatabaseManager.getObjectByFields("Contact", contactA.contactMap());

        assert temp != null;
        Assert.assertEquals("Alon", temp.getString("name"));

        ContactManager.deleteContact(contactA.getId());

        countQuery = ParseQuery.getQuery("Contact");
        for (String key : contactA.contactMap().keySet())
            countQuery.whereEqualTo(key, contactA.contactMap().get(key));
        try {
            Assert.assertEquals(0, countQuery.count());
        } catch (ParseException e) {
            assert null != null;
        }

    }

}
