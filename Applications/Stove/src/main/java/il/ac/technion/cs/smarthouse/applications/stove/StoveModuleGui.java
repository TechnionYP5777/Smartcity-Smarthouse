package il.ac.technion.cs.smarthouse.applications.stove;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.developers_api.SmarthouseApplication;
import il.ac.technion.cs.smarthouse.developers_api.application_builder.ColorRange;
import il.ac.technion.cs.smarthouse.developers_api.application_builder.DataObject;
import il.ac.technion.cs.smarthouse.sensors.stove.gui.StoveSensorSimulator;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorData;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorsService;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SystemPath;
import javafx.scene.paint.Color;

public class StoveModuleGui extends SmarthouseApplication {
    private static Logger log = LoggerFactory.getLogger(StoveModuleGui.class);
    
    public static void main(String[] args) throws Exception {
        launch(StoveSensorSimulator.class);
    }

    @Override public void onLoad() throws Exception {
        final DataObject<Double> alertAfterSecs = new DataObject<>(30.0);
        final DataObject<Integer> alertAboveDegs = new DataObject<>(120);
        final DataObject<Integer> temps = new DataObject<>(0);
        final DataObject<Boolean> isStoveOn = new DataObject<>(false);
        final DataObject<Double> timer = new DataObject<>();
        
        getAppBuilder().getConfigurationsRegionBuilder()
            .addDoubleInputField("Alert after (seconds):", alertAfterSecs)
            .addIntegerInputField("Alert at (degrees celsius):", alertAboveDegs);
        
        getAppBuilder().getStatusRegionBuilder()
            .addStatusField("Current tempeture:", temps, new ColorRange<Integer>().addRange(alertAboveDegs, Color.RED))
            .addTimerStatusField("Stove running timer:", isStoveOn, timer, new ColorRange<Double>().addRange(alertAfterSecs, Color.RED));

        super.<SensorsService>getService(ServiceType.SENSORS_SERVICE).getSensor("iStoves", StoveSensor.class).subscribe(stove -> {
            final String t = "Stove is " + (stove.isOn() ? "" : "Not ") + "On at " + stove.getTemperture() + " degrees";
            isStoveOn.setData(stove.isOn());
            temps.setData(!stove.isOn() ? 0 : stove.getTemperture());
            log.debug("App msg (from function subscibed to stove sensor): " + t + " | Sensor is located at: " + stove.getSensorLocation());
        });
        
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

