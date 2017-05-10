package il.ac.technion.cs.smarthouse.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoolLatch {
    private static Logger log = LoggerFactory.getLogger(BoolLatch.class);
    
    private boolean value;
    
    public synchronized void blockUntilTrue() {
        if (!value)
            try {
                wait();
            } catch (InterruptedException e) {
                log.error("wait was interrupted", e);
            }
    }
    
    public synchronized void setTrueAndRelease() {
        if (value)
            return;
        
        value = true;
        notifyAll();
    }
}
