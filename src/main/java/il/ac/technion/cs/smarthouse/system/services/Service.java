package il.ac.technion.cs.smarthouse.system.services;

import il.ac.technion.cs.smarthouse.system.SystemCore;

public abstract class Service {

    protected SystemCore systemCore;
    
    public Service(final SystemCore $) {
        systemCore = $;
    }
}
