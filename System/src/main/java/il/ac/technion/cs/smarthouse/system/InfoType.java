package il.ac.technion.cs.smarthouse.system;

/**
 * @author Inbal Zukerman
 * @date May 13, 2017
 */

public enum InfoType {

    SENSOR("sensor"),
    USER("user"),
    NAME("name"),
    PHONE_NUMBER("phone_numeber"),
    HOME_ADDRESS("home_address"),
    ID("id"),
    CONTACT("contact"),
    SYSTEM("system"),
    SAVEALL("saveall");

    private final String strInfoType;

    private InfoType(String strIT) {
        strInfoType = strIT;
    }

    @Override
    public String toString() {
        return this.strInfoType;
    }
}
