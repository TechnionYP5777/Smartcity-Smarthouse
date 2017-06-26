package il.ac.technion.cs.smarthouse.DeveloperSimulator;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import il.ac.technion.cs.smarthouse.gui_controller.GuiController;
import il.ac.technion.cs.smarthouse.sensors.simulator.SensorsSimulator;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class DeveloperSimulatorController extends GuiController<SensorsSimulator>{
	
	@FXML AnchorPane mainPane;
    @FXML public TextFlow sentConsole, receivedConsole;
	MainSensorListController listController;
	ConfigurationWindowController configController;
	SendMessageController messageController;
	
	@Override
	protected <T extends GuiController<SensorsSimulator>> void initialize(SensorsSimulator model1, T parent1,
			URL location, ResourceBundle b) {
		this.listController = createChildController(getClass().getResource("/sensor_config_list_ui.fxml"));
		this.configController = createChildController(getClass().getResource("/sensor_configuration_ui.fxml"));
		
//		sentConsole.getChildren().add(new Text("Welcome to the Sensor developer simulator\n"));
		
		model1.addSentMsgLogger(x -> Platform.runLater(()->sentConsole.getChildren().add(new Text(x+"\n"))));
		model1.addInstructionReceivedLogger(x -> Platform.runLater(()->receivedConsole.getChildren().add(new Text(x+"\n"))));
		
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
		if(this.getModel().getSensor(this.getModel().getSelectedSensor()).getObservablePaths().isEmpty()){
			final Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Sensor has no fields");
			alert.setContentText("Make sure to configure the sensor before starting to stream.");
			alert.showAndWait();
			return;
		}
			
		this.messageController = createChildController(getClass().getResource("/message_ui.fxml"));
		messageController.loadFields();
		final Stage stage = new Stage();
        stage.setScene(new Scene(messageController.getRootViewNode(),500,200));
        stage.show();
	}

}
