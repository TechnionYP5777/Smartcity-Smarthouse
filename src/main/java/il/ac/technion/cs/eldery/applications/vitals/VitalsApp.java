package il.ac.technion.cs.eldery.applications.vitals;

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

public class VitalsApp extends SmartHouseApplication {
    private Controller controller;

    public static void main(final String[] args) {
        launch(args);
    }

    @Override public void start(final Stage s) throws Exception {
        super.start(s);
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("vitals_app_ui.fxml"));
        final Parent root = fxmlLoader.load();
        final Scene scene = new Scene(root);
        s.setTitle("Vitals Application");
        s.setScene(scene);
        s.show();

        controller = fxmlLoader.getController();
    }

    @Override public void onLoad() throws OnLoadException {
        final List<String> ids = super.inquireAbout("iVitals");
        if (ids.isEmpty())
            throw new OnLoadException(ErrorCode.SENSOR_COM_NAME_NOT_FOUND);
        final String sensorId = ids.get(0);
        System.out.println("msg from app: onLoad");
        try {
            subscribeToSensor(sensorId, VitalsSensor.class, vitalsSensor -> {
                final String t = "Patient has pulse of " + vitalsSensor.getPulse() + " and blood pressure of " + vitalsSensor.getSystolicBP() + "/"
                        + vitalsSensor.getDiastolicBP() + " mmHg";
                controller.updateChart(vitalsSensor.getPulse(), vitalsSensor.getSystolicBP(), vitalsSensor.getDiastolicBP());
                System.out.println("msg from app: " + t);
            });
        } catch (final SensorNotFoundException ¢) {
            throw new OnLoadException(ErrorCode.SENSOR_ID_NOT_FOUND, ¢.getMessage());
        }
    }
}

class VitalsSensor extends SensorData {
    private int pulse;
    private int systolicBP;
    private int diastolicBP;

    int getPulse() {
        return pulse;
    }

    int getSystolicBP() {
        return systolicBP;
    }

    int getDiastolicBP() {
        return diastolicBP;
    }
}
