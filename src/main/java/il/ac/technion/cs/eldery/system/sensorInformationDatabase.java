package il.ac.technion.cs.eldery.system;

import java.util.*;

import il.ac.technion.cs.eldery.utils.*;

/** @author Inbal Zukeran
 * @since 9.12.2016 */
public class sensorInformationDatabase<L, R> {
  private final String sensorId;
  private final LinkedList<Tuple<L, R>> information;
  private int maxCapacity;

  /** @param maxCapacity - the maximal capacity required for this database. If
   *        maxCapacity is 0, the database will be initialized with
   *        maxCapacity=1. This method creates the new database for information
   *        from the sensor */
  public sensorInformationDatabase(final String sensorId, final int maxCapacity) {
    this.sensorId = sensorId;
    this.maxCapacity = maxCapacity == 0 ? 1 : maxCapacity;
    this.information = new LinkedList<>();
  }

  /** @param info- a tuple representing information received from a sensor This
   *        method inserts new information to the database.If needed, after
   *        removing the oldest information received */
  public void addInfo(final Tuple<L, R> info) {
    if (this.information.size() == this.maxCapacity)
      this.information.removeFirst();
    this.information.addLast(info);
  }

  public int getMaxCapacity() {
    return this.maxCapacity;
  }

  public int getCurrentCapacity() {
    return this.information.size();
  }

  /** @param newCapacity This method updates the capacity of the sensor's
   *        database */
  public void changeMaxCapacity(final int newCapacity) {
    if (this.information.size() >= newCapacity) {
      final int cutOff = this.information.size() - newCapacity;
      for (int ¢ = 0; ¢ < cutOff; ++¢)
        this.information.removeFirst();
    }
    this.maxCapacity = newCapacity;
  }

  public LinkedList<Tuple<L, R>> recievceLastUpdates(final int numOfUpdates) {
    if (numOfUpdates <= 0)
      return null;
    final LinkedList<Tuple<L, R>> $ = new LinkedList<>();
    final int position = numOfUpdates > this.information.size() ? 0 : this.information.size() - numOfUpdates;
    for (int ¢ = position; ¢ < this.information.size(); ++¢)
      $.addLast(this.information.get(¢));
    return $;
  }

  public Tuple<L, R> getLastUpdate() {
    return this.information.isEmpty() ? null : this.information.getLast();
  }

  public boolean doesExists(final Tuple<L, R> info) {
    return this.information.indexOf(info) != -1;
  }

  public String getSensorId() {
    return sensorId;
  }
}
