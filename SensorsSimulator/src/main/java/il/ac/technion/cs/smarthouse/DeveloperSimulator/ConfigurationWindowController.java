package il.ac.technion.cs.smarthouse.DeveloperSimulator;

import java.net.URL;
import java.util.ResourceBundle;


import il.ac.technion.cs.smarthouse.gui_controller.GuiController;
import il.ac.technion.cs.smarthouse.sensors.simulator.GenericSensor;
import il.ac.technion.cs.smarthouse.sensors.simulator.SensorBuilder;
import il.ac.technion.cs.smarthouse.sensors.simulator.SensorsSimulator;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.util.Pair;

@SuppressWarnings("rawtypes")
public class ConfigurationWindowController extends SimulatorGuiController {

	private GenericSensor currentSensor;
	private ObservableList<Pair<String, Class>> typesList;
	@FXML
	private TableView<Pair<String, Class>> fieldsTable;
	@FXML
	private TableColumn<Pair<String, Class>, String> nameColumn;
	@FXML
	private TableColumn<Pair<String, Class>, String> typeColumn;
	@FXML
	private TableColumn<Pair<String, Class>, Boolean> deleteColumn;
	@FXML
	private Label sensorNameLabel;
	@FXML
	private Button backButton;
	@FXML
	private Button addButton;
	@FXML
	private HBox buttonBox;
	@FXML
	private TextField addNameField;
	@FXML
	private ComboBox<Types> addTypeField;
	@FXML
	private Button saveButton;
	@FXML
	private Button deleteButton;

	@Override
	protected <T extends GuiController<SensorsSimulator>> void initialize(SensorsSimulator model1, T parent1,
			URL location, ResourceBundle b) {

		backButton.setOnAction(__1 -> ((DeveloperSimulatorController) this.getParentController()).moveToSensorsList());

		nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getKey()));
		typeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getValue().getName()));
		deleteColumn.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue() != null));
		deleteColumn.setCellFactory(p -> {
			final ButtonCell $ = new ButtonCell();
			$.setAlignment(Pos.CENTER);
			return $;
		});

		addTypeField.setPromptText("Sensor Type");
		addTypeField.getItems().addAll(Types.values());

		final int btnCount = buttonBox.getChildren().size();
		addNameField.prefWidthProperty().bind(buttonBox.widthProperty().divide(btnCount));
		addTypeField.prefWidthProperty().bind(buttonBox.widthProperty().divide(btnCount));
		addButton.prefWidthProperty().bind(buttonBox.widthProperty().divide(btnCount));

		addButton.setOnAction(__1 -> addField());

		saveButton.setOnAction(__1 -> saveNewSensor());
	}

	public void loadFields() {
		this.currentSensor = this.getModel().getSensor(getSelectedSensor());
		this.typesList = getObservablePaths(currentSensor);
		fieldsTable.setItems(typesList);
	}

	private void addField() {
		String fieldName =addNameField.getText();
		final Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error Dialog");
		alert.setHeaderText("Invalid Field Name");
		if("".equals(fieldName)){
			alert.setContentText("Field Name cant be empty.");
			alert.showAndWait();
			return;
		}
		boolean exists = false;
		for(Pair<String, Class> x: this.typesList) 
			if(x.getKey().equals(fieldName))
				exists = true;
		if(exists){
			alert.setContentText("Field Name allready exists.");
			alert.showAndWait();
			return;
		}
		this.typesList.add(new Pair<String, Class>(addNameField.getText(), addTypeField.getValue().getEClass()));
		addNameField.clear();
	}

	private void saveNewSensor() {
		SensorBuilder b = new SensorBuilder();
		b.setSensorId(currentSensor.getId());
		b.setAlias(currentSensor.getAlias());
		b.setCommname(currentSensor.getCommname());
		this.typesList.forEach(x -> b.addInfoSendingPath(x.getKey(), x.getValue()));
		this.getModel().updateSensor(getSelectedSensor(), b.build());
		((DeveloperSimulatorController) this.getParentController()).moveToSensorsList();
	}

	private class ButtonCell extends TableCell<Pair<String, Class>, Boolean> {
		final Button cellButton = new Button("Delete");

		ButtonCell() {

			// Action when the button is pressed
			cellButton.setOnAction(__ -> {
				final Pair<String, Class> currentField = ButtonCell.this.getTableView().getItems()
						.get(ButtonCell.this.getIndex());
				typesList.remove(currentField);
			});
		}

		// Display button if the row is not empty
		@Override
		protected void updateItem(final Boolean t, final boolean empty) {
			super.updateItem(t, empty);
			setGraphic(empty ? null : cellButton);
		}
	}

}
