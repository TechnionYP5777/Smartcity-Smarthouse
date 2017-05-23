package il.ac.technion.cs.smarthouse.database;

/**
 * @author Inbal Zukerman
 * @date May 13, 2017
 */

public enum InfoType {
	SENSOR_MESSAGE, USER$NAME, USER$PHONE_NUMBER, USER$HOME_ADDRESS, USER$ID, USER$CONTACT;

	@Override
	public String toString() {
		switch (this) {
		case SENSOR_MESSAGE:
			return "sensormessage";
		case USER$NAME:
			return "user.information.name";
		case USER$PHONE_NUMBER:
			return "user.information.phonenumber";
		case USER$HOME_ADDRESS:
			return "user.information.address";
		case USER$ID:
			return "user.information.id";
		case USER$CONTACT:
			return "user.contact";
		default:
			return "";
		}
	}
}
