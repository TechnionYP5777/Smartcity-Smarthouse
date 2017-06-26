package il.ac.technion.cs.smarthouse.DeveloperSimulator;

import java.net.URL;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import il.ac.technion.cs.smarthouse.gui_controller.GuiController;
import il.ac.technion.cs.smarthouse.sensors.simulator.GenericSensor;
import il.ac.technion.cs.smarthouse.sensors.simulator.SensorBuilder;
import il.ac.technion.cs.smarthouse.sensors.simulator.SensorsSimulator;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MainSensorListController extends  SimulatorGuiController{
	
	ObservableList<Pair<String, String>> sensors  = FXCollections.observableArrayList();
	@FXML private TableView<Pair<String, String>> sensorTable;
    @FXML private TableColumn<Pair<String, String>, String> nameColumn;
    @FXML private TableColumn<Pair<String, String>, Boolean> configColumn;
    @FXML private TableColumn<Pair<String, String>, Boolean> messageColumn;
    @FXML private TableColumn<Pair<String, String>, Boolean> deleteColumn;
	@FXML private Button addButton;
	
	@Override
	protected <T extends GuiController<SensorsSimulator>> void initialize(SensorsSimulator model1, T parent1,
			URL location, ResourceBundle b) {
		Consumer<GenericSensor> addConsumer = x ->{ this.sensors.add(new Pair<String, String>(model1.getSensorId(x), x.getAlias()));
		sensors.sort(new Comparator<Pair<String, String>>() {

			@Override
			public int compare(Pair<String, String> o1, Pair<String, String> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});};
		model1.addListenerWhen(SensorsSimulator.Action.ADD, addConsumer);
		sensorTable.setItems(sensors);
		nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getValue()));
        configColumn.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue() != null));
        configColumn.setCellFactory(p -> {
            final ButtonCell $ = new ButtonCell();
            $.setAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent e) {
					setSelectedSensor($.getTableView().getItems().get($.getIndex()).getKey());
					((DeveloperSimulatorController)MainSensorListController.this.getParentController()).moveToConfiguration();
				}
			});
            
            $.setImage(new ImageView(new Image(getClass().getResourceAsStream("/Settings.png"))));
            
            $.setAlignment(Pos.CENTER);
            return $;
        });
        messageColumn.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue() != null));
        messageColumn.setCellFactory(p -> {
            final ButtonCell $ = new ButtonCell();
            $.setAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent e) {
					setSelectedSensor($.getTableView().getItems().get($.getIndex()).getKey());
					((DeveloperSimulatorController)MainSensorListController.this.getParentController()).openMessageWindow();
				}
			});
            
            $.setImage(new ImageView(new Image(getClass().getResourceAsStream("/Message.png"))));
            
            $.setAlignment(Pos.CENTER);
            return $;
        });
        deleteColumn.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue() != null));
        deleteColumn.setCellFactory(p -> {
            final ButtonCell $ = new ButtonCell();
            $.setAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent e) {
					// TODO add are you sure alert
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Confirmation Dialog");
					alert.setHeaderText("Are you sure?");
					alert.setContentText("The sensor will be deleted forever!");

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() != ButtonType.OK)
						return;
					final Pair<String, String> currentSensor = $.getTableView().getItems().get($.getIndex());
					model1.removeSensor(currentSensor.getKey());
					MainSensorListController.this.sensors.remove(currentSensor);
				}
			});
            
            $.setImage(new ImageView(new Image(getClass().getResourceAsStream("/Delete.png"))));
            
            $.setAlignment(Pos.CENTER);
            return $;
        });
        addButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				final TextInputDialog dialog = new TextInputDialog();
		        dialog.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Homeicon.png"))));
		        dialog.setTitle("Create Sensor");
		        dialog.setHeaderText("Config your simulator");
		        dialog.setContentText("Please enter sensor name:");
		        final Optional<String> result = dialog.showAndWait();
		        if (!result.isPresent())
		            return;
		        final String name = result.get();
				SensorBuilder b = new SensorBuilder();
				b.setAlias(name);
				b.setCommname(name);
				model1.addSensor(b.build());
			}
		});
	}
    
    private class ButtonCell extends TableCell<Pair<String, String>, Boolean> {
        final Button cellButton; 

        ButtonCell() {
        	this.cellButton = new Button();
            // Action when the button is pressed
        }
        
        public void setAction(EventHandler<ActionEvent> e){
        	this.cellButton.setOnAction(e);
        }
        
        public void setImage(ImageView v){
        	this.cellButton.setGraphic(v);
        }

        // Display button if the row is not empty
        @Override
        protected void updateItem(final Boolean t, final boolean empty) {
            super.updateItem(t, empty);
            setGraphic(empty ? null : cellButton);
        }
    }

}
