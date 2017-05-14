package il.ac.technion.cs.smarthouse.system.userInformation;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.database.DatabaseManager;
import il.ac.technion.cs.smarthouse.database.InfoType;
import il.ac.technion.cs.smarthouse.database.ServerManager;
import il.ac.technion.cs.smarthouse.system.EmergencyLevel;
import il.ac.technion.cs.smarthouse.system.user_information.Contact;
import il.ac.technion.cs.smarthouse.system.user_information.UserInformation;

/**
 * 
 * @author Inbal Zukerman
 */
public class UserInformationTest {

	
    private final UserInformation userInfo = new UserInformation("Alon", "789", "0509535200", "Hertzel avn. 7, Jerusalem");
    private final Contact contactA = new Contact("123", "Dan", "0508080123", "alon@gmail.com");
    private final Contact contactB = new Contact("456", "Miri", "0547887261", "miri100@hotmail.com");

    
    @BeforeClass public static void init(){
    	ServerManager.initialize();
    }
    
    
    
    @Test public void initTest() {
        Assert.assertEquals("Alon", userInfo.getName());
        Assert.assertEquals("789", userInfo.getId());
        Assert.assertEquals("0509535200", userInfo.getPhoneNumber());
        Assert.assertEquals("Hertzel avn. 7, Jerusalem", userInfo.getHomeAddress());
    }

    @Test public void settersTest() {
        userInfo.setHomeAddress("Mlal 18, Haifa");
        Assert.assertEquals("Mlal 18, Haifa", userInfo.getHomeAddress());

        userInfo.setPhoneNumber("026895246");
        Assert.assertEquals("026895246", userInfo.getPhoneNumber());
    }

    @Test public void contactsTest() {
        userInfo.addContact(contactA, EmergencyLevel.CALL_EMERGENCY_CONTACT);
        userInfo.addContact(contactB, EmergencyLevel.SMS_EMERGENCY_CONTACT);

        List<Contact> temp = userInfo.getContacts(EmergencyLevel.CONTACT_FIRE_FIGHTERS);
        Assert.assertEquals(0, temp.size());

        temp = userInfo.getContacts(EmergencyLevel.SMS_EMERGENCY_CONTACT);
        Assert.assertEquals(1, temp.size());
        assert temp.contains(contactB);

        temp = userInfo.getContacts();
        assert temp.contains(contactA);
        assert temp.contains(contactB);

        Contact tempContact = userInfo.getContact("000");
        Assert.assertNull(tempContact);

        tempContact = userInfo.getContact("123");
        assert tempContact != null;

        userInfo.setContactEmergencyLevel("123", "CONTACT_HOSPITAL");
        temp = userInfo.getContacts(EmergencyLevel.CONTACT_HOSPITAL);
        Assert.assertEquals(1, temp.size());
        assert temp.contains(contactA);
        
        userInfo.removeContact(contactA.getId());
        userInfo.removeContact(contactB.getId());
    }

   

    @Test public void toStringTest() {
        assert userInfo + "" != null;
        Assert.assertEquals("User:\nuserId= 789\tname=Alon\tphone= 0509535200\taddress= Hertzel avn. 7, Jerusalem\n", userInfo + "");
    }
    
    @AfterClass public static void cleanup(){
    	//TODO: inbal
    	
    }

}
