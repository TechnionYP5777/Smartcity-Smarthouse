package il.ac.technion.cs.smarthouse.applications.dashboard;
/**
 * 
 */

import il.ac.technion.cs.smarthouse.system.applications.api.SmartHouseApplication;

/**
 * @author Elia Traore
 * @since May 29, 2017
 */
public class DashboardGui extends SmartHouseApplication{
	private Controller controller;
	
	public static void main(String[] args) throws Exception {
        launch();
    }
	
	@Override public String getApplicationName() {
        return "Dashboard";
    }
	
	@Override public void onLoad() throws Exception {
		//no need to subscribe to sensors automatically
		controller = super.setContentView("dashboard_ui.fxml");
	}
}
