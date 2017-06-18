package il.ac.technion.cs.smarthouse.sensors;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.sensors.simulator.SensorBuilder;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.sensors.SensorsLocalServer;

/**
 * @author Elia Traore
 * @since Jun 18, 2017
 */
public class GenericSensorTest {
    private class Counter {
        private Map<String, Integer> counters = new HashMap<>();
        private String defaultKey = "##DfefaultKey##";
        
        public Counter() {
            counters.put(defaultKey, 0);
        }
        public void inc(){
            inc(defaultKey);
        }
        
        public Integer get(){
            return get(defaultKey);
        }
        
        public void inc(String counterKey){
            if(!counters.containsKey(counterKey))
                counters.put(counterKey, 0);
            counters.put(counterKey, counters.get(counterKey)+1);
        }
        
        public Integer get(String counterKey){
            return !counters.containsKey(counterKey)? 0 : counters.get(counterKey);       
        }
        @Override
        public String toString() {
            return "Counter: ["+counters+"]";
        }
        
        
    }
    
    private final String sendPath = "foo.send";
    private SensorBuilder builder;
    private SystemCore systemCore;
    private SensorsLocalServer server;

    @Before
    public void init() {
        builder = new SensorBuilder().setCommname("testComm").setAlias("testAlias");
        systemCore = new SystemCore();
        server = new SensorsLocalServer(systemCore.getFileSystem());
        new Thread(server).start();
    }
    
    @After public void shutdown(){
        server.closeSockets();
    }
    


    @Test
    public void sendMessageSuccesfullyTest() throws InterruptedException {
        final Counter c = new Counter();
        
        systemCore.getFileSystem().subscribe((path, val) -> c.inc(),
                        FileSystemEntries.SENSORS_DATA.buildPath(sendPath));
        final Map<String, Object> data = new HashMap<>();
        
        data.put(sendPath, "yo");
        builder.addInfoSendingPath(sendPath, String.class).build().sendMessage(data);
        Thread.sleep(2000);
        Assert.assertEquals((Integer) 1, c.get());
    }

    @SuppressWarnings("rawtypes")
    private Map<String,List> createRanges(Function<Class,String> pathOf, Collection<Class> types){
        Map<String,List> ranges = new HashMap<>();
        types.forEach(cls -> {
            String path = pathOf.apply(cls);
            List vals;
            if(String.class.equals(cls))
                vals = Arrays.asList("foo", "bar", "foobar");
            else if(Boolean.class.equals(cls))
                vals =  Arrays.asList(false);
            else if(Integer.class.equals(cls))
                vals =  Arrays.asList(2,43);
            else //if(Double.class.equals(cls))
                vals =  Arrays.asList(1.0,11.0);
            ranges.put(path, vals);
        });
        return ranges;
    }

    @SuppressWarnings("rawtypes")
    private Boolean inRange(List range, Object val, Class valsCls){
        List vals;
        if(String.class.equals(valsCls))
            return range.contains(val);
        else if(Boolean.class.equals(valsCls)){
            Boolean bVal = Boolean.TRUE.toString().equals(val)? true : Boolean.FALSE.toString().equals(val) ? false: null;
            return bVal != null && range.contains(bVal);
        }
        else if(Integer.class.equals(valsCls)){
            Integer iVal = Integer.parseInt((String) val), from = (Integer)range.get(0), to = (Integer)range.get(1);
            return from <= iVal && iVal < to ;
        }
        else if(Double.class.equals(valsCls)){
            Double iVal = Double.parseDouble((String) val), from = (Double)range.get(0), to = (Double)range.get(1);
            return from <= iVal && iVal < to ;
        }
        return false;
    }
    
    @Test @SuppressWarnings("rawtypes") 
    public void StreamMessagesSuccesfullyTest() throws InterruptedException {
        Function<Class,String> pathOf = cls -> sendPath + "." + cls.toString().replace(' ', '.');
        
        //key is always pathOf(cls)
        final Map<String, Class> types = new HashMap<>();
        Arrays.asList(String.class, Boolean.class, Integer.class, Double.class).forEach(t -> types.put(pathOf.apply(t), t));
        final Map<String,Counter> counters = new HashMap<>();
        final Map<String,List> ranges = createRanges(pathOf, types.values());

        types.forEach( (path,cls) ->{
            counters.put(path, new Counter());
            builder.addInfoSendingPath(path, cls);
            builder.addStreamingRange(path, ranges.get(path));
        });
        
        final String receivedKey ="receivedKey", validValKey = "validValKey";   
        
        types.forEach((clsPath,cls) -> 
            systemCore.getFileSystem().subscribe((path, val) -> {
                counters.get(clsPath).inc(receivedKey);
                if(inRange(ranges.get(clsPath), val, cls))
                    counters.get(clsPath).inc(validValKey);
            }, FileSystemEntries.SENSORS_DATA.buildPath(clsPath))
        );
        
        builder.setStreamInterval(10*1L).build().streamMessages();
        
        Thread.sleep(10000);
        
        System.out.println("##### counters: "+counters);
        counters.values().forEach(cntr -> {
            assert cntr.get(receivedKey)>0 && cntr.get(receivedKey).equals(cntr.get(validValKey));
        });
    }
}
