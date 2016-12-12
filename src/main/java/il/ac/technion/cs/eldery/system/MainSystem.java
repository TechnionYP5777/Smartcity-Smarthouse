package il.ac.technion.cs.eldery.system;



/** hold the databases of the smart house, and allow sensors and applications to store and read information about the changes
 *  in the environment
 * */
public class MainSystem {
  /** API allowing smart house applications to register for information and notify on emergencies
   * */
  static class ApplicationHandler {
  }
  
  ApplicationHandler applicationHandler = new ApplicationHandler();
  public MainSystem() {}
}
