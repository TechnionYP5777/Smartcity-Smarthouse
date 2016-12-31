package il.ac.technion.cs.eldery.system.userInformation;

import org.jdom2.Element;

/** @author Inbal Zukerman
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

        // Creating the root element
        Element $ = new Element("contact");

        Element contactId = new Element("Id");
        contactId.setText(this.id);

        Element contactName = new Element("name");
        contactName.setText(this.name);

        Element contactPhoneNum = new Element("phoneNumber");
        contactPhoneNum.setText(this.phoneNumber);

        Element contactEmail = new Element("email");
        contactEmail.setText(this.emailAddress);

        $.addContent(contactId);
        $.addContent(contactName);
        $.addContent(contactPhoneNum);
        $.addContent(contactEmail);

        return $;

    }
}
