package il.ac.technion.cs.eldery.system.userInformation;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import il.ac.technion.cs.eldery.system.EmergencyLevel;

/** @author Inbal Zukerman
 * @since Dec 30, 2016 */

public class ContactsInformationTest {

    private final ContactsInformation contactsInfo = new ContactsInformation();
    private final Contact contactA = new Contact("123", "Alon", "0508080123", "alon@gmail.com");
    private final Contact contactB = new Contact("456", "Miri", "0547887261", "miri100@hotmail.com");

    @Test public void singleContactTest() {
        contactsInfo.addContact(contactA, EmergencyLevel.CALL_EMERGENCY_CONTACT);
        Assert.assertEquals(contactA, contactsInfo.getContact("123"));
        Assert.assertNull(contactsInfo.getContact("456"));
        contactsInfo.addContact(contactB, EmergencyLevel.SMS_EMERGENCY_CONTACT);
        Assert.assertEquals(contactB, contactsInfo.getContact("456"));
    }

    @Test public void getContactsTest() {
        contactsInfo.addContact(contactA, EmergencyLevel.CALL_EMERGENCY_CONTACT);
        contactsInfo.addContact(contactB, EmergencyLevel.SMS_EMERGENCY_CONTACT);

        List<Contact> temp = contactsInfo.getContacts();
        Assert.assertEquals(2, temp.size());
        Assert.assertTrue(temp.contains(contactA));
        Assert.assertTrue(temp.contains(contactB));

        temp = contactsInfo.getContacts(EmergencyLevel.CONTACT_FIRE_FIGHTERS);
        Assert.assertEquals(0, temp.size());

        temp = contactsInfo.getContacts(EmergencyLevel.CALL_EMERGENCY_CONTACT);
        Assert.assertEquals(1, temp.size());
        Assert.assertTrue(temp.contains(contactA));

        temp = contactsInfo.getContacts(EmergencyLevel.SMS_EMERGENCY_CONTACT);
        Assert.assertEquals(1, temp.size());
        Assert.assertTrue(temp.contains(contactB));

    }
}
