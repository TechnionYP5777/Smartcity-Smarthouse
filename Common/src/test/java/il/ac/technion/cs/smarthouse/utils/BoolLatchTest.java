package il.ac.technion.cs.smarthouse.utils;

import java.time.LocalTime;

import org.junit.Test;

public class BoolLatchTest {

    @Test(timeout = 5000)
    @SuppressWarnings("static-method")
    public void mainTest() {
        final BoolLatch b = new BoolLatch();
        new TimedListener(() -> b.setTrueAndRelease(), LocalTime.now().plusSeconds(2), null).start();
        double startTime = System.nanoTime();
        b.blockUntilTrue();
        assert (System.nanoTime() - startTime) / 1000000 > 1500;
        startTime = System.nanoTime();
        b.blockUntilTrue();
        assert (System.nanoTime() - startTime) / 1000000 < 500;
        b.setTrueAndRelease();
        startTime = System.nanoTime();
        b.blockUntilTrue();
        assert (System.nanoTime() - startTime) / 1000000 < 500;
    }
}
