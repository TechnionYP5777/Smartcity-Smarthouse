package il.ac.technion.cs.eldery.applications.sos;

import il.ac.technion.cs.eldery.system.applications.api.SensorData;
import il.ac.technion.cs.eldery.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.eldery.system.exceptions.SensorNotFoundException;
import javafx.application.Platform;
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

    @Override public void onLoad() {
        // TODO: ron - you are not supposed to know the ID :/ fix this
        final String sensorId = "00:00:00:00:00:01";
        System.out.println("msg from app: onLoad");
        try {
            subscribeToSensor(sensorId, SosSensor.class, sosSensor -> {
                final String t = "SOS " + (sosSensor.isPressed() ? "" : "Not ") + "Pressed";
                Platform.runLater(() -> sosController.killElder());
                System.out.println("msg from app: " + t);
            });
        } catch (final SensorNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class SosSensor extends SensorData {
    private boolean pressed;

    boolean isPressed() {
        return pressed;
    }
}
