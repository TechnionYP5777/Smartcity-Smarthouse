package il.ac.technion.cs.eldery.system.userInformation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import il.ac.technion.cs.eldery.system.EmergencyLevel;

/** @author Inbal Zukerman
 * @since Dec 29, 2016 */

public class UserInformation {

    private String name;
    private String id;
    private String phoneNumber;
    private String homeAddress;
    private final ContactsInformation emergencyContacts = new ContactsInformation();

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

    public void toXml(String fileName) {
        
        // Creating the root element
        Element user = new Element("user");
        Document doc = new Document(user);

        Element userDetails = new Element("userDetails");

        Element userId = new Element("Id");
        userId.setText(this.id);

        Element userName = new Element("name");
        userName.setText(this.name);

        Element userPhoneNum = new Element("phoneNumber");
        userPhoneNum.setText(this.phoneNumber);

        Element userHomeAddress = new Element("homeAddress");
        userHomeAddress.setText(this.homeAddress);

        userDetails.addContent(userId);
        userDetails.addContent(userName);
        userDetails.addContent(userPhoneNum);
        userDetails.addContent(userHomeAddress);

        Element contactsDetails = this.emergencyContacts.toXmlElement();

        doc.getRootElement().addContent(userDetails);
        doc.getRootElement().addContent(contactsDetails);

        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());

        try (FileWriter fw = new FileWriter(fileName);) {

            fw.write(xmlOutput.outputString(doc));
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace(); 
        }

    }

    
    
    public void alert(String appId, String msg, EmergencyLevel elvl){
        //TODO: Elia and Ron - Implement
    }
    // the next methods might seem redundant, but when we will change this class
    // to save a profile for each application separately, these methods will be
    // changed to receive also appId.
    // In my opinion implementing them now will be easier in the future when the
    // changes mentioned above will take place

    public void addContact(Contact c, EmergencyLevel elevel) {
        this.emergencyContacts.addContact(c, elevel);
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
