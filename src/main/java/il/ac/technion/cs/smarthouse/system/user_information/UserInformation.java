package il.ac.technion.cs.smarthouse.system.user_information;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.EmergencyLevel;

/** This class represents all the data which is relevant to the system about the
 * client
 * @author Inbal Zukerman
 * @since Dec 29, 2016 */

public class UserInformation {

    private static Logger log = LoggerFactory.getLogger(UserInformation.class);

    private String name;
    private String id;
    private String phoneNumber;
    private String homeAddress;
    private ContactsInformation emergencyContacts;

    public UserInformation(final String name, final String id, final String phoneNumber, final String homeAddress) {

        this.name = name;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.homeAddress = homeAddress;
        emergencyContacts = new ContactsInformation();
    }

    public UserInformation(final File xmlFile) {
        final SAXBuilder sBuilder = new SAXBuilder();
        try {
            final List<Element> classInfo = sBuilder.build(xmlFile).getRootElement().getChildren();
            final Element userDetails = classInfo.get(0);
            name = userDetails.getChildText("name");
            id = userDetails.getChildText("Id");
            phoneNumber = userDetails.getChildText("phoneNumber");
            homeAddress = userDetails.getChildText("homeAddress");
            emergencyContacts = new ContactsInformation(classInfo.get(1));
        } catch (final IOException | JDOMException ¢) {
            log.error("I/O error occurred", ¢);
        }
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

    public void toXml(final String fileName) {

        // Creating the root element
        final Element user = new Element("user");
        final Document doc = new Document(user);

        final Element userDetails = new Element("userDetails"), userId = new Element("Id");

        userId.setText(id);

        final Element userName = new Element("name");
        userName.setText(name);

        final Element userPhoneNum = new Element("phoneNumber");
        userPhoneNum.setText(phoneNumber);

        final Element userHomeAddress = new Element("homeAddress");
        userHomeAddress.setText(homeAddress);

        userDetails.addContent(userId);
        userDetails.addContent(userName);
        userDetails.addContent(userPhoneNum);
        userDetails.addContent(userHomeAddress);

        final Element contactsDetails = emergencyContacts.toXmlElement();

        doc.getRootElement().addContent(userDetails);
        doc.getRootElement().addContent(contactsDetails);

        final XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());

        try (FileWriter fw = new FileWriter(fileName);) {

            fw.write(xmlOutput.outputString(doc));
            fw.flush();
            fw.close();
        } catch (final IOException ¢) {
            log.error("I/O error occurred", ¢);
        }

    }

    // For debug mainly, leaving it implemented for future use
    @Override public String toString() {
        return "User:\nuserId= " + id + "\tname=" + name + "\tphone= " + phoneNumber + "\taddress= " + homeAddress + "\n" + emergencyContacts;

    }

    // the next methods might seem redundant, but when we will change this class
    // to save a profile for each application separately, these methods will be
    // changed to receive also appId.
    // In my opinion implementing them now will be easier in the future when the
    // changes mentioned above will take place.

    public void addContact(final Contact c, final EmergencyLevel elevel) {
        emergencyContacts.addContact(c, elevel);
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
