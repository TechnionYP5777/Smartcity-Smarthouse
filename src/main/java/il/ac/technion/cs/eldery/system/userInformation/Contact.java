package il.ac.technion.cs.eldery.system.userInformation;

/** @author Inbal Zukerman
 * @since Dec 28, 2016 */
public class Contact {

    private final String id;
    private final String name;
    private String phoneNumber;

    public Contact(final String id, final String name, final String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;

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

}
