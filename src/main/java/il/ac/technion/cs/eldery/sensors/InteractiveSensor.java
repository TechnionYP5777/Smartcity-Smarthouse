package il.ac.technion.cs.eldery.sensors;

import java.util.Map;

import il.ac.technion.cs.eldery.networking.messages.UpdateMessage;

/** This class represents a sensor that can get instructions and operate
 * accordingly.
 * @author Yarden
 * @since 31.3.17 */
public abstract class InteractiveSensor extends Sensor {
    protected int instructionsPort;
    protected InstructionHandler handler;

    public InteractiveSensor(final String id, final String commName, final String systemIP, final int systemPort, final int instructionsPort) {
        super(id, commName, systemIP, systemPort);
        this.instructionsPort = instructionsPort;
        this.handler = null;
    }

    /** Sets the operation to be made when instruction is received. This method
     * must be called before sending instructions. */
    public void setInstructionHandler(InstructionHandler h) {
        this.handler = h;
    }

    /** Extracts the instruction from a message and executes it.
     * @return <code>true</code> if the instruction was completed successfully,
     *         <code>false</code> otherwise */
    public boolean operate(UpdateMessage instruction) {
        return handler.applyInstruction(instruction.getData());
    }
}

interface InstructionHandler {
    /** Acts due to an instruction.
     * @return <code>true</code> if the action was successful,
     *         <code>false</code> otherwise */
    boolean applyInstruction(Map<String, String> instructionData);
}
