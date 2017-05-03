package il.ac.technion.cs.smarthouse.database;

import java.util.List;

import org.junit.AfterClass;
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

    private static final Contact contactA = new Contact("123", "Alon", "0508080123", "alon@gmail.com");
    private static final Contact contactB = new Contact("567", "Emma", "0546421211", "emma1@gmail.com");

    @BeforeClass public static void init() {

        DatabaseManager.initialize();

    }

    @Test @SuppressWarnings("static-method") public void testContactManager() {

        assert !ContactManager.isContactInDB(contactA);

        ContactManager.saveContact(contactA, EmergencyLevel.EMAIL_EMERGENCY_CONTACT);

        ParseQuery<ParseObject> countQuery = ParseQuery.getQuery("Contact");

        for (final String key : contactA.contactMap().keySet())
            countQuery.whereEqualTo(key, contactA.contactMap().get(key));

        try {
            Assert.assertEquals(1, countQuery.count());

        } catch (final ParseException e) {

            assert null != null;
        }

        assert ContactManager.isContactInDB(contactA);

        final ParseObject temp = DatabaseManager.getObjectByFields("Contact", contactA.contactMap());

        assert temp != null;
        Assert.assertEquals("Alon", temp.getString("name"));
        ContactManager.deleteContact(contactA.getId());

        countQuery = ParseQuery.getQuery("Contact");

        for (final String key : contactA.contactMap().keySet())
            countQuery.whereEqualTo(key, contactA.contactMap().get(key));

        try {

            Assert.assertEquals(0, countQuery.count());

        } catch (final ParseException e) {
            assert null != null;
        }
    }

    @Test @SuppressWarnings("static-method") public void testUpdates() {

        ContactManager.saveContact(contactA, EmergencyLevel.EMAIL_EMERGENCY_CONTACT);

        final Contact res = ContactManager.getContact(contactA.getId());
        assert res != null;

        Assert.assertEquals(contactA.getId(), res.getId());
        Assert.assertEquals(contactA.getName(), res.getName());
        Assert.assertEquals(contactA.getEmailAddress(), res.getEmailAddress());
        Assert.assertEquals(contactA.getPhoneNumber(), res.getPhoneNumber());

        res.setEmailAddress("mynewmail@gmail.com");
        res.setPhoneNumber("0267907070");

        ContactManager.updateContact(res);

        final Contact res2 = ContactManager.getContact(res.getId());
        Assert.assertEquals(res.getPhoneNumber(), res2.getPhoneNumber());
        Assert.assertEquals(res.getEmailAddress(), res2.getEmailAddress());

        assert !ContactManager.isContactInDB(contactB);

        ContactManager.updateContact(contactB);
        assert ContactManager.isContactInDB(contactB);

        ContactManager.deleteContact(contactA.getId());
        ContactManager.deleteContact(contactB.getId());
    }

    @Test @SuppressWarnings("static-method") public void testEmergencyLevels() {

        ContactManager.saveContact(contactA, EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
        ContactManager.updateContact(contactB);

        List<Contact> res = ContactManager.getContacts(EmergencyLevel.EMAIL_EMERGENCY_CONTACT);

        Assert.assertEquals(1, res.size());

        final Contact c = res.get(0);
        Assert.assertEquals(contactA.getId(), c.getId());
        Assert.assertEquals(contactA.getName(), c.getName());
        Assert.assertEquals(contactA.getEmailAddress(), c.getEmailAddress());
        Assert.assertEquals(contactA.getPhoneNumber(), c.getPhoneNumber());

        ContactManager.updateEmergencyLevel(contactA, EmergencyLevel.CALL_EMERGENCY_CONTACT);

        res = ContactManager.getContacts(EmergencyLevel.EMAIL_EMERGENCY_CONTACT);

        Assert.assertEquals(0, res.size());

        ContactManager.deleteContact(contactA.getId());
        ContactManager.deleteContact(contactB.getId());
    }

    @AfterClass public static void cleanup() {

        ContactManager.deleteContact(contactA.getId());
        ContactManager.deleteContact(contactB.getId());

    }
}
