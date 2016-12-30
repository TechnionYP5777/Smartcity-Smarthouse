package il.ac.technion.cs.eldery.system.userInformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import il.ac.technion.cs.eldery.system.EmergencyLevel;

/** @author Inbal Zukerman
 * @since Dec 29, 2016 */

public class ContactsInformation {

    private final Map<EmergencyLevel, Map<String, Contact>> data = new HashMap<>();

    public ContactsInformation() {
        for (final EmergencyLevel elevel : EmergencyLevel.values())
            data.put(elevel, new HashMap<>());
    }

    public void addContact(final Contact c, final EmergencyLevel elevel) {

        data.get(elevel).put(c.getId(), c);

    }

    public Contact getContact(final String id) {
        for (final EmergencyLevel elvl : EmergencyLevel.values())
            if (data.get(elvl).containsKey(id))
                return data.get(elvl).get(id);
        return null;
    }

    public List<Contact> getContacts(final EmergencyLevel elvl) {
        final Map<String, Contact> temp = data.get(elvl);
        final ArrayList<Contact> $ = new ArrayList<>();
        for (final Contact ¢ : temp.values())
            $.add(¢);

        return $;
    }

    public List<Contact> getContacts() {
        final ArrayList<Contact> $ = new ArrayList<>();
        Map<String, Contact> temp;

        for (final EmergencyLevel elvl : EmergencyLevel.values()) {
            temp = data.get(elvl);
            $.addAll(temp.values());
        }

        return $;
    }

}
