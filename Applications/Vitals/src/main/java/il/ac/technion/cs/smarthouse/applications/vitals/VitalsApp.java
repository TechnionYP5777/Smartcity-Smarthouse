package il.ac.technion.cs.smarthouse.applications.vitals;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.developers_api.SmarthouseApplication;
import il.ac.technion.cs.smarthouse.developers_api.application_builder.GuiBinderObject;
import il.ac.technion.cs.smarthouse.sensors.Simulatable;
import il.ac.technion.cs.smarthouse.sensors.simulator.GenericSensor;
import il.ac.technion.cs.smarthouse.sensors.simulator.SensorBuilder;
import il.ac.technion.cs.smarthouse.sensors.simulator.SensorsSimulator;
import il.ac.technion.cs.smarthouse.system.file_system.PathBuilder;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
import il.ac.technion.cs.smarthouse.system.services.alerts_service.AlertsManager;
import il.ac.technion.cs.smarthouse.system.services.alerts_service.EmergencyLevel;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorData;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorsService;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SystemPath;

/**
 * This class contains the logic of the vitals signs application.
 * 
 * @author Yarden
 * @since 19.1.17
 */
public class VitalsApp extends SmarthouseApplication implements Simulatable {
    private static Logger log = LoggerFactory.getLogger(VitalsApp.class);

    private Controller controller;
    boolean lowPulseAlert;
    boolean highPulseAlert;
    boolean lowBPAlert;
    int highBPAlert;

    static SensorsSimulator simulator = initSimulator();

    public static void main(String[] args) throws Exception {
        launch(simulator, true);
    }

    private static SensorsSimulator initSimulator() {
        final String pulsePath = "vitals" + PathBuilder.DELIMITER + "pulse",
                        sysBPPath = "vitals" + PathBuilder.DELIMITER + "systolicBP",
                        diBPPath = "vitals" + PathBuilder.DELIMITER + "diastolicBP";
        SensorsSimulator s = new SensorsSimulator();
        s.addSensor(new SensorBuilder().setCommname("iVitals").setAlias("Yarden's vitals sensor")
                        .addInfoSendingPath(pulsePath, Integer.class)
                        .addStreamingRange(pulsePath, Arrays.asList(80, 100))
                        .addInfoSendingPath(sysBPPath, Integer.class)
                        .addStreamingRange(sysBPPath, Arrays.asList(100, 130))
                        .addInfoSendingPath(diBPPath, Integer.class).addStreamingRange(diBPPath, Arrays.asList(30, 40))
                        .setStreamInterval(TimeUnit.SECONDS.toMillis(1)).build());
        return s;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * il.ac.technion.cs.smarthouse.sensors.Simulatable#getSimulatedSensors()
     */
    @Override
    public Collection<GenericSensor> getSimulatedSensors() {
        return simulator.getAllSensors();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * il.ac.technion.cs.smarthouse.developers_api.SmarthouseApplication#onLoad(
     * )
     */
    @Override
    public void onLoad() throws Exception {
        log.debug("App starting - in onLoad");

        SensorsService sensorsManager = (SensorsService) super.getService(ServiceType.SENSORS_SERVICE);
        AlertsManager alertsManager = (AlertsManager) super.getService(ServiceType.ALERTS_SERVICE);

        sensorsManager.getSensor("iVitals", VitalsSensor.class).subscribe(vitals -> {
            final int pulse = vitals.getPulse(), systolicBP = vitals.getSystolicBP(),
                            diastolicBP = vitals.getDiastolicBP();
            final String t = "Client has pulse of " + pulse + " and blood pressure of " + systolicBP + "/" + diastolicBP
                            + " mmHg";
            controller.updateChart(vitals.getPulse(), vitals.getSystolicBP(), vitals.getDiastolicBP());

            log.debug("App msg (from function subscibed to vitals sensor): " + t + " | Sensor is located at: "
                            + vitals.getSensorLocation());

            // Major alerts
            if (pulse < 45 && !lowPulseAlert) {
                lowPulseAlert = true;
                alertsManager.sendAlert(getApplicationName(), "Client has an extremely low pulse.",
                                EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
            }
            if (pulse >= 45 && lowPulseAlert)
                lowPulseAlert = false;

            if (pulse > 115 && !highPulseAlert) {
                highPulseAlert = true;
                alertsManager.sendAlert(getApplicationName(), "Client has an extremely high pulse.",
                                EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
            }
            if (pulse <= 115 && highPulseAlert)
                highPulseAlert = false;

            if ((systolicBP < 80 || diastolicBP < 50) && !lowBPAlert) {
                lowBPAlert = true;
                alertsManager.sendAlert(getApplicationName(), "Client suffers from hypotension.",
                                EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
            }
            if (systolicBP >= 80 && diastolicBP >= 50 && lowBPAlert)
                lowBPAlert = false;

            if ((systolicBP > 190 || diastolicBP > 120) && highBPAlert < 2) {
                highBPAlert = 2;
                alertsManager.sendAlert(getApplicationName(), "Client suffers from hypertensive emergency.",
                                EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
            } else if ((systolicBP > 160 && systolicBP <= 190 || diastolicBP > 100 && diastolicBP <= 120)
                            && highBPAlert < 1) {
                highBPAlert = 1;
                alertsManager.sendAlert(getApplicationName(), "Client suffers from hypertension.",
                                EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
            }

            if (systolicBP <= 160 && diastolicBP <= 100)
                highBPAlert = 0;
        });

        GuiBinderObject<Controller> c = new GuiBinderObject<>();
        getAppBuilder().getCustomRegionBuilder().add("vitals_app_ui.fxml", c);
        controller = c.getData();
    }

    /*
     * (non-Javadoc)
     * 
     * @see il.ac.technion.cs.smarthouse.developers_api.SmarthouseApplication#
     * getApplicationName()
     */
    @Override
    public String getApplicationName() {
        return "Vitals Application";
    }

    public static class VitalsSensor extends SensorData {
        @SystemPath("vitals.pulse")
        public int pulse;
        @SystemPath("vitals.systolicBP")
        public int systolicBP;
        @SystemPath("vitals.diastolicBP")
        public int diastolicBP;

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
}
