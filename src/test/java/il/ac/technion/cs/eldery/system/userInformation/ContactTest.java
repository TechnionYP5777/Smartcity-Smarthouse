package il.ac.technion.cs.eldery.system.userInformation;

import org.junit.Assert;
import org.junit.Test;

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

}
