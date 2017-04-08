package il.ac.technion.cs.smarthouse.applications.vitals;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.EmergencyLevel;
import il.ac.technion.cs.smarthouse.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
import il.ac.technion.cs.smarthouse.system.services.alerts_service.AlertsManager;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorData;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorsManager;

/** This class contains the logic of the vitals signs application.
 * @author Yarden
 * @since 19.1.17 */
public class VitalsApp extends SmartHouseApplication {
    private static Logger log = LoggerFactory.getLogger(VitalsApp.class);

    private Controller controller;
    boolean lowPulseAlert;
    boolean highPulseAlert;
    boolean lowBPAlert;
    int highBPAlert;

    @Override public void onLoad() throws Exception {
        log.debug("App starting - in onLoad");

        SensorsManager sensorsManager = (SensorsManager) super.getService(ServiceType.SENSORS_SERVICE);
        AlertsManager alertsManager = (AlertsManager) super.getService(ServiceType.ALERTS_SERVICE);

        sensorsManager.getDefaultSensor(VitalsSensor.class, "iVitals").subscribe(vitals -> {
            final int pulse = vitals.getPulse(), systolicBP = vitals.getSystolicBP(), diastolicBP = vitals.getDiastolicBP();
            final String t = "Client has pulse of " + pulse + " and blood pressure of " + systolicBP + "/" + diastolicBP + " mmHg";
            controller.updateChart(vitals.getPulse(), vitals.getSystolicBP(), vitals.getDiastolicBP());

            log.debug("App msg (from function subscibed to vitals sensor): " + t + " | Sensor is located at: " + vitals.getSensorLocation());

            // major alerts
            if (pulse < 45 && !lowPulseAlert) {
                lowPulseAlert = true;
                alertsManager.sendAlert("ATTENTION: Client has an extremely low pulse.", EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
            }
            if (pulse >= 45 && lowPulseAlert)
                lowPulseAlert = false;

            if (pulse > 115 && !highPulseAlert) {
                highPulseAlert = true;
                alertsManager.sendAlert("ATTENTION: Client has an extremely high pulse.", EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
            }
            if (pulse <= 115 && highPulseAlert)
                highPulseAlert = false;

            if ((systolicBP < 80 || diastolicBP < 50) && !lowBPAlert) {
                lowBPAlert = true;
                alertsManager.sendAlert("ATTENTION: Client suffers from hypotension.", EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
            }
            if (systolicBP >= 80 && diastolicBP >= 50 && lowBPAlert)
                lowBPAlert = false;

            if ((systolicBP > 190 || diastolicBP > 120) && highBPAlert < 2) {
                highBPAlert = 2;
                alertsManager.sendAlert("ATTENTION: Client suffers from hypertensive emergency.", EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
            } else if ((systolicBP > 160 && systolicBP <= 190 || diastolicBP > 100 && diastolicBP <= 120) && highBPAlert < 1) {
                highBPAlert = 1;
                alertsManager.sendAlert("ATTENTION: Client suffers from hypertension.", EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
            }

            if (systolicBP <= 160 && diastolicBP <= 100)
                highBPAlert = 0;
        });

        controller = super.setContentView(getClass().getResource("vitals_app_ui.fxml"));
    }

    @Override public String getApplicationName() {
        return "Vitals Application";
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
