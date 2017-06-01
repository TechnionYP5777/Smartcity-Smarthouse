package il.ac.technion.cs.smarthouse.networking.messages;

import java.util.Arrays;
import java.util.List;

import il.ac.technion.cs.smarthouse.sensors.Sensor;
import il.ac.technion.cs.smarthouse.sensors.SensorType;

/** @author Yarden
 * @author Sharon
 * @since 11.12.16 */
public class RegisterMessage extends Message {
    public final String sensorId, sensorCommName;
    public final List<String> observationSendingPaths, instructionRecievingPaths;

    /** Creates a new registration message for the given sensor.
     * @param sensor sensor to register */
    public RegisterMessage(final Sensor sensor) {
        super(MessageType.REGISTRATION);

        sensorId = sensor.getId();
        sensorCommName = sensor.getCommname();
        observationSendingPaths = sensor.getObservationSendingPaths();
        instructionRecievingPaths = sensor.getInstructionRecievingPaths();
    }

    @Override public String toString() {
        String values = Arrays.asList(this.getClass()
                                                .getFields())
                                                .stream()
                                                .map(f -> {
                                                            try {
                                                                return f.getName()+": "+ f.get(this);
                                                            } catch (IllegalArgumentException | IllegalAccessException e) {
                                                            }
                                                            return null;
                                                })
                                                .reduce("", (a,b)->a+b);
        return "RegisterMessage " +"["+values+"]";
    }

    public String getSensorId() {
        return sensorId;
    }

    public String getSensorCommName() {
        return sensorCommName;
    }

    public List<String> getObservationSendingPaths() {
        return observationSendingPaths;
    }

    public List<String> getInstructionRecievingPaths() {
        return instructionRecievingPaths;
    }
    
}
