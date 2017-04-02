package il.ac.technion.cs.eldery.system.services;

import il.ac.technion.cs.eldery.system.SystemCore;

public abstract class Service {

    protected SystemCore systemCore;
    
    public Service(final SystemCore $) {
        systemCore = $;
    }
}
