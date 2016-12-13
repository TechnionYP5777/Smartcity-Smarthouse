package il.ac.technion.cs.eldery.system.sensors;

import java.util.HashMap;
import java.util.Map;

public class SensorHandler {
    @SuppressWarnings("rawtypes") private Map<String, SensorInfo> sensors = new HashMap<>();

    @SuppressWarnings("rawtypes") public Map<String, SensorInfo> getSensors() {
        return sensors;
    }
}
