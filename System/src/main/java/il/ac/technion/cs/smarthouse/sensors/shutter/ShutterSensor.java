package il.ac.technion.cs.smarthouse.sensors.shutter;

import il.ac.technion.cs.smarthouse.sensors.Sensor;

/** This class represents shutters sensor and contains its logic.
 * @author Alex
 * @author Inbal Zukerman
 * @since 8.5.17 */
public class ShutterSensor extends Sensor {

    public ShutterSensor(String id, String commName, String systemIP, int systemPort) {
        super(id, commName, systemIP, systemPort);
    }
    
    
    public void updateSystem(final boolean open, final int fromTime , final int toTime) {
        super.updateSystem("open@" + open + "@time" + fromTime);
    }
    

   /* @Override public String[] getObservationsNames() {
        return new String[] { "open", "fromTime", "toTime" };
       
    }*/

}
