package il.ac.technion.cs.eldery.applications.installer.exceptions;

import il.ac.technion.cs.eldery.applications.BaseApplication;

/** @author RON
 * @since 09-12-16 */
public class InstallerException extends Exception {
  private static final long serialVersionUID = 4633789134305606432L;
  public static final String MORE_THAN_ONE_IMPL = "Only one class can extend " + BaseApplication.class.getName();
  private int value;

  public InstallerException(String msg, int value) {
    super(msg);
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }
}
