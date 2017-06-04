package il.ac.technion.cs.smarthouse.simulator.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.smarthouse.simulator.model.SensorData;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class SensorListViewController implements Initializable {

    @FXML private TableView<SensorData> sensorTable;
    @FXML private TableColumn<SensorData, String> NameColumn;
    @FXML private TableColumn<SensorData, String> locationColumn;
    private SimulatorController mainController;

    @Override
    public void initialize(final URL location, final ResourceBundle __) {
        // TODO Auto-generated method stub
        NameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        locationColumn.setCellValueFactory(
                        cellData -> new ReadOnlyStringWrapper(cellData.getValue().getLocation().getFieldDescription()));
        sensorTable.setEditable(false);
        sensorTable.getSelectionModel().selectedItemProperty()
                        .addListener((observable, oldValue, newValue) -> enterSensorConfig(newValue));
    }

    public SensorListViewController setMainController(final SimulatorController mainController) {
        this.mainController = mainController;
        return this;
    }

    public SensorListViewController setData(final ObservableList<SensorData> ¢) {
        sensorTable.setItems(¢);
        return this;
    }

    private void enterSensorConfig(final SensorData d) {
        if (d == null)
            return;
        final FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/sensor_configuration_ui.fxml"));
        try {
            JavaFxHelper.placeNodeInPane(fxmlLoader.load(), mainController.sidePane);
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final SensorConfigurationController controller = fxmlLoader.getController();
        controller.setSensor(d).setMainController(mainController);
    }

}
