package il.ac.technion.cs.eldery.applications.stove;

import java.io.IOException;
import java.util.List;

import il.ac.technion.cs.eldery.system.applications.api.SensorData;
import il.ac.technion.cs.eldery.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.eldery.system.applications.api.exceptions.OnLoadException;
import il.ac.technion.cs.eldery.system.applications.api.exceptions.OnLoadException.ErrorCode;
import il.ac.technion.cs.eldery.system.exceptions.SensorNotFoundException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

public class StoveModuleGui extends SmartHouseApplication {
    private StoveAppController controller;
    
    @Override public void onLoad() throws OnLoadException {
        final List<String> ids = super.inquireAbout("iStoves");
        if (ids.isEmpty())
            throw new OnLoadException(ErrorCode.SENSOR_COM_NAME_NOT_FOUND);

        final String sensorId = ids.get(0);
        System.out.println("msg from app: onLoad");
        try {
            subscribeToSensor(sensorId, StoveSensor.class, stoveSensor -> {
                final String t = "Stove is " + (stoveSensor.isOn() ? "" : "Not ") + "On at " + stoveSensor.getTemperture() +" degrees";
                if(stoveSensor.isOn())  
                    controller.turnOn();
                else
                    controller.turnOf();
                controller.updateTemperture(stoveSensor.getTemperture());
                System.out.println("msg from app: " + t);
            });
        } catch (final SensorNotFoundException ¢) {
            throw new OnLoadException(ErrorCode.SENSOR_ID_NOT_FOUND, ¢.getMessage());
        }
    }

    @Override public String getApplicationName() {
        return "Stove Application";
    }
    
    @Override public Node getRootNode() {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("stove_app_ui.fxml"));
            final Parent $ = fxmlLoader.load();
            controller = fxmlLoader.getController();
            return $;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class StoveSensor extends SensorData {
    private boolean on;
    private int temperature;
    
    boolean isOn() {
        return on;
    }
    
    int getTemperture(){
        return temperature;
    }
}
