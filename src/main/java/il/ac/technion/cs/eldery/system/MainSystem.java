package il.ac.technion.cs.eldery.system;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
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
      /** Allows registration to a sensor. on update, the data will be given to the consumer for farther processing
       * @param sensorCommercialName The name of sensor, agreed upon in an external platform
       * @param notifyWhen A predicate that will be called every time the sensor updates the date. If it returns true the consumer will be called
       * @param notifee A consumer that will receive the new data from the sensor
       * @return True if the registration was successful, false otherwise
       * */
      public static <L,R> Boolean registerToSensor(String sensorCommercialName, Predicate<Tuple<L,R>> notifyWhen, 
                              Consumer<Tuple<L,R>> notifee){
          return Boolean.FALSE; //TODO: ELIA implement
      }
      
      /** Allows registration to a sensor. on time, the sensor will be polled and the data will be given to the consumer for farther processing 
       * @param sensorCommercialName The name of sensor, agreed upon in an external platform
       * @param t the time when a polling is requested
       * @param notifee A consumer that will receive the new data from the sensor
       * @return True if the registration was successful, false otherwise
       * */
      public static <L,R> Boolean registerToSensor(String sensorCommercialName, LocalTime t, Consumer<Tuple<L,R>> notifee){
          return Boolean.FALSE; //TODO: ELIA implement
      }
      
      /** request for the latest data received by a sensor
       *  @param sensorCommercialName The name of sensor, agreed upon in an external platform
       *  @return the latest data (or Optional.empty() if the query failed in any point)
       * */
      public static <L,R> Optional<Tuple<L,R>> querySensor(String sensorCommercialName){
          return Optional.empty(); //TODO: ELIA implement
      }
  }
  
  ApplicationHandler applicationHandler = new ApplicationHandler();
  public MainSystem() {}
}
