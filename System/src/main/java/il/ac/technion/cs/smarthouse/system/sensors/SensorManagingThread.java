package il.ac.technion.cs.smarthouse.system.sensors;

import java.net.Socket;

import il.ac.technion.cs.smarthouse.system.SensorsManager;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemImpl;

public class SensorManagingThread extends Thread {
    protected final Socket client;
    protected final FileSystemImpl fsImpl;
    protected final SensorsManager sManager;
    

    public SensorManagingThread(final Socket client, final SensorsManager sManager, final FileSystemImpl fsImpl) {
        this.client = client;
        this.fsImpl = fsImpl;
        this.sManager = sManager;
    }

}
