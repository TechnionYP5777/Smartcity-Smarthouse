package il.ac.technion.cs.smarthouse.system;

public abstract class ChildCore implements Savable {
    protected SystemCore systemCore;
    
    public ChildCore(final SystemCore systemCore) {
        this.systemCore = systemCore;
    }
}
