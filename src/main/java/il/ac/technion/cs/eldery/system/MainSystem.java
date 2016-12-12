package il.ac.technion.cs.eldery.system;

import java.time.LocalTime;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import il.ac.technion.cs.eldery.utils.Tuple;

/** hold the databases of the smart house, and allow sensors and applications to store and read information about the changes
 *  in the environment
 * */
public class MainSystem {
  /** API allowing smart house applications to register for information and notify on emergencies
   * */
  static class ApplicationHandler {
      public static <L,R> Boolean registerToSensor(String sensorCommercialName, Predicate<Tuple<L,R>> notifyWhen, 
                              Consumer<List<Tuple<L,R>>> notifee){
          return Boolean.FALSE; //TODO: ELIA implement
      }
      
      public static <L,R> Boolean registerToSensor(String sensorCommercialName, LocalTime time, 
                              Consumer<List<Tuple<L,R>>> notifee){
      return Boolean.FALSE; //TODO: ELIA implement
      }
  }
  
  ApplicationHandler applicationHandler = new ApplicationHandler();
  public MainSystem() {}
}
