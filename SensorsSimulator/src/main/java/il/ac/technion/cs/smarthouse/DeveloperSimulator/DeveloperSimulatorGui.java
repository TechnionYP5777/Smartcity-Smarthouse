package il.ac.technion.cs.smarthouse.DeveloperSimulator;

import il.ac.technion.cs.smarthouse.sensors.simulator.SensorsSimulator;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * @author Roy Shchory
 * @since Jun 17, 2017
 */
public class DeveloperSimulatorGui extends Application{

	private SensorsSimulator simulator;

	@Override
	public void start(Stage s) throws Exception {
		final Scene scene = new Scene(DeveloperSimulatorController.createRootController(
											getClass().getResource("/main_window.fxml"), getSimulator()).getRootViewNode());
		s.setOnHiding(e -> getSimulator().stopSendingMsgsInAllSensors());
		s.setTitle("Sensor Simulator");
        s.setScene(scene);
        s.setResizable(false);
        s.show();
		
	}

	private SensorsSimulator getSimulator(){
		if(simulator == null)
			simulator = new SensorsSimulator();
		return simulator;
	}
	
	public DeveloperSimulatorGui setSimulator(SensorsSimulator s){
		this.simulator = s;
		return this;
	}
	
    public static void main(final String[] args) {
        launch(args);
    }

}
