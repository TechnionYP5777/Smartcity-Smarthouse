package il.ac.technion.cs.smarthouse.DeveloperSimulator;

import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.smarthouse.gui_controller.GuiController;
import il.ac.technion.cs.smarthouse.sensors.simulator.SensorBuilder;
import il.ac.technion.cs.smarthouse.sensors.simulator.SensorsSimulator;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.util.Pair;

/**
 * @author Roy Shchory
 * @since Jun 17, 2017
 */
@SuppressWarnings("rawtypes")
public class ConfigurationWindowController extends SimulatorGuiController {

    private ObservableList<Pair<String, Class>> typesList = FXCollections.observableArrayList();
    @FXML private TableView<Pair<String, Class>> fieldsTable;
    @FXML private TableColumn<Pair<String, Class>, String> nameColumn;
    @FXML private TableColumn<Pair<String, Class>, String> typeColumn;
    @FXML private TableColumn<Pair<String, Class>, Boolean> deleteColumn;
    @FXML private Button backButton;
    @FXML private Button addButton;
    @FXML private HBox buttonBox;
    @FXML private TextField addNameField;
    @FXML private TextField alias;
    @FXML private TextField commName;
    @FXML private ComboBox<Types> addTypeField;
    @FXML private Button saveButton;
    @FXML private Button deleteButton;

    /*
     * (non-Javadoc)
     * 
     * @see
     * il.ac.technion.cs.smarthouse.gui_controller.GuiController#initialize(java
     * .lang.Object, il.ac.technion.cs.smarthouse.gui_controller.GuiController,
     * java.net.URL, java.util.ResourceBundle)
     */
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

        addTypeField.setPromptText("Field Type");
        addTypeField.getItems().addAll(Types.values());

        addNameField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> b, Boolean oldValue, Boolean newValue) {
                if (!newValue) // Focusing out
                    addNameField.setStyle("-fx-border-color: " + (addNameField.getText().isEmpty() ? "red" : "green"));
            }
        });
        alias.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> b, Boolean oldValue, Boolean newValue) {
                if (!newValue) // Focusing out
                    alias.setStyle("-fx-border-color: " + (alias.getText().isEmpty() ? "red" : "green"));
            }
        });
        commName.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> b, Boolean oldValue, Boolean newValue) {
                if (!newValue) // Focusing out
                    commName.setStyle("-fx-border-color: " + (commName.getText().isEmpty() ? "red" : "green"));
            }
        });
        final int btnCount = buttonBox.getChildren().size();
        addNameField.prefWidthProperty().bind(buttonBox.widthProperty().divide(btnCount));
        addTypeField.prefWidthProperty().bind(buttonBox.widthProperty().divide(btnCount));
        addButton.prefWidthProperty().bind(buttonBox.widthProperty().divide(btnCount));

        addButton.setOnAction(__1 -> addField());

        saveButton.setOnAction(__1 -> saveNewSensor());

        fieldsTable.setItems(typesList);
    }

    private void addField() {
        String fieldName = addNameField.getText();
        final Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Invalid Field Name");
        if ("".equals(fieldName)) {
            alert.setContentText("Field Name cant be empty.");
            alert.showAndWait();
            return;
        }
        for (Pair<String, Class> x : this.typesList)
            if (x.getKey().equals(fieldName)) {
                alert.setContentText("Field Name allready exists.");
                alert.showAndWait();
                return;
            }
        this.typesList.add(new Pair<String, Class>(addNameField.getText(), addTypeField.getValue().getEClass()));
        addNameField.clear();
    }

    private void saveNewSensor() {
        String commercialName = commName.getText(), aliasName = alias.getText();
        final Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Invalid Input Paramaters");
        if ("".equals(commercialName)) {
            alert.setContentText("Commercial Name cant be empty.");
            alert.showAndWait();
            return;
        }
        if (this.getModel().checkCommNameExists(commercialName)) {
            alert.setContentText(
                            "Commercial Name allready exists! if you want another instance of this sensor use the clone button.");
            alert.showAndWait();
            return;
        }
        if ("".equals(aliasName)) {
            alert.setContentText("Alias cant be empty.");
            alert.showAndWait();
            return;
        }
        if (typesList.isEmpty()) {
            alert.setContentText("Sensor must have at least one path.");
            alert.showAndWait();
            return;
        }
        SensorBuilder b = new SensorBuilder();
        b.setAlias(aliasName);
        b.setCommname(commercialName);
        this.typesList.forEach(x -> b.addInfoSendingPath(x.getKey(), x.getValue()));
        this.getModel().addSensor(b.build());
        alias.clear();
        commName.clear();
        addNameField.clear();
        addTypeField.getSelectionModel().clearSelection();
        addTypeField.setValue(null);
        typesList = FXCollections.observableArrayList();
        fieldsTable.setItems(typesList);
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
