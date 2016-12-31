package il.ac.technion.cs.eldery.system.userInformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;

import il.ac.technion.cs.eldery.system.EmergencyLevel;

/** @author Inbal Zukerman
 * @since Dec 29, 2016 */

public class ContactsInformation {

    private final Map<EmergencyLevel, Map<String, Contact>> data = new HashMap<>();

    public ContactsInformation() {
        for (final EmergencyLevel elevel : EmergencyLevel.values())
            data.put(elevel, new HashMap<>());
    }

    /** Adds a contact with a specific emergency level
     * @param c contact to add
     * @param elevel emergency level to inform this contact at */
    public void addContact(final Contact c, final EmergencyLevel elevel) {
        data.get(elevel).put(c.getId(), c);

    }

    /** @param id the id of the contact required
     * @return the contact with the required id or null if does not exist */
    public Contact getContact(final String id) {
        for (final EmergencyLevel elvl : EmergencyLevel.values())
            if (data.get(elvl).containsKey(id))
                return data.get(elvl).get(id);
        return null;
    }

    /** @param elvl emergency level of the required contacts
     * @return list of all the contacts to be informed at required emergency
     *         level */
    public List<Contact> getContacts(final EmergencyLevel elvl) {
        final Map<String, Contact> temp = data.get(elvl);
        final ArrayList<Contact> $ = new ArrayList<>();
        for (final Contact ¢ : temp.values())
            $.add(¢);

        return $;
    }

    /** @return all the contacts saved in this instance */
    public List<Contact> getContacts() {
        final ArrayList<Contact> $ = new ArrayList<>();
        Map<String, Contact> temp;

        for (final EmergencyLevel elvl : EmergencyLevel.values()) {
            temp = data.get(elvl);
            $.addAll(temp.values());
        }

        return $;
    }

    public Element toXmlElement() {
        Element $ = new Element("contactsInformation");
        Map<String, Contact> temp;

        Element emergencyLevel;
        for (final EmergencyLevel elvl : EmergencyLevel.values()) {
            temp = data.get(elvl);
            if (temp.isEmpty())
                continue;

            emergencyLevel = new Element(elvl + "");

            for (Contact ¢ : temp.values())
                emergencyLevel.addContent(¢.toXmlElement());

            $.addContent(emergencyLevel);
        }

        return $;

    }
}
