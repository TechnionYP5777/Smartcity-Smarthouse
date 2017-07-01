package il.ac.technion.cs.smarthouse.DeveloperSimulator;

import java.net.URL;
import java.util.Comparator;
import java.util.Optional;
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

/**
 * @author Roy Shchory
 * @since Jun 17, 2017
 */
public class MainSensorListController extends SimulatorGuiController {

    ObservableList<Pair<String, String>> sensors = FXCollections.observableArrayList();
    @FXML private TableView<Pair<String, String>> sensorTable;
    @FXML private TableColumn<Pair<String, String>, String> nameColumn;
    @FXML private TableColumn<Pair<String, String>, Boolean> configColumn;
    @FXML private TableColumn<Pair<String, String>, Boolean> messageColumn;
    @FXML private TableColumn<Pair<String, String>, Boolean> deleteColumn;
    @FXML private Button addButton;

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
        Consumer<GenericSensor> addConsumer = x -> {
            this.sensors.add(new Pair<String, String>(model1.getSensorId(x),
                            x.getCommname() + "(" + x.getAlias() + ")"));
            sensors.sort(new Comparator<Pair<String, String>>() {

                @Override
                public int compare(Pair<String, String> o1, Pair<String, String> o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }
            });
        };
        model1.addListenerWhen(SensorsSimulator.Action.ADD, addConsumer);
        sensorTable.setItems(sensors);
        nameColumn.prefWidthProperty().bind(sensorTable.widthProperty().multiply(0.7));
        nameColumn.setResizable(false);
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getValue()));
        configColumn.prefWidthProperty().bind(sensorTable.widthProperty().multiply(0.1));
        configColumn.setResizable(false);
        configColumn.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue() != null));
        configColumn.setCellFactory(p -> {
            final ButtonCell $ = new ButtonCell();
            $.setAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent e) {
                    final TextInputDialog dialog = new TextInputDialog();
                    dialog.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Homeicon.png"))));
                    dialog.setTitle("Sensor simulator");
                    dialog.setHeaderText("Clone Sensor");
                    dialog.setContentText("Please enter the cloned sensor alias:");
                    final Optional<String> result = dialog.showAndWait();
                    if (!result.isPresent())
                        return;
                    final String name = result.get();
                    MainSensorListController.this.getModel()
                                    .cloneSensor($.getTableView().getItems().get($.getIndex()).getKey(), name);
                }
            });

            $.setImage(new ImageView(new Image(getClass().getResourceAsStream("/Copy.png"))));

            $.setAlignment(Pos.CENTER);
            return $;
        });
        messageColumn.prefWidthProperty().bind(sensorTable.widthProperty().multiply(0.1));
        messageColumn.setResizable(false);
        messageColumn.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue() != null));
        messageColumn.setCellFactory(p -> {
            final ButtonCell $ = new ButtonCell();
            $.setAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent e) {
                    setSelectedSensor($.getTableView().getItems().get($.getIndex()).getKey());
                    ((DeveloperSimulatorController) MainSensorListController.this.getParentController())
                                    .openMessageWindow();
                }
            });

            $.setImage(new ImageView(new Image(getClass().getResourceAsStream("/Message.png"))));

            $.setAlignment(Pos.CENTER);
            return $;
        });
        deleteColumn.prefWidthProperty().bind(sensorTable.widthProperty().multiply(0.1));
        deleteColumn.setResizable(false);
        deleteColumn.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue() != null));
        deleteColumn.setCellFactory(p -> {
            final ButtonCell $ = new ButtonCell();
            $.setAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent e) {
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
                ((DeveloperSimulatorController) MainSensorListController.this.getParentController())
                                .moveToConfiguration();
            }
        });
    }

    private class ButtonCell extends TableCell<Pair<String, String>, Boolean> {
        final Button cellButton;

        ButtonCell() {
            this.cellButton = new Button();
            // Action when the button is pressed
        }

        public void setAction(EventHandler<ActionEvent> e) {
            this.cellButton.setOnAction(e);
        }

        public void setImage(ImageView v) {
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
