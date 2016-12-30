package il.ac.technion.cs.eldery.system.userInformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import il.ac.technion.cs.eldery.system.*;

/** @author Inbal Zukerman
 * @since Dec 29, 2016 */

public class ContactsInformation {

    private Map<EmergencyLevel, Map<String, Contact>> data = new HashMap<>();

    public ContactsInformation() {
        for (EmergencyLevel elevel : EmergencyLevel.values())
            data.put(elevel, new HashMap<>());
    }

    public void addContact(Contact c, EmergencyLevel elevel) {

        data.get(elevel).put(c.getId(), c);

    }

    public Contact getContact(String id) {
        for (EmergencyLevel elvl : EmergencyLevel.values())
            if (this.data.get(elvl).containsKey(id))
                return this.data.get(elvl).get(id);
        return null;
    }

    public List<Contact> getContacts(EmergencyLevel elvl) {
        Map<String, Contact> temp = this.data.get(elvl);
        ArrayList<Contact> $ = new ArrayList<>();
        for (Contact ¢ : temp.values())
            $.add(¢);

        return $;
    }

    public List<Contact> getContacts() {
        ArrayList<Contact> $ = new ArrayList<>();
        Map<String, Contact> temp;

        for (EmergencyLevel elvl : EmergencyLevel.values()) {
            temp = this.data.get(elvl);
            $.addAll(temp.values());
        }

        return $;
    }

}
