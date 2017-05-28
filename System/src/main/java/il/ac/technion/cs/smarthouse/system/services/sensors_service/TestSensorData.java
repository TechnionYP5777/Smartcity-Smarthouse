package il.ac.technion.cs.smarthouse.system.services.sensors_service;

public class TestSensorData extends SensorData {
    @PathVal("a.b.c")
    int systolic;
    
    public static void main(String[] args) {
        TestSensorData t = new TestSensorData();
        
        System.out.println(t.getClass().getDeclaredFields()[0].getAnnotation(PathVal.class).value());
    }
}
