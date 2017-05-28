package il.ac.technion.cs.smarthouse.system.cores;

import il.ac.technion.cs.smarthouse.system.Savable;
import il.ac.technion.cs.smarthouse.system.SystemCore;

public abstract class ChildCore implements Savable {
    protected SystemCore systemCore;

    public ChildCore(final SystemCore systemCore) {
        this.systemCore = systemCore;
    }
}
