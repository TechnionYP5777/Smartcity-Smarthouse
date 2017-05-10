package il.ac.technion.cs.smarthouse.system.sensors;

import java.io.PrintWriter;

/** Maps between a connected sensor and an output stream of it.
 * @author Yarden
 * @since 6.4.17 */
public interface OutputMapper {
    void store(String id, PrintWriter out);

}