package il.ac.technion.cs.smarthouse.utils;

/**
 * Convenient time counting
 * 
 * @author RON
 * @since 07-06-2017
 */
public class TimeCounter {
    private double startTimeNano;

    public TimeCounter start() {
        startTimeNano = System.nanoTime();
        return this;
    }

    public double getTimePassedNanos() {
        return System.nanoTime() - startTimeNano;
    }

    public double getTimePassedMillis() {
        return (System.nanoTime() - startTimeNano) / 1000000;
    }

    public double getTimePassedSecs() {
        return (System.nanoTime() - startTimeNano) / 1000000000;
    }
}
