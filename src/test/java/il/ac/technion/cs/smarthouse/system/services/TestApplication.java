package il.ac.technion.cs.smarthouse.system.services;

import java.util.function.Consumer;

import il.ac.technion.cs.smarthouse.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorData;



public class TestApplication extends SmartHouseApplication {
    private boolean isLoaded;

    public TestApplication() {}

    @Override public void onLoad() {
        isLoaded = true;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    @Override public String getApplicationName() {
        return "TestApplication";
    }
    
    private static <T extends SensorData> Consumer<T> generateConsumer(final Consumer<T> functionToRun) {
        return sensorData -> functionToRun.accept(sensorData);
    }

}
