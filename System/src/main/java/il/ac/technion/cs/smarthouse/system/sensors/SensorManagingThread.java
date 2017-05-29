package il.ac.technion.cs.smarthouse.system.sensors;

import java.net.Socket;

import il.ac.technion.cs.smarthouse.system.DatabaseHandler;

public class SensorManagingThread extends Thread {
    protected final Socket client;
    protected final DatabaseHandler databaseHandler;
    

    public SensorManagingThread(final Socket client, final DatabaseHandler databaseHandler) {
        this.client = client;
        this.databaseHandler = databaseHandler;   
    }

}
