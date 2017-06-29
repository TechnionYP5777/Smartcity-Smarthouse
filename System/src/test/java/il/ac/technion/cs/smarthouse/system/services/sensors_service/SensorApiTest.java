package il.ac.technion.cs.smarthouse.system.services.sensors_service;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;

import org.junit.Assert;

import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemImpl;
import il.ac.technion.cs.smarthouse.utils.BoolLatch;
import il.ac.technion.cs.smarthouse.utils.UuidGenerator;

public class SensorApiTest {
    static final String PARAM1_BASE_PATH = "my_sensor.data.param1";
    static final String SENSOR_COMM_NAME = "iCoolSensor";
    static final String SENSOR_ALIAS_NAME_1 = "MyAlias_1";
    static final String SENSOR_ALIAS_NAME_2 = "MyAlias_2";
    static final String SENSOR_SID = UuidGenerator.GenerateUniqueIDstring();

    private FileSystem fileSystem;

    @Before
    public void init() {
        fileSystem = new FileSystemImpl();
    }

    public <T extends SensorData> SensorApi<T> getSensor(final String commercialName, final Class<T> sensorDataClass,
                    final String alias) {
        return new SensorApiImpl<>(fileSystem, commercialName, sensorDataClass, alias);
    }

    public void sensorConnect() {
        sensorSetAlias(SENSOR_ALIAS_NAME_1);

    }

    public void sensorSetAlias(String alias) {
        fileSystem.sendMessage(alias, FileSystemEntries.ALIAS.buildPath(SENSOR_COMM_NAME, SENSOR_SID));
    }

    public void sensorSendMsg(String param1AsStr) {
        fileSystem.sendMessage(param1AsStr,
                        FileSystemEntries.SENSORS_DATA_FULL__WITH_SENSOR_ID.buildPath(PARAM1_BASE_PATH, SENSOR_SID));
        fileSystem.sendMessage(null, FileSystemEntries.DONE_SENDING_MSG.buildPath(SENSOR_COMM_NAME, SENSOR_SID));
    }

    @Test
    public void connectionTest_beforeSensorConnected() {

        SensorApi<MySensor> s = getSensor(SENSOR_COMM_NAME, MySensor.class, null);

        Assert.assertEquals(s.getCommercialName(), SENSOR_COMM_NAME);
        Assert.assertEquals(s.isConnected(), false);

        sensorConnect();

        Assert.assertEquals(s.getCommercialName(), SENSOR_COMM_NAME);
        Assert.assertEquals(s.getSensorAlias(), SENSOR_ALIAS_NAME_1);
        Assert.assertEquals(s.isConnected(), true);

        sensorSetAlias(SENSOR_ALIAS_NAME_2);

        Assert.assertEquals(s.getCommercialName(), SENSOR_COMM_NAME);
        Assert.assertEquals(s.getSensorAlias(), SENSOR_ALIAS_NAME_2);
        Assert.assertEquals(s.isConnected(), true);
    }

    @Test
    public void connectionTest_afterSensorConnected() {

        sensorConnect();

        SensorApi<MySensor> s = getSensor(SENSOR_COMM_NAME, MySensor.class, null);

        Assert.assertEquals(s.getCommercialName(), SENSOR_COMM_NAME);
        Assert.assertEquals(s.isConnected(), true);
    }

    @Test
    public void connectionTest_BySpecificAlias() {

        SensorApi<MySensor> s = getSensor(SENSOR_COMM_NAME, MySensor.class, SENSOR_ALIAS_NAME_2);

        Assert.assertEquals(s.getCommercialName(), SENSOR_COMM_NAME);
        Assert.assertEquals(s.isConnected(), false);

        sensorConnect();

        Assert.assertEquals(s.getCommercialName(), SENSOR_COMM_NAME);

        Assert.assertEquals(s.getSensorAlias(), "");
        Assert.assertEquals(s.isConnected(), false);

        s.reselectSensorByAlias(SENSOR_ALIAS_NAME_1);

        Assert.assertEquals(s.getCommercialName(), SENSOR_COMM_NAME);
        Assert.assertEquals(s.getSensorAlias(), SENSOR_ALIAS_NAME_1);
        Assert.assertEquals(s.isConnected(), true);

        s.reselectSensorByAlias(SENSOR_ALIAS_NAME_2);

        Assert.assertEquals(s.getCommercialName(), SENSOR_COMM_NAME);
        Assert.assertEquals(s.getSensorAlias(), "");
        Assert.assertEquals(s.isConnected(), false);

        sensorSetAlias(SENSOR_ALIAS_NAME_2);

        Assert.assertEquals(s.getCommercialName(), SENSOR_COMM_NAME);
        Assert.assertEquals(s.getSensorAlias(), SENSOR_ALIAS_NAME_2);
        Assert.assertEquals(s.isConnected(), true);
    }

    @Test(timeout = 1000)
    public void subscribeTest() {

        final BoolLatch wasCalled = new BoolLatch();
        final int param1Data = 1234;

        SensorApi<MySensor> s = getSensor(SENSOR_COMM_NAME, MySensor.class, null);

        Assert.assertEquals(s.isConnected(), false);
        sensorConnect();
        Assert.assertEquals(s.isConnected(), true);

        s.subscribe(sensorData -> {
            Assert.assertEquals(sensorData.getParam1(), param1Data);
            Assert.assertEquals(sensorData.getCommercialName(), SENSOR_COMM_NAME);
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

        s.subscribeOnTime(sensorData -> {
            Assert.assertEquals(sensorData.getParam1(), 0);
            Assert.assertEquals(sensorData.getCommercialName(), SENSOR_COMM_NAME);
            wasCalled.setTrueAndRelease();
        }, LocalTime.now().plusSeconds(2));

        wasCalled.blockUntilTrue();
    }

    @Test(timeout = 4000)
    public void sendMsgBeforeSubscribeTest() {

        final BoolLatch wasCalled = new BoolLatch();
        final int param1Data = 1234;

        SensorApi<MySensor> s = getSensor(SENSOR_COMM_NAME, MySensor.class, null);

        sensorConnect();
        sensorSendMsg(param1Data + "");

        s.subscribeOnTime(sensorData -> {
            Assert.assertEquals(sensorData.getParam1(), param1Data);
            Assert.assertEquals(sensorData.getCommercialName(), SENSOR_COMM_NAME);
            wasCalled.setTrueAndRelease();
        }, LocalTime.now().plusSeconds(2));

        wasCalled.blockUntilTrue();
    }

    private int counter;

    @Test
    public void subscribeOnTimeWithRepeatAndUnsubscribeTest() throws InterruptedException {

        final int param1Data = 1234;

        SensorApi<MySensor> s = getSensor(SENSOR_COMM_NAME, MySensor.class, null);

        sensorConnect();
        sensorSendMsg(param1Data + "");

        String id = s.subscribeOnTime(sensorData -> {
            Assert.assertEquals(sensorData.getParam1(), param1Data);
            Assert.assertEquals(sensorData.getCommercialName(), SENSOR_COMM_NAME);
            ++counter;
        }, LocalTime.now().plusSeconds(4), 4000);
        Thread.sleep(1000);// 1000

        assert counter == 0;
        Thread.sleep(4000);// 5000

        assert counter == 1;
        Thread.sleep(5000);// 9000

        assert counter == 2;
        s.unsubscribe(id);
        Thread.sleep(4000);// 13000

        assert counter == 2;
    }

    @Test
    public void subscribeOnTimeWithRepeatAndUnsubscribeTest2() throws InterruptedException {

        final int param1Data = 1234;

        SensorApi<MySensor> s = getSensor(SENSOR_COMM_NAME, MySensor.class, null);

        sensorConnect();
        sensorSendMsg(param1Data + "");

        String id = s.subscribeOnTime(sensorData -> {
            Assert.assertEquals(sensorData.getParam1(), param1Data);
            Assert.assertEquals(sensorData.getCommercialName(), SENSOR_COMM_NAME);
            ++counter;
        }, 4000);
        Thread.sleep(1000);// 1000

        assert counter == 1;
        Thread.sleep(4000);// 5000

        assert counter == 2;
        Thread.sleep(5000);// 9000

        assert counter == 3;
        s.unsubscribe(id);
        Thread.sleep(4000);// 13000

        assert counter == 3;
    }

    private class MySensor extends SensorData {
        @SystemPath(PARAM1_BASE_PATH) private int param1;

        public int getParam1() {
            return param1;
        }
    }
}
