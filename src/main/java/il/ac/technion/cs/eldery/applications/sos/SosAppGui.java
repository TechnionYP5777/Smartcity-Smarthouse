package il.ac.technion.cs.eldery.applications.sos;

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

public class SosAppGui extends SmartHouseApplication {
    private SosController sosController;

    public static void main(final String[] args) {
        launch(args);
    }

    @Override public void start(final Stage s) throws Exception {
        super.start(s);
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sos_app_ui.fxml"));
        final Parent root = fxmlLoader.load();
        final Scene scene = new Scene(root);
        s.setTitle("SOS Application");
        s.setScene(scene);
        s.setWidth(350);
        s.show();

        sosController = fxmlLoader.getController();
    }

    @Override public void onLoad() throws OnLoadException {
        List<String> ids = super.inquireAbout("iSOS");
        if (ids.isEmpty())
            throw new OnLoadException(ErrorCode.SENSOR_COM_NAME_NOT_FOUND);

        final String sensorId = ids.get(0);
        System.out.println("msg from app: onLoad");
        try {
            subscribeToSensor(sensorId, SosSensor.class, sosSensor -> {
                final String t = "SOS " + (sosSensor.isPressed() ? "" : "Not ") + "Pressed";
                sosController.killElder();
                System.out.println("msg from app: " + t);
            });
        } catch (SensorNotFoundException e) {
            throw new OnLoadException(ErrorCode.SENSOR_ID_NOT_FOUND, e.getMessage());
        }
    }
}

class SosSensor extends SensorData {
    private boolean pressed;

    boolean isPressed() {
        return pressed;
    }
}
