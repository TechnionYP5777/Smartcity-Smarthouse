package il.ac.technion.cs.eldery.system.applications.API;

import java.util.function.Consumer;

import il.ac.technion.cs.eldery.system.applications.api.SensorData;
import il.ac.technion.cs.eldery.system.applications.api.SmartHouseApplication;
import javafx.application.Platform;
import javafx.scene.Node;

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

    @Override public Node getRootNode() {
        return null;
    }
    
    private static <T extends SensorData> Consumer<T> generateConsumer(final Consumer<T> functionToRun) {
        return sensorData -> functionToRun.accept(sensorData);
    }

}
