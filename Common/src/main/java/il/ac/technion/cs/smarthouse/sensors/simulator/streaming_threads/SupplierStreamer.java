package il.ac.technion.cs.smarthouse.sensors.simulator.streaming_threads;

import java.util.Map;

import com.google.common.base.Supplier;

import il.ac.technion.cs.smarthouse.sensors.simulator.GenericSensor;

/**
 * @author Elia Traore
 * @since Jun 22, 2017
 */
public class SupplierStreamer extends MsgStreamerThread {

    private Supplier<Map<String, Object>> msgGenerator;

    public SupplierStreamer(GenericSensor sensor, Long streamingInterval, Supplier<Map<String, Object>> msgGenerator) {
        super(sensor, streamingInterval);
        this.msgGenerator = msgGenerator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see il.ac.technion.cs.smarthouse.sensors.simulator.streaming_threads.
     * MsgStreamerThread#send()
     */
    @Override
    void send() {
        sensor.sendMessage(msgGenerator.get());

    }

    /*
     * (non-Javadoc)
     * 
     * @see il.ac.technion.cs.smarthouse.sensors.simulator.streaming_threads.
     * MsgStreamerThread#canStartStreaming()
     */
    @Override
    Boolean canStartStreaming() {
        return true;
    }

}
