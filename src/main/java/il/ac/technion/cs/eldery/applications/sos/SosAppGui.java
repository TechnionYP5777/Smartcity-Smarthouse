package il.ac.technion.cs.eldery.applications.sos;

import java.util.List;

import il.ac.technion.cs.eldery.system.EmergencyLevel;
import il.ac.technion.cs.eldery.system.applications.api.SensorData;
import il.ac.technion.cs.eldery.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.eldery.system.applications.api.exceptions.OnLoadException;
import il.ac.technion.cs.eldery.system.applications.api.exceptions.OnLoadException.ErrorCode;
import il.ac.technion.cs.eldery.system.exceptions.SensorNotFoundException;
import javafx.scene.control.Button;

public class SosAppGui extends SmartHouseApplication {

    SosController sosController;
    private Button killerButon;
    public boolean shouldAlert = true;

    @Override public void onLoad() throws OnLoadException {
        final List<String> ids = super.inquireAbout("iSOS");
        if (ids.isEmpty())
            throw new OnLoadException(ErrorCode.SENSOR_COM_NAME_NOT_FOUND);

        final String sensorId = ids.get(0);
        System.out.println("msg from app: onLoad");
        try {
            subscribeToSensor(sensorId, SosSensor.class, sosSensor -> {
                final String t = "SOS " + (sosSensor.isPressed() ? "" : "Not ") + "Pressed";
                if (sosController != null && shouldAlert) {
                    sosController.sosBtnPressed();
                    sendAlert("ATTENTION SOS: Client is requesting help.", EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
                    shouldAlert = false;
                }
                System.out.println("msg from app: " + t);
            });
        } catch (final SensorNotFoundException ¢) {
            throw new OnLoadException(ErrorCode.SENSOR_ID_NOT_FOUND, ¢.getMessage());
        }
        
        
        sosController = super.setContentView(getClass().getResource("sos_app_ui.fxml"));
        
        killerButon = sosController.getBtn();
        killerButon.setOnAction(__ -> {
            if (shouldAlert)
                sosController.sosBtnPressed();
            else {
                sosController.sosBtnUnpress();
                shouldAlert = !shouldAlert;
            }
        });
        
    }

    @Override public String getApplicationName() {
        return "SOS Application";
    }

}

class SosSensor extends SensorData {
    private boolean pressed;

    boolean isPressed() {
        return pressed;
    }
}
