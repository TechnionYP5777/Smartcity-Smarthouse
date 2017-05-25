package il.ac.technion.cs.smarthouse.system.user_information;

import java.util.List;

import il.ac.technion.cs.smarthouse.system.EmergencyLevel;

/**
 * This class represents all the data which is relevant to the system about the
 * client
 * 
 * @author Inbal Zukerman
 * @since Dec 29, 2016
 */

public class UserInformation {

    private final String name;
    private final String id;
    private String phoneNumber;
    private String homeAddress;
    private final ContactsInformation emergencyContacts;

    public UserInformation(final String name, final String id, final String phoneNumber, final String homeAddress) {

        this.name = name;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.homeAddress = homeAddress;
        emergencyContacts = new ContactsInformation();

        // TODO: inbal!
        /*
         * DatabaseManager.addInfo(InfoType.NAME, name);
         * DatabaseManager.addInfo(InfoType.ID, id);
         * DatabaseManager.addInfo(InfoType.PHONE_NUMBER, phoneNumber);
         * DatabaseManager.addInfo(InfoType.HOME_ADDRESS, homeAddress);
         */

    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;

        // TODO: inbal!
        /*
         * DatabaseManager.deleteInfo(InfoType.PHONE_NUMBER);
         * DatabaseManager.addInfo(InfoType.PHONE_NUMBER, phoneNumber);
         */

    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(final String homeAddress) {
        this.homeAddress = homeAddress;

        // TODO: inbal!
        /*
         * DatabaseManager.deleteInfo(InfoType.HOME_ADDRESS);
         * DatabaseManager.addInfo(InfoType.HOME_ADDRESS, homeAddress);
         */

    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    // For debug mainly, leaving it implemented for future use
    @Override
    public String toString() {
        return "User:\nuserId= " + id + "\tname=" + name + "\tphone= " + phoneNumber + "\taddress= " + homeAddress
                        + "\n" + emergencyContacts;

    }

    public void addContact(final Contact c, final EmergencyLevel elevel) {
        emergencyContacts.addContact(c, elevel);
    }

    public void removeContact(final String contactID) {
        emergencyContacts.removeContact(contactID);

    }

    public Contact getContact(final String contactId) {
        return emergencyContacts.getContact(contactId);
    }

    public List<Contact> getContacts(final EmergencyLevel elvl) {
        return emergencyContacts.getContacts(elvl);
    }

    public List<Contact> getContacts() {
        return emergencyContacts.getContacts();
    }

    public void setContactEmergencyLevel(final String id, final String eLevel) {
        emergencyContacts.setContactEmergencyLevel(id, EmergencyLevel.fromString(eLevel));
    }

}
