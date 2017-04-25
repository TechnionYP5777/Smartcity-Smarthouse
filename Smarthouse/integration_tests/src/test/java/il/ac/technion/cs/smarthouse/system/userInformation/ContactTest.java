package il.ac.technion.cs.smarthouse.system.userInformation;

import org.jdom2.Element;
import org.junit.Assert;
import org.junit.Test;

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

}
