package il.ac.technion.cs.smarthouse.applications.stove;

import il.ac.technion.cs.smarthouse.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorApi;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorData;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorsManager;

public class StoveModuleGui extends SmartHouseApplication {
    private StoveAppController controller;

    @Override public void onLoad() throws Exception {
        SensorsManager sensorsManager = (SensorsManager) super.getService(ServiceType.SENSORS_SERVICE);

        SensorApi<StoveSensor> stoveSensor = sensorsManager.getDefaultSensor(StoveSensor.class, "iStoves");

        System.out.println("msg from app: onLoad");

        stoveSensor.subscribeToSensor(stove -> {
            final String t = "Stove is " + (stove.isOn() ? "" : "Not ") + "On at " + stove.getTemperture() + " degrees";
            if (stove.isOn())
                controller.turnOn();
            else
                controller.turnOf();
            controller.updateTemperture(stove.getTemperture());
            System.out.println("msg from app: " + t);
        });

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
