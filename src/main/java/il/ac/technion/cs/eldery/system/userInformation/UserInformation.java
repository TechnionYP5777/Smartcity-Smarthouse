package il.ac.technion.cs.eldery.system.userInformation;

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

import il.ac.technion.cs.eldery.system.Communicate;
import il.ac.technion.cs.eldery.system.EmergencyLevel;

/** @author Inbal Zukerman
 * @since Dec 29, 2016 */

public class UserInformation {

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
            ¢.printStackTrace();
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

        final Element userDetails = new Element("userDetails");

        final Element userId = new Element("Id");
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
            ¢.printStackTrace();
        }

    }

    public void alert(final String appId, final String msg, final EmergencyLevel elvl) {
        // basic implementation todo: heavily revise.
        final List<Contact> $ = emergencyContacts.getContacts(elvl);
        switch (elvl) {
            case NOTIFY_ELDERLY:
                // todo: add to notification feed. optional - a separate queue
                // for this elvl notifs.
                break;
            case SMS_EMERGENCY_CONTACT:
                $.stream().forEach(c -> Communicate.throughSms(c.getPhoneNumber(), msg));
                break;
            case CALL_EMERGENCY_CONTACT:
            case CONTACT_HOSPITAL:
            case CONTACT_POLICE:
            case CONTACT_FIRE_FIGHTERS:
                $.stream().forEach(c -> Communicate.throughPhone(c.getPhoneNumber()));
                break;
            case EMAIL_EMERGENCY_CONTACT:
                $.stream().forEach(c -> Communicate.throughEmailFromHere(c.getEmailAddress(), msg));
                break;
            default:
                // todo: whats the desired behaviour?
                break;
        }
        // todo:add to notifications queue
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

}
