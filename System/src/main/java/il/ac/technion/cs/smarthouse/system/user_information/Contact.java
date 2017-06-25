package il.ac.technion.cs.smarthouse.system.user_information;

import com.google.gson.annotations.Expose;

/**
 * This class saves information about a contact and implements the required API
 * for the system
 * 
 * @author Inbal Zukerman
 * @since Dec 28, 2016
 */

public class Contact {

    @Expose private final String id;
    @Expose private final String name;
    @Expose private String phoneNumber;
    @Expose private String emailAddress;

    public Contact(final String id, final String name, final String phoneNumber, final String emailAddress) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;

    }

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

    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;

    }

    // For debug mainly, leaving it implemented for future use
    @Override
    public String toString() {
        return "Contact:  id= " + id + "; name= " + name + "; phone= " + phoneNumber + "; email= " + emailAddress
                        + ";\n";
    }

}
