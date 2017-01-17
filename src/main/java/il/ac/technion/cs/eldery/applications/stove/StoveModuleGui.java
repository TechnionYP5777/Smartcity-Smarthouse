package il.ac.technion.cs.eldery.applications.stove;

import java.util.List;

import il.ac.technion.cs.eldery.system.applications.api.SensorData;
import il.ac.technion.cs.eldery.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.eldery.system.applications.api.exceptions.OnLoadException;
import il.ac.technion.cs.eldery.system.applications.api.exceptions.OnLoadException.ErrorCode;
import il.ac.technion.cs.eldery.system.exceptions.SensorNotFoundException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StoveModuleGui extends SmartHouseApplication {
    private StoveAppController controller;
    public static void main(final String[] args) {
        launch(args);
    }

    @Override public void start(final Stage s) throws Exception {
        super.start(s);
        
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("stove_app_ui.fxml"));
        final Parent root = fxmlLoader.load();
        final Scene scene = new Scene(root);
        s.setTitle("Stove Application");
        s.setScene(scene);
        s.setWidth(350);
        s.show();
        
        controller = fxmlLoader.getController();
    }
    
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
