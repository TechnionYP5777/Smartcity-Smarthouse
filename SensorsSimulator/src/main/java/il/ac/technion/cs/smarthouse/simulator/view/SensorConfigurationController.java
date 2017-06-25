package il.ac.technion.cs.smarthouse.simulator.view;

import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.smarthouse.DeveloperSimulator.Types;
import il.ac.technion.cs.smarthouse.simulator.model.SensorData;
import il.ac.technion.cs.smarthouse.simulator.model.SensorField;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class SensorConfigurationController implements Initializable {

    @FXML private TableView<SensorField> fieldsTable;
    @FXML private TableColumn<SensorField, String> nameColumn;
    @FXML private TableColumn<SensorField, String> typeColumn;
    @FXML private TableColumn<SensorField, Boolean> deleteColumn;
    @FXML private Label sensorNameLabel;
    @FXML private Button backButton;
    @FXML private Button messageButton;
    @FXML private HBox buttonBox;
    @FXML private TextField addNameField;
    @FXML private ComboBox<Types> addTypeField;
    @FXML private Button saveButton;
    @FXML private Button deleteButton;
    SensorData currentSensor;
    SimulatorController mainController;

    @Override
    public void initialize(final URL location, final ResourceBundle __) {
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        typeColumn.setCellValueFactory(
                        cellData -> new ReadOnlyStringWrapper(cellData.getValue().getType().getFieldDescription()));
        deleteColumn.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue() != null));
        deleteColumn.setCellFactory(p -> {
            final ButtonCell $ = new ButtonCell();
            $.setAlignment(Pos.CENTER);
            return $;
        });
		backButton.setOnAction(__1 -> mainController.loadSensorList());
        HBox.setHgrow(addNameField, Priority.ALWAYS);
        HBox.setHgrow(addTypeField, Priority.ALWAYS);
        HBox.setHgrow(saveButton, Priority.ALWAYS);

        addTypeField.setPromptText("Sensor Type");
        addTypeField.getItems().addAll(Types.values());

        final int btnCount = buttonBox.getChildren().size();
        addNameField.prefWidthProperty().bind(buttonBox.widthProperty().divide(btnCount));
        addTypeField.prefWidthProperty().bind(buttonBox.widthProperty().divide(btnCount));
        saveButton.prefWidthProperty().bind(buttonBox.widthProperty().divide(btnCount));

        saveButton.setOnAction(__1 -> addField());

        deleteButton.setOnAction(__1 -> {
            mainController.removeSensor(currentSensor);
            mainController.loadSensorList();
        });

        messageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent __1) {
                try {
                    final FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/message_ui.fxml"));
                    final Parent root1 = (Parent) fxmlLoader.load();
                    ((MessageViewController) fxmlLoader.getController()).setCurrentSensor(currentSensor);
                    final Stage stage = new Stage();
                    stage.setScene(new Scene(root1));
                    stage.show();
                } catch (final Exception $) {
                    System.out.println($);
                }
            }
        });
    }

    public SensorConfigurationController setSensor(final SensorData ¢) {
        currentSensor = ¢;
        fieldsTable.setItems(currentSensor.getFields());
        sensorNameLabel.setText(currentSensor.getName());
        return this;
    }

    void addField() {
        currentSensor.addField(new SensorField(addNameField.getText(), addTypeField.getValue()));
    }

    public SensorConfigurationController setMainController(final SimulatorController mainController) {
        this.mainController = mainController;
        return this;
    }

    private class ButtonCell extends TableCell<SensorField, Boolean> {
        final Button cellButton = new Button("Delete");

        ButtonCell() {

            // Action when the button is pressed
            cellButton.setOnAction(__ -> {
                final SensorField currentField = ButtonCell.this.getTableView().getItems()
                                .get(ButtonCell.this.getIndex());
                currentSensor.getFields().remove(currentField);
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
