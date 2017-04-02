package il.ac.technion.cs.eldery.applications.stove;

import java.util.List;

import il.ac.technion.cs.eldery.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.eldery.system.applications.api.exceptions.OnLoadException;
import il.ac.technion.cs.eldery.system.applications.api.exceptions.OnLoadException.ErrorCode;
import il.ac.technion.cs.eldery.system.exceptions.SensorNotFoundException;
import il.ac.technion.cs.eldery.system.services.ServiceType;
import il.ac.technion.cs.eldery.system.services.sensors_service.SensorData;
import il.ac.technion.cs.eldery.system.services.sensors_service.SensorsManager;

public class StoveModuleGui extends SmartHouseApplication {
    private StoveAppController controller;

    @Override public void onLoad() throws OnLoadException {
        SensorsManager sensorsManager = (SensorsManager) super.getService(ServiceType.SENSORS_SERVICE);
        
        final List<String> ids = sensorsManager.inquireAbout("iStoves");
        if (ids.isEmpty())
            throw new OnLoadException(ErrorCode.SENSOR_COM_NAME_NOT_FOUND);

        final String sensorId = ids.get(0);
        System.out.println("msg from app: onLoad");
        try {
            sensorsManager.subscribeToSensor(sensorId, StoveSensor.class, stoveSensor -> {
                final String t = "Stove is " + (stoveSensor.isOn() ? "" : "Not ") + "On at " + stoveSensor.getTemperture() + " degrees";
                if (stoveSensor.isOn())
                    controller.turnOn();
                else
                    controller.turnOf();
                controller.updateTemperture(stoveSensor.getTemperture());
                System.out.println("msg from app: " + t);
            });
        } catch (final SensorNotFoundException ¢) {
            throw new OnLoadException(ErrorCode.SENSOR_ID_NOT_FOUND, ¢.getMessage());
        }
        
        
        controller = super.setContentView(getClass().getResource("stove_app_ui.fxml"));
        controller.setInstance(this);
    }

    @Override public String getApplicationName() {
        return "Stove Application";
    }
}

class StoveSensor extends SensorData {
    private boolean on;
    private int temperature;

    boolean isOn() {
        return on;
    }

    int getTemperture() {
        return temperature;
    }
}
