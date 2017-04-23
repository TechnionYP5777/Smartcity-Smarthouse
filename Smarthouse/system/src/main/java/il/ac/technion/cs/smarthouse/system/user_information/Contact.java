package il.ac.technion.cs.smarthouse.system.user_information;

import java.util.HashMap;
import java.util.Map;

import org.jdom2.Element;

/** This class saves information about a contact and implements the required API
 * for the system
 * @author Inbal Zukerman
 * @since Dec 28, 2016 */
public class Contact {

    private final String id;
    private final String name;
    private String phoneNumber;
    private String emailAddress;

    public Contact(final String id, final String name, final String phoneNumber, final String emailAddress) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;

    }

    public Contact(final Element contactElement) {

        id = contactElement.getChildText("Id");
        name = contactElement.getChildText("name");
        phoneNumber = contactElement.getChildText("phoneNumber");
        emailAddress = contactElement.getChildText("email");
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

    public Element toXmlElement() {

        final Element $ = new Element("contact"), contactId = new Element("Id");

        contactId.setText(id);

        final Element contactName = new Element("name");
        contactName.setText(name);

        final Element contactPhoneNum = new Element("phoneNumber");
        contactPhoneNum.setText(phoneNumber);

        final Element contactEmail = new Element("email");
        contactEmail.setText(emailAddress);

        $.addContent(contactId);
        $.addContent(contactName);
        $.addContent(contactPhoneNum);
        $.addContent(contactEmail);

        return $;

    }

    public Map<String, Object> contactMap() {
        Map<String, Object> $ = new HashMap<>();

        $.put("id", this.id);
        $.put("name", this.name);
        $.put("phoneNumber", this.phoneNumber);
        $.put("email", this.emailAddress);

        return $;

    }

    // For debug mainly, leaving it implemented for future use
    @Override public String toString() {
        return "Contact:  id= " + id + "; name= " + name + "; phone= " + phoneNumber + "; email= " + emailAddress + ";\n";
    }

}
