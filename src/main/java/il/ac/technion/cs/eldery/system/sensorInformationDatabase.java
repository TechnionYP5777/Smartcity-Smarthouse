package il.ac.technion.cs.eldery.system;
import java.util.LinkedList;

import il.ac.technion.cs.eldery.utils.Tuple;

/**
 * @author Inbal Zukeran
 * @since 9.12.2016
 *
 */
public class sensorInformationDatabase<L,R> {
  
  private final LinkedList<Tuple<L,R>> information;
  private int maxCapacity;

  public sensorInformationDatabase(final int maxCapacity) {
    this.maxCapacity = maxCapacity;
    this.information = new LinkedList<> ();
  }
  
  /**
   * @param info- a tuple representing information received from a sensor
   * This method inserts new information to the database.If needed, after removing the oldest information received
   */
  public void addInfo(final Tuple<L,R> info){
    if (this.information.size() == this.maxCapacity)
      this.information.removeFirst();
    this.information.addLast(info);
  }
  
  

  /**
   * @param newCapacity
   * This method updates the capacity of the sensor's database
   */
  public void changeMaxCapacity(final int newCapacity){
    if (this.information.size() >= newCapacity) {
      final int cutOff = this.information.size() - newCapacity;
      for (int ¢ = 0; ¢ < cutOff; ++¢)
        this.information.removeFirst();
    }
    this.maxCapacity = newCapacity;
  }
  
  
  public LinkedList<Tuple<L,R>> recievceLastUpdates(final int numOfUpdates){
    
    if(numOfUpdates<=0 )
      return null;
    
    final LinkedList<Tuple<L, R>> $ = new LinkedList<>();
    final int position = numOfUpdates > this.information.size() ? 0 : this.information.size() - numOfUpdates;
    $.addAll(position, this.information);
    return $;
    
  }
  
  public Tuple<L,R> getLastUpdate(){
    
    return this.information.getLast();
  }
  
 
}
