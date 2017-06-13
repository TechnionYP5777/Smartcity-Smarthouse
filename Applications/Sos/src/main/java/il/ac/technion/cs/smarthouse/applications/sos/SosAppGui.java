package il.ac.technion.cs.smarthouse.applications.sos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.developers_api.SmarthouseApplication;
import il.ac.technion.cs.smarthouse.developers_api.application_builder.ColorRange;
import il.ac.technion.cs.smarthouse.developers_api.application_builder.GuiBinderObject;
import il.ac.technion.cs.smarthouse.sensors.sos.gui.SosSensorSimulator;
import il.ac.technion.cs.smarthouse.system.EmergencyLevel;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
import il.ac.technion.cs.smarthouse.system.services.alerts_service.AlertsManager;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorApi;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorData;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorsService;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SystemPath;
import javafx.scene.paint.Color;

public class SosAppGui extends SmarthouseApplication {
    private static Logger log = LoggerFactory.getLogger(SosAppGui.class);

    public boolean shouldAlert = true;
    
    public static void main(String[] args) throws Exception {
        launch(SosSensorSimulator.class);
    }

    @Override public void onLoad() throws Exception {
        SensorApi<SosSensor> mySosSensor = ((SensorsService) super.getService(ServiceType.SENSORS_SERVICE)).getSensor("iSOS", SosSensor.class);
        
        final String INIT = "SOS not pressed";
        GuiBinderObject<String> str = new GuiBinderObject<>(INIT);
        
        getAppBuilder().getConfigurationsRegionBuilder()
            .addSensorAliasSelectionField("Selected SOS button", mySosSensor)
            .addButtonInputField("Press if ok", "OK", (new GuiBinderObject<Void>()).addOnDataChangedListener(d->{
            if (!shouldAlert) {
                shouldAlert = !shouldAlert;
                str.setData(INIT);
            }
        }));
        
        getAppBuilder().getStatusRegionBuilder().addStatusField("", str, new ColorRange<String>(Color.RED).addIfEquals(INIT, Color.GREEN));

        mySosSensor.subscribe(sos -> {
            final String t = "SOS " + (sos.isPressed() ? "" : "Not ") + "Pressed";
            if (shouldAlert) {
                str.setData(t);
                ((AlertsManager) super.getService(ServiceType.ALERTS_SERVICE)).sendAlert("ATTENTION SOS: Client is requesting help.",
                        EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
                shouldAlert = false;
            }
            log.debug("App msg (from function subscibed to sos sensor): " + t + " | Sensor is located at: " + sos.getSensorLocation());
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

