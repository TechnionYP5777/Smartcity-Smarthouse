package il.ac.technion.cs.smarthouse.applications.sos;

import il.ac.technion.cs.smarthouse.system.EmergencyLevel;
import il.ac.technion.cs.smarthouse.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
import il.ac.technion.cs.smarthouse.system.services.alerts_service.AlertsManager;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorApi;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorData;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorsManager;
import javafx.application.Platform;
import javafx.scene.control.Button;

public class SosAppGui extends SmartHouseApplication {

    SosController sosController;
    private Button killerButon;
    public boolean shouldAlert = true;

    @Override public void onLoad() throws Exception {
        SensorsManager sensorsManager = (SensorsManager) super.getService(ServiceType.SENSORS_SERVICE);
        AlertsManager alertsManager = (AlertsManager) super.getService(ServiceType.ALERTS_SERVICE);

        SensorApi<SosSensor> sosSensor = sensorsManager.getDefaultSensor(SosSensor.class, "iSOS");

        System.out.println("msg from app: onLoad " + Platform.isFxApplicationThread());

        sosSensor.subscribeToSensor(sos -> {
            final String t = "SOS " + (sos.isPressed() ? "" : "Not ") + "Pressed";
            System.out.println("msg from app: onLoad " + Platform.isFxApplicationThread());
            if (sosController != null && shouldAlert) {
                sosController.sosBtnPressed();
                alertsManager.sendAlert("ATTENTION SOS: Client is requesting help.", EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
                shouldAlert = false;
            }
            System.out.println("msg from app: " + t + " Location: " + sos.getSensorLocation());
        });

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
