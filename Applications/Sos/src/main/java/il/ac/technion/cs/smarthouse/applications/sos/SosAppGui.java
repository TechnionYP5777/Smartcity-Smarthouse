package il.ac.technion.cs.smarthouse.applications.sos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.sensors.sos.gui.SosSensorSimulator;
import il.ac.technion.cs.smarthouse.system.EmergencyLevel;
import il.ac.technion.cs.smarthouse.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
import il.ac.technion.cs.smarthouse.system.services.alerts_service.AlertsManager;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorData;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorsService;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SystemPath;
import javafx.application.Platform;
import javafx.scene.control.Button;

public class SosAppGui extends SmartHouseApplication {
    private static Logger log = LoggerFactory.getLogger(SosAppGui.class);

    SosController sosController;
    private Button killerButon;
    public boolean shouldAlert = true;
    
    public static void main(String[] args) throws Exception {
        launch(SosSensorSimulator.class);
    }

    @Override public void onLoad() throws Exception {
        log.debug("App starting - in onLoad");

        ((SensorsService) super.getService(ServiceType.SENSORS_SERVICE)).getSensor("iSOS", SosSensor.class).subscribe(sos -> {
            final String t = "SOS " + (sos.isPressed() ? "" : "Not ") + "Pressed";
            System.out.println("msg from app: onLoad " + Platform.isFxApplicationThread());
            if (sosController != null && shouldAlert) {
                sosController.sosBtnPressed();
                ((AlertsManager) super.getService(ServiceType.ALERTS_SERVICE)).sendAlert("ATTENTION SOS: Client is requesting help.",
                        EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
                shouldAlert = false;
            }
            log.debug("App msg (from function subscibed to sos sensor): " + t + " | Sensor is located at: " + sos.getSensorLocation());
        });
        
        sosController = super.setContentView("sos_app_ui.fxml");

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
    
    public static class SosSensor extends SensorData {
        @SystemPath("sos.pressed")
        private boolean pressed;

        boolean isPressed() {
            return pressed;
        }
    }

}

