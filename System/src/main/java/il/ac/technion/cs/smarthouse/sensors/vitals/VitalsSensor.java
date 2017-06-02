package il.ac.technion.cs.smarthouse.sensors.vitals;

import java.util.Arrays;

import il.ac.technion.cs.smarthouse.sensors.Sensor;
import il.ac.technion.cs.smarthouse.system.file_system.PathBuilder;

/**
 * This class represents a vitals signs sensor and contains its logic.
 * 
 * @author Yarden
 * @author Inbal Zukerman
 * @since 16.1.17
 */
public class VitalsSensor extends Sensor {
    static final String pulsePath = "vitals" + PathBuilder.DELIMITER + "pulse";
    static final String sysBPPath = "vitals" + PathBuilder.DELIMITER + "systolicBP";
    static final String diBPPath = "vitals" + PathBuilder.DELIMITER + "diastolicBP";

    public VitalsSensor(final String id, final int systemPort) {
        super("iVitals", id, Arrays.asList(pulsePath, sysBPPath, diBPPath), systemPort);
    }

    public void updateSystem(final int pulse, final int systolicBP, final int diastolicBP) {

        super.updateSystem(pulsePath, pulse);

        super.updateSystem(sysBPPath, systolicBP);

        super.updateSystem(diBPPath, diastolicBP);

    }

}
