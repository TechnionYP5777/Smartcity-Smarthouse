package il.ac.technion.cs.smarthouse.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Mostly a wrapper for {@link Timer}
 * @author RON
 * @author Elia Traore
 * @since 31-05-2017
 */
public class TimedListener {
    private static Date localTimeToDate(final LocalTime $) {
        return Date.from($.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
    }

    final Runnable task;
    final LocalTime timeToStartOn;
    final Long repeatInMilisec;
    Timer currentTimer;

    /**
     * TimedListener c'tor
     * @param task the task to run
     * @param timeToStartOn The time at which task is to be executed. If the time is in the past or null, the task is scheduled for immediate execution.
     * @param repeatInMilisec The time in milliseconds between successive task executions. If null, the task will only run once.
     */
    public TimedListener(final Runnable task, final LocalTime timeToStartOn, final Long repeatInMilisec) {
        this.task = task;
        this.timeToStartOn = timeToStartOn;
        this.repeatInMilisec = repeatInMilisec;
    }

    public void start() {
        if (currentTimer != null)
            return;
        
        TimerTask t = new TimerTask() {
            @Override
            public void run() {
                task.run();
            }
        };
        
        currentTimer = new Timer();
        
        if (repeatInMilisec == null && timeToStartOn == null)
            currentTimer.schedule(t, localTimeToDate(LocalTime.MIN));
        else if (repeatInMilisec == null && timeToStartOn != null)
            currentTimer.schedule(t, localTimeToDate(timeToStartOn));
        else if (repeatInMilisec != null && timeToStartOn != null)
            currentTimer.schedule(t, localTimeToDate(timeToStartOn), repeatInMilisec);
        else if (repeatInMilisec != null && timeToStartOn == null)
            currentTimer.schedule(t, localTimeToDate(LocalTime.now()), repeatInMilisec);
    }

    public void kill() {
        if (currentTimer != null)
            currentTimer.cancel();
        currentTimer = null;
    }
}
