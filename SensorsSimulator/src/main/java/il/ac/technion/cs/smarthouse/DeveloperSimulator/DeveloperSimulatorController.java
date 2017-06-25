package il.ac.technion.cs.smarthouse.DeveloperSimulator;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import il.ac.technion.cs.smarthouse.gui_controller.GuiController;
import il.ac.technion.cs.smarthouse.sensors.simulator.SensorsSimulator;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DeveloperSimulatorController extends GuiController<SensorsSimulator>{
	
	@FXML AnchorPane mainPane;
	MainSensroListController listController;
	ConfigurationWindowController configController;
	SendMessageController messageController;
	@Override
	protected <T extends GuiController<SensorsSimulator>> void initialize(SensorsSimulator model1, T parent1,
			URL location, ResourceBundle b) {
		this.listController = createChildController(getClass().getResource("/sensor_config_list_ui.fxml"));
		this.configController = createChildController(getClass().getResource("/sensor_configuration_ui.fxml"));
		Consumer<String> s = x -> System.out.println(x);
		model1.addSentMsgLogger(s);
		JavaFxHelper.placeNodeInPane(listController.getRootViewNode(),mainPane);
	}
	
	public void moveToConfiguration(){
		configController.loadFields();
		JavaFxHelper.placeNodeInPane(configController.getRootViewNode(),mainPane);
	}
	
	public void moveToSensorsList(){
		JavaFxHelper.placeNodeInPane(listController.getRootViewNode(),mainPane);
	}
	
	public void openMessageWindow(){
		this.messageController = createChildController(getClass().getResource("/message_ui.fxml"));
		messageController.loadFields();
		final Stage stage = new Stage();
        stage.setScene(new Scene(messageController.getRootViewNode(),500,200));
        stage.show();
	}

}
