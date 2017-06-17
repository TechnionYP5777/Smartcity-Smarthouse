package il.ac.technion.cs.smarthouse.system.sensors;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.sensors.InteractiveSensor;
import il.ac.technion.cs.smarthouse.sensors.PathType;
import il.ac.technion.cs.smarthouse.sensors.simulator.GenericSensor;
import il.ac.technion.cs.smarthouse.sensors.simulator.SensorBuilder;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorApi;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorData;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorsService;
import il.ac.technion.cs.smarthouse.utils.Random;

/**
 * @author Elia Traore
 * @since 07.06.17
 */
public class InstructionsSenderThreadTest {

    private static class TestISdata extends SensorData {}

    Integer numOfInstructionReceived;
    // system
    private FileSystem fileSystem;
    private SensorsLocalServer server;
    private SystemCore systemCore;
    private SensorsService sensorsService;

    // sensor + app
    private SensorApi<TestISdata> sensorRepresentingObj;
    private SensorBuilder builder;
    
    final String commname = "testSensor";
    final String instPath = "acu.state";


    @Before
    public void initSystem() {
        systemCore = new SystemCore();
        fileSystem = systemCore.getFileSystem();
        server = new SensorsLocalServer(fileSystem);
        sensorsService = (SensorsService) systemCore.getSystemServiceManager().getService(ServiceType.SENSORS_SERVICE);
        new Thread(server).start();

        numOfInstructionReceived = 0;
        builder = new SensorBuilder()
                        .setSensorId(Random.sensorId())
                        .setAlias("myAlias")
                        .setCommname(commname)
                        .addPath(PathType.INSTRUCTION_RECEIVING, instPath, String.class)
                        .setPollingInterval((long)10)
                        .setInstructionHandler((path, inst) -> {
                            if(inst.equals(true + ""))
                                incNumOfInstructions();
                            return true;
                        });
                        
        sensorRepresentingObj = sensorsService.getSensor(commname, TestISdata.class);
    }

    @After
    public void closeServerSocket() throws InterruptedException {
        server.closeSockets();
        Thread.sleep(1000);
    }

    // ------------------------- private helpers ------------------------------
    private void instructInc() {
        sensorRepresentingObj.instruct(true + "", instPath);
    }
    
    //instruction verification
    private void incNumOfInstructions(){
        ++numOfInstructionReceived;
    }
    private Boolean didGetInstruction() {
        return numOfInstructionReceived > 0;
    }
    private Integer numOfReceivedInstructions() {
        return numOfInstructionReceived;
    }


    // ------------------------- tests ----------------------------------------
    @Test
    public void GetsInstuctionOnPathTest() throws InterruptedException {
        builder.build().connect();
        instructInc();
        Thread.sleep(100);
        assert didGetInstruction();
    }

    // TODO: move this test to InteractiveSensorTest class [create mutual parent
    // class with initSystem]
    @Test
    public void GetsInstuctionByPollingTest() {
        final long waitTime = 500;
        builder.setPollingInterval(waitTime).build().connect();

        final Integer times = 4;
        new Thread(() -> {
            for (int i = 0; i < times; ++i) {
                instructInc();
                try {
                    Thread.sleep(2 * waitTime);
                } catch (final InterruptedException e) {
                    assert false;
                }
            }
        }).start();

        try {
            Thread.sleep(2 * waitTime * (times + 1)); // +1 to assure the other
                                                      // threads are done
        } catch (final InterruptedException e) {
            assert false;
        }

        assert numOfReceivedInstructions() == times;
    }

    @Test
    public void GetsAlreadyWaitingInstructionTest() {
        instructInc();

        GenericSensor sensor = builder.setPollingInterval((long)10000).build().connect();
        if(!didGetInstruction())
            sensor.waitForInstruction();
                        
        assert didGetInstruction();
    }

}
