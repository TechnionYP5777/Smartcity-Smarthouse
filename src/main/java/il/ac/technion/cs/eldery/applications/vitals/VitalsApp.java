package il.ac.technion.cs.eldery.applications.vitals;

import java.io.IOException;
import java.util.List;

import il.ac.technion.cs.eldery.system.EmergencyLevel;
import il.ac.technion.cs.eldery.system.applications.api.SensorData;
import il.ac.technion.cs.eldery.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.eldery.system.applications.api.exceptions.OnLoadException;
import il.ac.technion.cs.eldery.system.applications.api.exceptions.OnLoadException.ErrorCode;
import il.ac.technion.cs.eldery.system.exceptions.SensorNotFoundException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

/** @author Yarden
 * @since 19.1.17 */
public class VitalsApp extends SmartHouseApplication {
    private Controller controller;
    boolean lowPulseAlert;
    boolean highPulseAlert;
    boolean lowBPAlert;
    int highBPAlert;

    @Override public void onLoad() throws OnLoadException {
        final List<String> ids = super.inquireAbout("iVitals");
        if (ids.isEmpty())
            throw new OnLoadException(ErrorCode.SENSOR_COM_NAME_NOT_FOUND);
        final String sensorId = ids.get(0);
        System.out.println("msg from app: onLoad");
        try {
            subscribeToSensor(sensorId, VitalsSensor.class, vitalsSensor -> {
                final int pulse = vitalsSensor.getPulse(), systolicBP = vitalsSensor.getSystolicBP(), diastolicBP = vitalsSensor.getDiastolicBP();
                final String t = "Client has pulse of " + pulse + " and blood pressure of " + systolicBP + "/" + diastolicBP + " mmHg";
                controller.updateChart(vitalsSensor.getPulse(), vitalsSensor.getSystolicBP(), vitalsSensor.getDiastolicBP());
                System.out.println("msg from app: " + t);

                // major alerts
                if (pulse < 45 && !lowPulseAlert) {
                    lowPulseAlert = true;
                    sendAlert("ATTENTION: Client has an extremely low pulse.", EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
                }
                if (pulse >= 45 && lowPulseAlert)
                    lowPulseAlert = false;

                if (pulse > 115 && !highPulseAlert) {
                    highPulseAlert = true;
                    sendAlert("ATTENTION: Client has an extremely high pulse.", EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
                }
                if (pulse <= 115 && highPulseAlert)
                    highPulseAlert = false;

                if ((systolicBP < 80 || diastolicBP < 50) && !lowBPAlert) {
                    lowBPAlert = true;
                    sendAlert("ATTENTION: Client suffers from hypotension.", EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
                }
                if (systolicBP >= 80 && diastolicBP >= 50 && lowBPAlert)
                    lowBPAlert = false;

                if ((systolicBP > 190 || diastolicBP > 120) && highBPAlert < 2) {
                    highBPAlert = 2;
                    sendAlert("ATTENTION: Client suffers from hypertensive emergency.", EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
                } else if ((systolicBP > 160 && systolicBP <= 190 || diastolicBP > 100 && diastolicBP <= 120) && highBPAlert < 1) {
                    highBPAlert = 1;
                    sendAlert("ATTENTION: Client suffers from hypertension.", EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
                }

                if (systolicBP <= 160 && diastolicBP <= 100)
                    highBPAlert = 0;
            });
        } catch (final SensorNotFoundException ¢) {
            throw new OnLoadException(ErrorCode.SENSOR_ID_NOT_FOUND, ¢.getMessage());
        }
    }

    @Override public String getApplicationName() {
        return "Vitals Application";
    }

    @Override public Node getRootNode() {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("vitals_app_ui.fxml"));
            final Parent $ = fxmlLoader.load();
            controller = fxmlLoader.getController();
            return $;
        } catch (final IOException ¢) {
            ¢.printStackTrace();
        }
        return null;
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
