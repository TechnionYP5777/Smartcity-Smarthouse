package il.ac.technion.cs.smarthouse.applications.stove;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.developers_api.SmarthouseApplication;
import il.ac.technion.cs.smarthouse.sensors.stove.gui.StoveSensorSimulator;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorData;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorsService;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SystemPath;

public class StoveModuleGui extends SmarthouseApplication {
    private static Logger log = LoggerFactory.getLogger(StoveModuleGui.class);

    private StoveAppController controller;
    
    public static void main(String[] args) throws Exception {
        launch(StoveSensorSimulator.class);
    }

    @Override public void onLoad() throws Exception {
        log.debug("App starting - in onLoad");

        super.<SensorsService>getService(ServiceType.SENSORS_SERVICE).getSensor("iStoves", StoveSensor.class).subscribe(stove -> {
            final String t = "Stove is " + (stove.isOn() ? "" : "Not ") + "On at " + stove.getTemperture() + " degrees";
            if (stove.isOn())
                controller.turnOn();
            else
                controller.turnOf();
            controller.updateTemperture(stove.getTemperture());
            log.debug("App msg (from function subscibed to stove sensor): " + t + " | Sensor is located at: " + stove.getSensorLocation());
        });

        controller = super.setContentView("stove_app_ui.fxml");
        controller.setInstance(this);
        
        saveApplicationData(5, "myNum");
    }

    @Override public String getApplicationName() {
        return "Stove Application";
    }
    
    public static class StoveSensor extends SensorData {
        @SystemPath("stove.is_on")
        private boolean on;
        
        @SystemPath("stove.temperature")
        private int temperature;

        boolean isOn() {
            return on;
        }

        int getTemperture() {
            return temperature;
        }
    }
}

