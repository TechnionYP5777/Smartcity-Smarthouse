package il.ac.technion.cs.smarthouse.applications.dashboard;
/**
 * 
 */

import il.ac.technion.cs.smarthouse.developers_api.SmarthouseApplication;
import il.ac.technion.cs.smarthouse.developers_api.application_builder.DataObject;
import il.ac.technion.cs.smarthouse.sensors.stove.gui.StoveSensorSimulator;
import il.ac.technion.cs.smarthouse.sensors.vitals.gui.VitalsSensorSimulator;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
import il.ac.technion.cs.smarthouse.system.services.file_system_service.FileSystemService;

/**
 * @author Elia Traore
 * @since May 29, 2017
 */
public class DashboardGui extends SmarthouseApplication {
    
	public static void main(String[] args) throws Exception {
		launch(StoveSensorSimulator.class, VitalsSensorSimulator.class);
	}

	@Override
	public String getApplicationName() {
		return "Dashboard";
	}

	@Override
	public void onLoad() throws Exception {
		// no need to subscribe to sensors automatically
	    DataObject<Controller> c = new DataObject<>();
	    getAppBuilder().getCustomRegionBuilder().setTitle(null).add("dashboard_ui.fxml", c);
		c.getData().setFileSystem((FileSystemService) super.getService(ServiceType.FILE_SYSTEM_SERVICE));
	}
}
