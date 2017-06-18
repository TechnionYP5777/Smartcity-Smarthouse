package il.ac.technion.cs.smarthouse.utils;

import org.junit.Test;

public class TimeCounterTest {
    @Test
    public void mainTest() throws InterruptedException {
        TimeCounter t = new TimeCounter();
        t.start();
        assert t.getTimePassedSecs() < 1;
        Thread.sleep(1500);
        assert t.getTimePassedSecs() > 1 && t.getTimePassedMillis() > 1200 && t.getTimePassedNanos() > 1200000;
    }
}
