package il.ac.technion.cs.smarthouse.system;

/**
 * @author Inbal Zukerman
 * @date May 13, 2017
 */

public enum InfoType {

    SENSOR("sensor"),
    USER("user"),
    NAME("name"),
    PHONE_NUMBER("phone_number"),
    HOME_ADDRESS("home_address"),
    ID("id"),
    CONTACT("contact"),
    SYSTEM("system"),
    SAVEALL("saveall"),
    TEST("test");

    private final String strInfoType;

    private InfoType(final String strIT) {
        strInfoType = strIT;
    }

    @Override
    public String toString() {
        return strInfoType;
    }
}
