package il.ac.technion.cs.smarthouse.system.userInformation;

import java.util.List;

import org.jdom2.Element;
import org.junit.Assert;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.system.EmergencyLevel;
import il.ac.technion.cs.smarthouse.system.user_information.Contact;
import il.ac.technion.cs.smarthouse.system.user_information.ContactsInformation;

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
        assert temp.contains(contactA);
        assert temp.contains(contactB);

        temp = contactsInfo.getContacts(EmergencyLevel.CONTACT_FIRE_FIGHTERS);
        Assert.assertEquals(0, temp.size());

        temp = contactsInfo.getContacts(EmergencyLevel.CALL_EMERGENCY_CONTACT);
        Assert.assertEquals(1, temp.size());
        assert temp.contains(contactA);

        temp = contactsInfo.getContacts(EmergencyLevel.SMS_EMERGENCY_CONTACT);
        Assert.assertEquals(1, temp.size());
        assert temp.contains(contactB);

    }

    @Test public void setContactEmergencyLevelTest() {
        contactsInfo.addContact(contactA, EmergencyLevel.CALL_EMERGENCY_CONTACT);
        contactsInfo.addContact(contactB, EmergencyLevel.SMS_EMERGENCY_CONTACT);

        List<Contact> temp = contactsInfo.getContacts(EmergencyLevel.CALL_EMERGENCY_CONTACT);
        Assert.assertEquals(1, temp.size());
        assert temp.contains(contactA);

        temp = contactsInfo.getContacts(EmergencyLevel.SMS_EMERGENCY_CONTACT);
        Assert.assertEquals(1, temp.size());
        assert temp.contains(contactB);

        contactsInfo.setContactEmergencyLevel(contactA.getId(), EmergencyLevel.SMS_EMERGENCY_CONTACT);

        temp = contactsInfo.getContacts(EmergencyLevel.CALL_EMERGENCY_CONTACT);
        Assert.assertEquals(0, temp.size());

        temp = contactsInfo.getContacts(EmergencyLevel.SMS_EMERGENCY_CONTACT);
        Assert.assertEquals(2, temp.size());
        assert temp.contains(contactA);

    }

    @Test public void xmlTest() {
        contactsInfo.addContact(contactA, EmergencyLevel.CALL_EMERGENCY_CONTACT);
        contactsInfo.addContact(contactB, EmergencyLevel.SMS_EMERGENCY_CONTACT);

        final Element contactsInfoElement = contactsInfo.toXmlElement();
        final ContactsInformation newContactsInfo = new ContactsInformation(contactsInfoElement);

        assert newContactsInfo != null;
        final Contact newContactA = newContactsInfo.getContact(contactA.getId());
        assert newContactA != null;

        Assert.assertEquals(contactA.getId(), newContactA.getId());
        Assert.assertEquals(contactA.getName(), newContactA.getName());

        assert newContactsInfo.getContact(contactB.getId()) != null;

        assert newContactsInfo.getContacts(EmergencyLevel.CALL_EMERGENCY_CONTACT) != null;
        Assert.assertEquals(0, newContactsInfo.getContacts(EmergencyLevel.CONTACT_HOSPITAL).size());

    }

    @Test public void toStringTest() {
        assert contactsInfo + "" != null;
        Assert.assertEquals("", contactsInfo + "");

        contactsInfo.addContact(contactA, EmergencyLevel.CALL_EMERGENCY_CONTACT);
        assert contactsInfo + "" != null;

        Assert.assertEquals("Elvl is: CALL_EMERGENCY_CONTACT\n\tContact:  id= 123; name= Alon; phone= 0508080123; email= alon@gmail.com;\n\n",
                contactsInfo + "");

    }
}
