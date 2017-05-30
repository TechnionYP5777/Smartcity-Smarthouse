package il.ac.technion.cs.smarthouse.system.services.sensors_service;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;

import org.junit.Assert;

import il.ac.technion.cs.smarthouse.system.SensorLocation;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemImpl;
import il.ac.technion.cs.smarthouse.utils.BoolLatch;
import il.ac.technion.cs.smarthouse.utils.UuidGenerator;

public class SensorApiTest {
    static final String PARAM1_BASE_PATH = "my_sensor.data.param1";
    static final String SENSOR_COMM_NAME = "iCoolSensor";
    static final String SENSOR_SID = UuidGenerator.GenerateUniqueIDstring();
    
    private FileSystem fileSystem;
    
    @Before
    public void init() {
        fileSystem = new FileSystemImpl();
    }
    
    private void printTestName() {
        System.out.println("\n>> Running test: " + Thread.currentThread().getStackTrace()[2].getMethodName());
    }
    
    public <T extends SensorData> SensorApi<T> getSensor(final String commercialName, final Class<T> sensorDataClass, final SensorLocation defaultLocation) {
        return new SensorApiImpl<>(fileSystem, commercialName, defaultLocation, sensorDataClass);
    }
    
    public void sensorConnect() {
        sensorSetLocation(SensorLocation.UNDEFINED);
        System.out.println(">> sensor has connected");
    }
    
    public void sensorSetLocation(SensorLocation l) {
        fileSystem.sendMessage(l, FileSystemEntries.LOCATION.buildPath(SENSOR_COMM_NAME, SENSOR_SID));
    }
    
    public void sensorSendMsg(String param1AsStr) {
        fileSystem.sendMessage(param1AsStr, FileSystemEntries.SENSORS_DATA.buildPath(PARAM1_BASE_PATH, SENSOR_SID));
        fileSystem.sendMessage(null, FileSystemEntries.DONE_SENDING_MSG.buildPath(SENSOR_COMM_NAME, SENSOR_SID));
    }
    
    @Test
    public void connectionTest_beforeSensorConnected() {
        printTestName();
        SensorApi<MySensor> s = getSensor(SENSOR_COMM_NAME, MySensor.class, null);
        
        Assert.assertEquals(s.getCommercialName(), SENSOR_COMM_NAME);
        Assert.assertEquals(s.getSensorLocation(), SensorLocation.UNDEFINED);
        Assert.assertEquals(s.isConnected(), false);
        
        sensorConnect();
        
        Assert.assertEquals(s.getCommercialName(), SENSOR_COMM_NAME);
        Assert.assertEquals(s.getSensorLocation(), SensorLocation.UNDEFINED);
        Assert.assertEquals(s.isConnected(), true);
        
        sensorSetLocation(SensorLocation.BASEMENT);
        
        Assert.assertEquals(s.getCommercialName(), SENSOR_COMM_NAME);
        Assert.assertEquals(s.getSensorLocation(), SensorLocation.BASEMENT);
        Assert.assertEquals(s.isConnected(), true);
    }
    
    @Test
    public void connectionTest_afterSensorConnected() {
        printTestName();
        sensorConnect();
        
        SensorApi<MySensor> s = getSensor(SENSOR_COMM_NAME, MySensor.class, null);
        
        Assert.assertEquals(s.getCommercialName(), SENSOR_COMM_NAME);
        Assert.assertEquals(s.getSensorLocation(), SensorLocation.UNDEFINED);
        Assert.assertEquals(s.isConnected(), true);
    }
    
    @Test
    public void connectionTest_OnSpecificLocation() {
        printTestName();
        SensorApi<MySensor> s = getSensor(SENSOR_COMM_NAME, MySensor.class, SensorLocation.KITCHEN);
        
        Assert.assertEquals(s.getCommercialName(), SENSOR_COMM_NAME);
        Assert.assertEquals(s.getSensorLocation(), SensorLocation.UNDEFINED);
        Assert.assertEquals(s.isConnected(), false);
        
        sensorConnect();
        
        Assert.assertEquals(s.getCommercialName(), SENSOR_COMM_NAME);
        Assert.assertEquals(s.getSensorLocation(), SensorLocation.UNDEFINED);
        Assert.assertEquals(s.isConnected(), false);
        
        sensorSetLocation(SensorLocation.BASEMENT);
        
        Assert.assertEquals(s.getCommercialName(), SENSOR_COMM_NAME);
        Assert.assertEquals(s.getSensorLocation(), SensorLocation.UNDEFINED);
        Assert.assertEquals(s.isConnected(), false);
        
        sensorSetLocation(SensorLocation.KITCHEN);
        
        Assert.assertEquals(s.getCommercialName(), SENSOR_COMM_NAME);
        Assert.assertEquals(s.getSensorLocation(), SensorLocation.KITCHEN);
        Assert.assertEquals(s.isConnected(), true);
    }
    
    @Test(timeout = 1000)
    public void subscribeTest() {
        printTestName();
        final BoolLatch wasCalled = new BoolLatch();
        final int param1Data = 1234;
        
        SensorApi<MySensor> s = getSensor(SENSOR_COMM_NAME, MySensor.class, null);
        
        Assert.assertEquals(s.isConnected(), false);
        sensorConnect();
        Assert.assertEquals(s.isConnected(), true);
        
        s.subscribe(sensorData->{
            Assert.assertEquals(sensorData.getParam1(), param1Data);
            Assert.assertEquals(sensorData.getCommercialName(), SENSOR_COMM_NAME);
            Assert.assertEquals(sensorData.getSensorLocation(), SensorLocation.UNDEFINED);
            wasCalled.setTrueAndRelease();
        });
        
        sensorSendMsg(param1Data + "");
        
        wasCalled.blockUntilTrue();
    }
    
    @Test(timeout = 4000)
    public void subscribeOnTimeTest() {
        final BoolLatch wasCalled = new BoolLatch();
        
        SensorApi<MySensor> s = getSensor(SENSOR_COMM_NAME, MySensor.class, null);
        
        Assert.assertEquals(s.isConnected(), false);
        sensorConnect();
        Assert.assertEquals(s.isConnected(), true);
        
        s.subscribeOnTime(sensorData->{
            Assert.assertEquals(sensorData.getParam1(), 0);
            Assert.assertEquals(sensorData.getCommercialName(), SENSOR_COMM_NAME);
            Assert.assertEquals(sensorData.getSensorLocation(), SensorLocation.UNDEFINED);
            wasCalled.setTrueAndRelease();
        }, LocalTime.now().plusSeconds(2));
        
        wasCalled.blockUntilTrue();
    }
    
    @Test(timeout = 4000)
    public void sendMsgBeforeSubscribeTest() {
        printTestName();
        final BoolLatch wasCalled = new BoolLatch();
        final int param1Data = 1234;
        
        SensorApi<MySensor> s = getSensor(SENSOR_COMM_NAME, MySensor.class, null);
        
        sensorConnect();
        sensorSendMsg(param1Data + "");
        
        s.subscribeOnTime(sensorData->{
            Assert.assertEquals(sensorData.getParam1(), param1Data);
            Assert.assertEquals(sensorData.getCommercialName(), SENSOR_COMM_NAME);
            Assert.assertEquals(sensorData.getSensorLocation(), SensorLocation.UNDEFINED);
            wasCalled.setTrueAndRelease();
        }, LocalTime.now().plusSeconds(2));
        
        wasCalled.blockUntilTrue();
    }
    
    private int counter;
    @Test
    public void subscribeOnTimeWithRepeatAndUnsubscribeTest() throws InterruptedException {
        printTestName();
        final int param1Data = 1234;
        
        SensorApi<MySensor> s = getSensor(SENSOR_COMM_NAME, MySensor.class, null);
        
        sensorConnect();
        sensorSendMsg(param1Data + "");
        
        String id = s.subscribeOnTime(sensorData->{
            Assert.assertEquals(sensorData.getParam1(), param1Data);
            Assert.assertEquals(sensorData.getCommercialName(), SENSOR_COMM_NAME);
            Assert.assertEquals(sensorData.getSensorLocation(), SensorLocation.UNDEFINED);
            ++counter;
        }, LocalTime.now().plusSeconds(4), 4000);
        Thread.sleep(1000);//1000
        System.out.println(counter);
        assert counter == 0;
        Thread.sleep(4000);//5000
        System.out.println(counter);
        assert counter == 1;
        Thread.sleep(5000);//9000
        System.out.println(counter);
        assert counter == 2;
        s.unsubscribe(id);
        Thread.sleep(4000);//13000
        System.out.println(counter);
        assert counter == 2;
    }
    
    @Test
    public void subscribeOnTimeWithRepeatAndUnsubscribeTest2() throws InterruptedException {
        printTestName();
        final int param1Data = 1234;
        
        SensorApi<MySensor> s = getSensor(SENSOR_COMM_NAME, MySensor.class, null);
        
        sensorConnect();
        sensorSendMsg(param1Data + "");
        
        String id = s.subscribeOnTime(sensorData->{
            Assert.assertEquals(sensorData.getParam1(), param1Data);
            Assert.assertEquals(sensorData.getCommercialName(), SENSOR_COMM_NAME);
            Assert.assertEquals(sensorData.getSensorLocation(), SensorLocation.UNDEFINED);
            ++counter;
        }, 4000);
        Thread.sleep(1000);//1000
        System.out.println(counter);
        assert counter == 1;
        Thread.sleep(4000);//5000
        System.out.println(counter);
        assert counter == 2;
        Thread.sleep(5000);//9000
        System.out.println(counter);
        assert counter == 3;
        s.unsubscribe(id);
        Thread.sleep(4000);//13000
        System.out.println(counter);
        assert counter == 3;
    }
    
    public static class MySensor extends SensorData {
        @SystemPath(PARAM1_BASE_PATH)
        private int param1;
        
        public int getParam1() {
            return param1;
        }
    }
}
