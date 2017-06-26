/**
 * 
 */
package il.ac.technion.cs.smarthouse.DeveloperSimulator;

import il.ac.technion.cs.smarthouse.gui_controller.GuiController;
import il.ac.technion.cs.smarthouse.sensors.PathType;
import il.ac.technion.cs.smarthouse.sensors.simulator.GenericSensor;
import il.ac.technion.cs.smarthouse.sensors.simulator.SensorsSimulator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

/**
 * @author Elia Traore
 * @since Jun 26, 2017
 */
public abstract class SimulatorGuiController extends GuiController<SensorsSimulator> {
	private static String selectedSensor;
	
	protected static void setSelectedSensor(String id) {
		selectedSensor = id;
	}
	
	protected static String getSelectedSensor() {
		return selectedSensor;
	}
	
	protected static ObservableList<Pair<String,Class>> getObservablePaths(GenericSensor sensor){
		ObservableList<Pair<String,Class>> list = FXCollections.observableArrayList();
		sensor.getPathsWithClasses(PathType.INFO_SENDING).forEach((path,cls)-> list.add(new Pair<String,Class>(path, cls)));
		return list;
	}
}
