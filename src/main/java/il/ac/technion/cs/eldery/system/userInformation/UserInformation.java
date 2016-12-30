package il.ac.technion.cs.eldery.system.userInformation;

import java.util.List;

import il.ac.technion.cs.eldery.system.EmergencyLevel;

/** @author Inbal Zukerman
 * @since Dec 29, 2016 */

public class UserInformation {

    private String name;
    private String id;
    private String phoneNumber;
    private String homeAddress;
    private final ContactsInformation emergencyContacts = new ContactsInformation();

    public UserInformation() {

    }

    public UserInformation(final String name, final String id, final String phoneNumber, final String homeAddress) {

        this.name = name;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.homeAddress = homeAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(final String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    // the next methods might seem redundant, but when we will change this class
    // to save a profile for each application separately, these methods will be
    // changed to receive also appId.
    // In my opinion implementing them now will be easier in the future when the
    // changes mentioned above will take place

    public Contact getContact(final String contactId) {
        return emergencyContacts.getContact(contactId);
    }

    public List<Contact> getContacts(final EmergencyLevel elvl) {
        return emergencyContacts.getContacts(elvl);
    }

    public List<Contact> getContacts() {
        return emergencyContacts.getContacts();
    }

}
