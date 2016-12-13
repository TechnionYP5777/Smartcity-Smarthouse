package il.ac.technion.cs.eldery.system.exceptions;

import il.ac.technion.cs.eldery.system.applications.SmartHouseApplication;

/** @author RON
 * @since 09-12-2016 */
public class AppInstallerException extends Exception {
    private static final long serialVersionUID = 4633789134305606432L;

    public static final String MORE_THAN_ONE_IMPL = "Only one class can extend " + SmartHouseApplication.class.getName();
    private int value;

    public AppInstallerException(final String msg, final int value) {
        super(msg);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(final int value) {
        this.value = value;
    }
}
