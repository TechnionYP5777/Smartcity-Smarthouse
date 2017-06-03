package il.ac.technion.cs.smarthouse.applications.dashboard;
/**
 * 
 */

import il.ac.technion.cs.smarthouse.sensors.stove.gui.StoveSensorSimulator;
import il.ac.technion.cs.smarthouse.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
import il.ac.technion.cs.smarthouse.system.services.file_system_service.FileSystemService;

/**
 * @author Elia Traore
 * @since May 29, 2017
 */
public class DashboardGui extends SmartHouseApplication{
	private Controller controller;
	
	public static void main(String[] args) throws Exception {
        launch(StoveSensorSimulator.class);
    }
	
	@Override public String getApplicationName() {
        return "Dashboard";
    }
	
	@Override public void onLoad() throws Exception {
		//no need to subscribe to sensors automatically
		controller = super.setContentView("dashboard_ui.fxml");
		controller.setFileSystem((FileSystemService)super.getService(ServiceType.FILE_SYSTEM_SERVICE));
	}
}
