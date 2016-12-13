package il.ac.technion.cs.eldery.system;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import il.ac.technion.cs.eldery.system.applications.ApplicationIdentifier;
import il.ac.technion.cs.eldery.system.exceptions.ApplicationNotRegisteredToEvent;
import il.ac.technion.cs.eldery.system.sensors.SensorHandler;
import il.ac.technion.cs.eldery.system.sensors.SensorInfo;
import il.ac.technion.cs.eldery.utils.Tuple;

/** Hold the databases of the smart house, and allow sensors and applications to
 * store and read information about the changes in the environment */
public class MainSystem {
    SensorHandler sensorHandler = new SensorHandler();

    Map<ApplicationIdentifier, AppThread> apps = new HashMap<>();

    /** API allowing smart house applications to register for information and
     * notify on emergencies */
    class ApplicationHandler {
        /** Allows registration to a sensor. on update, the data will be given
         * to the consumer for farther processing
         * @param id The identification of the application requesting to
         *        register
         * @param sensorCommercialName The name of sensor, agreed upon in an
         *        external platform
         * @param notifyWhen A predicate that will be called every time the
         *        sensor updates the date. If it returns true the consumer will
         *        be called
         * @param notifee A consumer that will receive the new data from the
         *        sensor
         * @return True if the registration was successful, false otherwise */
        public <L, R> Boolean registerToSensor(ApplicationIdentifier id, String sensorCommercialName, Predicate<Tuple<L, R>> notifyWhen,
                Consumer<Tuple<L, R>> notifee) {
            try {
                Long eventId = apps.get(id).registerEventConsumer(notifee);
                @SuppressWarnings("unchecked") SensorInfo<L, R> sensor = sensorHandler.getSensors().get(sensorCommercialName);
                Consumer<Tuple<L, R>> $ = new Consumer<Tuple<L, R>>() {
                    /* (non-Javadoc)
                     * 
                     * @see
                     * java.util.function.Consumer#accept(java.lang.Object) */
                    @Override public void accept(Tuple<L, R> t) {
                        if (notifyWhen.test(t))
                            try {
                                apps.get(id).notifyOnEvent(eventId, t);
                            } catch (ApplicationNotRegisteredToEvent e) {
                                e.printStackTrace();
                            }
                    }
                };
                sensor.addListener($);
                return Boolean.TRUE;
            } catch (Exception __) {
                return Boolean.FALSE;
            }
        }

        /** Allows registration to a sensor. on time, the sensor will be polled
         * and the data will be given to the consumer for farther processing
         * @param id The identification of the application requesting to
         *        register
         * @param sensorCommercialName The name of sensor, agreed upon in an
         *        external platform
         * @param t the time when a polling is requested
         * @param notifee A consumer that will receive the new data from the
         *        sensor
         * @return True if the registration was successful, false otherwise */
        public <L, R> Boolean registerToSensor(ApplicationIdentifier id, String sensorCommercialName, LocalTime t, Consumer<Tuple<L, R>> notifee) {
            return Boolean.FALSE; // TODO: ELIA implement
        }

        /** Request for the latest data received by a sensor
         * @param sensorCommercialName The name of sensor, agreed upon in an
         *        external platform
         * @return the latest data (or Optional.empty() if the query failed in
         *         any point) */
        public <L, R> Optional<Tuple<L, R>> querySensor(String sensorCommercialName) {
            return Optional.ofNullable(sensorHandler.getSensors().get(sensorCommercialName)).map(SensorInfo::getLastUpdate);
        }

        /** Report an abnormality in the expected schedule. The system will
         * contact the needed personal, according to the urgency level
         * @param message Specify the abnormality, will be presented to the
         *        contacted personal
         * @param eLevel The level of personnel needed in the situation */
        public void alertOnAbnormalState(String message, EmergencyLevel eLevel) {
            // TODO: ELIA implement
        }
    }

    ApplicationHandler applicationHandler = new ApplicationHandler();

    public MainSystem() {}
}
