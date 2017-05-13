package il.ac.technion.cs.smarthouse.system.user_information;

//import java.util.HashMap;
//import java.util.Map;


import org.parse4j.ParseException;
//import org.parse4j.ParseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.database.DatabaseManager;

/** This class saves information about a contact and implements the required API
 * for the system
 * @author Inbal Zukerman
 * @since Dec 28, 2016 */

public class Contact {

    private final String id;
    private final String name;
    private String phoneNumber;
    private String emailAddress;
    
    
    private static Logger log = LoggerFactory.getLogger(Contact.class);

    public Contact(final String id, final String name, final String phoneNumber, final String emailAddress) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        
    }

   /* TODO: inbal...

    public Contact(final ParseObject contactObj) {

        id = contactObj.getString("id");
        name = contactObj.getString("name");
        phoneNumber = contactObj.getString("phoneNumber");
        emailAddress = contactObj.getString("email");
        
        try {
			DatabaseManager.addContactInfo(id, name, phoneNumber, emailAddress);
		} catch (ParseException e) {
			log.error("Contact could not be saved", e);
		}

    }
*/
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
        
        try {
        	DatabaseManager.deleteContactInfo(id);
			DatabaseManager.addContactInfo(id, name, phoneNumber, emailAddress);
		} catch (ParseException e) {
			log.error("Contact could not be updated", e);
		}
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
        
        try {
        	DatabaseManager.deleteContactInfo(id);
			DatabaseManager.addContactInfo(id, name, phoneNumber, emailAddress);
		} catch (ParseException e) {
			log.error("Contact could not be updated", e);
		}
    }

   /*
	TODO: inbal...
    public Map<String, Object> contactMap() {
        final Map<String, Object> $ = new HashMap<>();

        $.put("id", id);
        $.put("name", name);
        $.put("phoneNumber", phoneNumber);
        $.put("email", emailAddress);

        return $;

    }

    public Map<String, Object> contactIdentifiresMap() {
        final Map<String, Object> $ = new HashMap<>();

        $.put("id", id);
        $.put("name", name);

        return $;

    }
    */

    // For debug mainly, leaving it implemented for future use
    @Override public String toString() {
        return "Contact:  id= " + id + "; name= " + name + "; phone= " + phoneNumber + "; email= " + emailAddress + ";\n";
    }

}
