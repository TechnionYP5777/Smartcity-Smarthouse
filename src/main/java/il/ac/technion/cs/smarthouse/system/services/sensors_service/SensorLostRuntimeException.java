package il.ac.technion.cs.smarthouse.system.services.sensors_service;

import il.ac.technion.cs.smarthouse.system.exceptions.SensorNotFoundException;

/**
 * An unchecked runtime exception that will be thrown by the SensorApi if the sensor was lost.
 * We assume that the client can't handle this kind of exception anyway, so we made it a runtime exception (unchecked).
 * @author RON
 * @since 07-04-2017
 */
public class SensorLostRuntimeException extends RuntimeException {
    
    private static final long serialVersionUID = -0x18C7C8EB474F0E0EL;

    public SensorLostRuntimeException(SensorNotFoundException $) {
        super($ == null ? "" : $.getMessage());
    }
}
