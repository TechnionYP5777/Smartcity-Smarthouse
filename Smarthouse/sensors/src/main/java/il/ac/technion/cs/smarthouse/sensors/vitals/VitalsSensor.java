package il.ac.technion.cs.smarthouse.sensors.vitals;

import java.util.HashMap;
import java.util.Map;

import il.ac.technion.cs.smarthouse.sensors.Sensor;

/** This class represents a vitals signs sensor and contains its logic.
 * @author Yarden
 * @since 16.1.17 */
public class VitalsSensor extends Sensor {
    public VitalsSensor(final String id, final String commName, final String systemIP, final int systemPort) {
        super(id, commName, systemIP, systemPort);
    }

    public void updateSystem(final int pulse, final int systolicBP, final int diastolicBP) {
        final Map<String, String> data = new HashMap<>();
        data.put("pulse", pulse + "");
        data.put("systolicBP", systolicBP + "");
        data.put("diastolicBP", diastolicBP + "");
        super.updateSystem(data);
    }

    @Override public String[] getObservationsNames() {
        return new String[] { "pulse", "systolicBP", "diastolicBP" };
    }

}
