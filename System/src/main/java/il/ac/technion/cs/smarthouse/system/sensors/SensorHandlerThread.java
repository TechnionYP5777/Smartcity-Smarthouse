package il.ac.technion.cs.smarthouse.system.sensors;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.networking.messages.MessageType;
import il.ac.technion.cs.smarthouse.networking.messages.SensorMessage;
import il.ac.technion.cs.smarthouse.networking.messages.SensorMessage.IllegalMessageBaseExecption;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;

/**
 * A sensors handler thread is a class that handles a specific connection with a
 * sensor. The class can parse the different incoming messages and act
 * accordingly.
 * 
 * @author Elia Traore
 * @author Yarden
 * @since 24.12.16
 */
public class SensorHandlerThread extends SensorManagingThread {
    private static Logger log = LoggerFactory.getLogger(SensorHandlerThread.class);

    private final Map<String, String> legalSystemPaths = new HashMap<>();// short
                                                                         // path
                                                                         // &
                                                                         // full
                                                                         // path
    private Map<String, String> dataBuffer = new HashMap<>(); // short path &
                                                              // counter
    private String donePath;

    public SensorHandlerThread(final Socket client, final FileSystem fs) {
        super(client, fs);
    }

    @Override
    protected void handleSensorMessage(final SensorMessage msg) {
        switch (msg.getType()) {
            case REGISTRATION:
                handleRegisterMessage(msg);
                break;
            case UPDATE:
                handleUpdateMessage(msg);
                break;
            default:
                ;
        }
    }

    private void handleRegisterMessage(final SensorMessage msg) {
        log.info(msg.toJson() + "\n" + msg.getObservationSendingPaths());
        filesystem.sendMessage("UNDEFINED",
                        FileSystemEntries.LOCATION.buildPath(msg.getSensorCommName(), msg.getSensorId()));

        donePath = FileSystemEntries.DONE_SENDING_MSG.buildPath(msg.getSensorCommName(), msg.getSensorId());
        msg.getObservationSendingPaths().stream().forEach(
                        p -> legalSystemPaths.put(p, FileSystemEntries.SENSORS_DATA.buildPath(p, msg.getSensorId())));

        msg.getObservationSendingPaths().stream()
                        .forEach(p -> log.info("Resolved " + p + " to the full path:" + legalSystemPaths.get(p)));
        log.info("done showing resolved paths");
        try {
            new SensorMessage(MessageType.SUCCESS_ANSWER).send(out, null);
        } catch (final IllegalMessageBaseExecption e) {}
    }

    private void handleUpdateMessage(final SensorMessage msg) {
        for (final String path : msg.getData().keySet())
            bufferOrSend(path, msg.getData().get(path));
    }

    private void bufferOrSend(final String path, final String data) {
        final String oldData = dataBuffer.put(path, data);

        // we  have  data waiting to be sent  on all paths
        final Boolean bufferIsReady = dataBuffer.size() == legalSystemPaths.size(); 

        if (oldData != null && bufferIsReady)
            log.error("The dataBuffer invariant isn't preserved:" + "(path,olddata)=(" + path + "," + data
                            + "), dataBuffer=" + data + " .\nSome data update might be lost.");

        if (oldData == null && !bufferIsReady)
            return; // nothing is ready to be sent

        final Map<String, String> toSend = bufferIsReady ? dataBuffer : new HashMap<>();

        if (bufferIsReady)
            dataBuffer = new HashMap<>();
        else // oldData != null
            toSend.put(path, oldData);

        for (final String p : toSend.keySet()) {
            log.info("Sending: " + toSend.get(p) + " on path: " + legalSystemPaths.get(p));
            filesystem.sendMessage(toSend.get(p), legalSystemPaths.get(p));
        }
        // toSend.keySet().stream().forEach(p->{});
        filesystem.sendMessage(null, donePath);
    }

}
