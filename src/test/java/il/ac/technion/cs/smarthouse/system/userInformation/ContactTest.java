package il.ac.technion.cs.smarthouse.system.userInformation;

import java.util.Map;

import org.jdom2.Element;
import org.junit.Assert;
import org.junit.Test;
import org.parse4j.ParseObject;

import il.ac.technion.cs.smarthouse.system.user_information.Contact;

/** @author Inbal Zukerman
 * @since Dec 28, 2016 */
public class ContactTest {

    private final Contact contactA = new Contact("123", "Alon", "0508080123", "alon@gmail.com");

    @Test public void initializationTest() {

        Assert.assertEquals(contactA.getId(), "123");
        Assert.assertEquals(contactA.getName(), "Alon");
    }

    @Test public void phoneNumberTest() {
        Assert.assertEquals(contactA.getPhoneNumber(), "0508080123");
        contactA.setPhoneNumber("026798080");
        Assert.assertEquals(contactA.getPhoneNumber(), "026798080");
    }

    @Test public void emailAddressTest() {
        Assert.assertEquals(contactA.getEmailAddress(), "alon@gmail.com");
        contactA.setEmailAddress("alon100@gmail.com");
        Assert.assertEquals(contactA.getEmailAddress(), "alon100@gmail.com");
    }

    @Test public void xmlTest() {
        final Element contactAElement = contactA.toXmlElement();
        final Contact newContactA = new Contact(contactAElement);

        assert newContactA != null;
        Assert.assertEquals(contactA.getId(), newContactA.getId());
        Assert.assertEquals(contactA.getName(), newContactA.getName());
        Assert.assertEquals(contactA.getPhoneNumber(), newContactA.getPhoneNumber());
        Assert.assertEquals(contactA.getEmailAddress(), newContactA.getEmailAddress());
    }

    @Test public void toStringTest() {
        assert contactA + "" != null;
        Assert.assertEquals("Contact:  id= 123; name= Alon; phone= 0508080123; email= alon@gmail.com;\n", contactA + "");
    }

    @Test public void contactMapsTest() {
        final Map<String, Object> cm = contactA.contactMap();
        Assert.assertEquals("123", cm.get("id"));
        Assert.assertEquals("Alon", cm.get("name"));
        Assert.assertEquals("0508080123", cm.get("phoneNumber"));
        Assert.assertEquals("alon@gmail.com", cm.get("email"));

        final Map<String, Object> cim = contactA.contactIdentifiresMap();
        Assert.assertEquals(cm.get("id"), cim.get("id"));
        Assert.assertEquals(cm.get("name"), cim.get("name"));

        final ParseObject contactObject = ParseObject.create("ContactTest");

        contactObject.put("id", contactA.getId());
        contactObject.put("name", contactA.getName());
        contactObject.put("phoneNumber", contactA.getPhoneNumber());
        contactObject.put("email", contactA.getEmailAddress());

        final Contact fromPO = new Contact(contactObject);
        Assert.assertEquals(contactA.getId(), fromPO.getId());
        Assert.assertEquals(contactA.getName(), fromPO.getName());
        Assert.assertEquals(contactA.getPhoneNumber(), fromPO.getPhoneNumber());
        Assert.assertEquals(contactA.getEmailAddress(), fromPO.getEmailAddress());

    }
}
