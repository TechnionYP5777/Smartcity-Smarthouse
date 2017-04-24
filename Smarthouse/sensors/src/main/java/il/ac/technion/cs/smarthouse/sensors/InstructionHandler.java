package il.ac.technion.cs.smarthouse.sensors;

import java.util.Map;

/** @author Yarden
 * @since 31.3.17 */
public interface InstructionHandler {
    /** Acts due to an instruction.
     * @return <code>true</code> if the action was successful,
     *         <code>false</code> otherwise */
    boolean applyInstruction(Map<String, String> instructionData);
}