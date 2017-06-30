package il.ac.technion.cs.smarthouse.system.user_information;

import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;

import il.ac.technion.cs.smarthouse.system.services.alerts_service.EmergencyLevel;

/**
 * This class represents all the data which is relevant to the system about the
 * client
 * 
 * @author Inbal Zukerman
 * @since Dec 29, 2016
 */

public class UserInformation {

    @Expose private final String name;
    @Expose private final String id;
    @Expose private String phoneNumber;
    @Expose private String homeAddress;
    @Expose private final ContactsInformation emergencyContacts;

    public UserInformation(final String name, final String id, final String phoneNumber, final String homeAddress) {

        this.name = name;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.homeAddress = homeAddress;
        emergencyContacts = new ContactsInformation();

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

    // For debug mainly, leaving it implemented for future use
    @Override
    public String toString() {
        return "User:\nuserId= " + id + "\tname=" + name + "\tphone= " + phoneNumber + "\taddress= " + homeAddress
                        + "\n" + emergencyContacts;

    }

    /**
     * Adding a contact with a certain emergency level
     * 
     * @param c
     *            the contact to add
     * @param elevel
     */
    public void addContact(final Contact c, final EmergencyLevel elevel) {
        emergencyContacts.addContact(c, elevel);
    }

    /**
     * Remove a contact with a certain ID
     * 
     * @param contactID
     */
    public void removeContact(final String contactID) {
        emergencyContacts.removeContact(contactID);

    }

    /**
     * Retrieves a contact with a certain ID
     * 
     * @param contactId
     * @return
     */
    public Contact getContact(final String contactId) {
        return emergencyContacts.getContact(contactId);
    }

    /**
     * Retrieves all contacts with a certain emergency level
     * 
     * @param elvl
     * @return a list of all contacts with elvl as emergency level
     */
    public List<Contact> getContacts(final EmergencyLevel elvl) {
        return emergencyContacts.getContacts(elvl);
    }

    /**
     * 
     * @return All contacts
     */
    public List<Contact> getContacts() {
        return emergencyContacts.getContacts();
    }

    /**
     * This method sets the emergency level of a contact
     * 
     * @param id
     *            the contact's id whom emergency level we would like to change
     * @param eLevel
     */
    public void setContactEmergencyLevel(final String id, final String eLevel) {
        emergencyContacts.setContactEmergencyLevel(id, EmergencyLevel.fromString(eLevel));
    }

    /**
     * @return a map with all contacts with their emergency levels
     */
    public Map<Contact, EmergencyLevel> getContactsWithElevel() {
        return this.emergencyContacts.getContactsWithElevel();
    }
}
