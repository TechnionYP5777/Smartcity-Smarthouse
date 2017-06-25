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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Pair;

@SuppressWarnings("rawtypes")
public class SendMessageController extends GuiController<SensorsSimulator> {

	@FXML
	VBox mainPane;
	private Map<String, List> ranges;
	private List<Consumer<Map<String, List>>> consumers = new ArrayList<>();
	private boolean encounterdIssue;
	private GenericSensor currentSensor;
	private ObservableList<Pair<String, Class>> typesList;

	@Override
	protected <T extends GuiController<SensorsSimulator>> void initialize(SensorsSimulator model1, T parent1,
			URL location, ResourceBundle b) {
		mainPane.setSpacing(5);
		mainPane.setPadding(new Insets(10, 0, 0, 10));
	}

	public void loadFields() {
		this.currentSensor = this.getModel().getSensor(this.getModel().getSelectedSensor());
		Label l = new Label(currentSensor.getAlias()+" Fields:");
		l.setFont(new Font("Arial", 20));
		mainPane.getChildren().add(l);
		this.typesList = currentSensor.getObservablePaths();
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
			consumers.forEach(c -> c.accept(ranges));
			if(encounterdIssue){
				//TODO allert issue in inputes
				encounterdIssue = false;
				return;
			}
			currentSensor.streamMessages(ranges);
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

	private void addIntegerField(String fieldName) {
		Label label = new Label(fieldName + ":");
		TextField lowerRange = new TextField("From"), topRange = new TextField("To");
		HBox hb = new HBox(label, lowerRange, topRange);
		hb.setSpacing(3);

		VBox vbox = new VBox(label, hb);
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 0, 0, 10));

		mainPane.getChildren().add(vbox);

		consumers.add(l -> {
			List<Integer> input = new ArrayList<>();
			try {
				input.add(Integer.parseInt(lowerRange.getText()));
				input.add(Integer.parseInt(lowerRange.getText()));
				l.put(fieldName, input);
			} catch (final Exception e) {
				this.encounterdIssue = true;
			}
		});
	}

	private void addDoubleField(String fieldName) {
		Label label = new Label(fieldName + ":");
		TextField lowerRange = new TextField("From"), topRange = new TextField("To");
		HBox hb = new HBox(label, lowerRange, topRange);
		hb.setSpacing(3);

		VBox vbox = new VBox(label, hb);
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 0, 0, 10));
		
		mainPane.getChildren().add(vbox);

		consumers.add(l -> {
			List<Double> input = new ArrayList<>();
			try {
				input.add(Double.parseDouble(lowerRange.getText()));
				input.add(Double.parseDouble(lowerRange.getText()));
				l.put(fieldName, input);
			} catch (final Exception e) {
				this.encounterdIssue = true;
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
			else
				this.encounterdIssue = true;
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
