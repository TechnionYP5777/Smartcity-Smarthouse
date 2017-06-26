package il.ac.technion.cs.smarthouse.DeveloperSimulator;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import il.ac.technion.cs.smarthouse.gui_controller.GuiController;
import il.ac.technion.cs.smarthouse.sensors.simulator.GenericSensor;
import il.ac.technion.cs.smarthouse.sensors.simulator.SensorsSimulator;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Pair;

@SuppressWarnings("rawtypes")
public class SendMessageController extends SimulatorGuiController {

	final String FROM="From (inclusive)", TO="To (exclusive)";
	@FXML
	VBox mainPane;
	private Map<String, List> ranges;
	private List<Consumer<Map<String, List>>> consumers = new ArrayList<>();
	private boolean encounterdIssue;
	private GenericSensor currentSensor;
	private ObservableList<Pair<String, Class>> typesList;
	private List<String> issues;
	private String errorMessage;
	
	@Override
	protected <T extends GuiController<SensorsSimulator>> void initialize(SensorsSimulator model1, T parent1,
			URL location, ResourceBundle b) {
		mainPane.setSpacing(5);
		mainPane.setPadding(new Insets(10, 0, 0, 10));
	}

	public void loadFields() {
		this.currentSensor = this.getModel().getSensor(getSelectedSensor());
		Label l = new Label(currentSensor.getAlias()+" Fields:");
		l.setFont(new Font("Arial", 20));
		mainPane.getChildren().add(l);
		this.typesList = getObservablePaths(currentSensor);
		this.typesList.forEach(p -> {
			Class c = p.getValue();
			if (c.equals(Double.class))
				addDoubleField(p.getKey());
			if (c.equals(Integer.class))
				addIntegerField(p.getKey());
			if (c.equals(Boolean.class))
				addBoolField(p.getKey());
			if (c.equals(String.class))
				addStringField(p.getKey());
		});
		Button saveButton = new Button("Send");
		saveButton.setOnAction(__1 -> {
			ranges = new HashMap<>();
			issues = new ArrayList<>();
			consumers.forEach(c -> c.accept(ranges));
			if(encounterdIssue){
				errorMessage="";
				issues.forEach(s -> errorMessage+=s+"\n");
				encounterdIssue = false;
				final Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText("Invalid Field Ranges!");
				alert.setContentText(errorMessage);
				alert.showAndWait();
				return;
			}
			currentSensor.streamMessages(ranges);
			Stage stage = (Stage) saveButton.getScene().getWindow();
		    stage.close();
		});
		mainPane.getChildren().add(saveButton);
	}

	private void addBoolField(String fieldName) {
		Label label = new Label(fieldName + ":");
		final ToggleGroup group = new ToggleGroup();

		RadioButton rb1 = new RadioButton("Random");
		rb1.setToggleGroup(group);
		rb1.setSelected(true);

		RadioButton rb2 = new RadioButton("True");
		rb2.setToggleGroup(group);

		RadioButton rb3 = new RadioButton("False");
		rb3.setToggleGroup(group);

		HBox hb = new HBox(label, rb1, rb2, rb3);
		hb.setSpacing(3);

		VBox vbox = new VBox(label, hb);
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 0, 0, 10));

		mainPane.getChildren().add(vbox);

		consumers.add(l -> {
			List<Boolean> input = new ArrayList<>();
			RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();
			switch (selectedRadioButton.getText()) {
			case "Random":
				input.add(true);
				input.add(false);
				break;
			case "True":
				input.add(true);
				break;
			case "False":
				input.add(false);
				break;
			}
			l.put(fieldName, input);
		});
	}
	
	static boolean validateInteger(String s){
		try {
			Integer.parseInt(s);
		} catch(Exception e){
			return false;
		}
		return true;
	}
	
	static boolean validateDouble(String s){
		try {
			Double.parseDouble(s);
		} catch(Exception e){
			return false;
		}
		return true;
	}

	private void addIntegerField(String fieldName) {
		Label label = new Label(fieldName + ":");
		TextField lowerRange = new TextField(FROM), topRange = new TextField(TO);
		lowerRange.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> b, Boolean oldValue, Boolean newValue) {
				if (!newValue) // Focusing out
					lowerRange
							.setStyle("-fx-border-color: " + (!validateInteger(lowerRange.getText()) ? "red" : "green"));
			}
		});
		topRange.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> b, Boolean oldValue, Boolean newValue) {
				if (!newValue) // Focusing out
					topRange
							.setStyle("-fx-border-color: " + (!validateInteger(topRange.getText()) ? "red" : "green"));
			}
		});
		HBox hb = new HBox(label, lowerRange, topRange);
		hb.setSpacing(3);

		VBox vbox = new VBox(label, hb);
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 0, 0, 10));

		mainPane.getChildren().add(vbox);

		consumers.add(l -> {
			List<Integer> input = new ArrayList<>();
			try {
				int lower = Integer.parseInt(lowerRange.getText()), top = Integer.parseInt(topRange.getText());
				if (lower > top) {
					this.encounterdIssue = true;
					issues.add("in " + fieldName + " From must be less or equal to To");
				} else {
					input.add(lower);
					input.add(top);
					l.put(fieldName, input);
				}
			} catch (final Exception e) {
				this.encounterdIssue = true;
				issues.add("in "+fieldName+" Values must be Integers!");
			}
		});
	}

	private void addDoubleField(String fieldName) {
		Label label = new Label(fieldName + ":");
		TextField lowerRange = new TextField(FROM), topRange = new TextField(TO);
		HBox hb = new HBox(label, lowerRange, topRange);
		hb.setSpacing(3);
		
		lowerRange.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> b, Boolean oldValue, Boolean newValue) {
				if (!newValue) // Focusing out
					lowerRange
							.setStyle("-fx-border-color: " + (!validateInteger(lowerRange.getText()) ? "red" : "green"));
			}
		});
		topRange.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> b, Boolean oldValue, Boolean newValue) {
				if (!newValue) // Focusing out
					topRange
							.setStyle("-fx-border-color: " + (!validateInteger(topRange.getText()) ? "red" : "green"));
			}
		});

		VBox vbox = new VBox(label, hb);
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 0, 0, 10));
		
		mainPane.getChildren().add(vbox);

		consumers.add(l -> {
			List<Double> input = new ArrayList<>();
			try {
				double lower = Double.parseDouble(lowerRange.getText()), top = Double.parseDouble(topRange.getText());
				if (lower > top) {
					this.encounterdIssue = true;
					issues.add("in " + fieldName + " From must be less or equal to To");
				} else {
					input.add(lower);
					input.add(top);
					l.put(fieldName, input);
				}
			} catch (final Exception e) {
				this.encounterdIssue = true;
				issues.add("in "+fieldName+" Values must be Double!");
			}
		});
	}

	private void addStringField(String fieldName) {
		ObservableList<String> s = FXCollections.observableArrayList();
		Label label = new Label(fieldName + ":");
		TableView<String> table = new TableView<>(s);
		TableColumn<String, String> valueCol = new TableColumn<String, String>("Value");
		valueCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));
		TableColumn<String, Boolean> deleteCol = new TableColumn<String, Boolean>();
		deleteCol.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue() != null));
		deleteCol.setCellFactory(p -> {
			final ButtonCell $ = new ButtonCell();
			$.setAction(__1 -> s.remove($.getTableView().getItems().get($.getIndex())));
			$.setAlignment(Pos.CENTER);
			return $;
		});
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setPrefSize(100, 100);
		table.getColumns().setAll(Arrays.asList(valueCol, deleteCol));

		Group group = new Group(table);
		VBox.setVgrow(group, Priority.NEVER);

		TextField addValue = new TextField();
		Button addButton = new Button("Add");
		addButton.setOnAction(__1 -> {
			s.add(addValue.getText());
			addValue.clear();
		});
		HBox hb = new HBox(addValue, addButton);
		hb.setSpacing(3);

		VBox vbox = new VBox(label, table, hb);
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 0, 0, 10));

		mainPane.getChildren().add(vbox);

		consumers.add(l -> {
			if (!s.isEmpty())
				l.put(fieldName, s);
			else {
				this.encounterdIssue = true;
				issues.add("in "+fieldName+" must contain at least one String");
			}
		});
	}

	private class ButtonCell extends TableCell<String, Boolean> {
		final Button cellButton = new Button("Delete");

		ButtonCell() {

		}

		public void setAction(EventHandler<ActionEvent> e) {
			this.cellButton.setOnAction(e);
		}

		// Display button if the row is not empty
		@Override
		protected void updateItem(final Boolean t, final boolean empty) {
			super.updateItem(t, empty);
			setGraphic(empty ? null : cellButton);
		}
	}

}
