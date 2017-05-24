package il.ac.technion.cs.smarthouse.simulator.view;

import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.smarthouse.simulator.model.SensorData;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class SensorListViewController implements Initializable {

    @FXML private TableView<SensorData> sensorTable;
    @FXML private TableColumn<SensorData, String> NameColumn;
    @FXML private TableColumn<SensorData, String> locationColumn;
    private SimulatorController mainController;

    @Override
    public void initialize(URL location, ResourceBundle __) {
        // TODO Auto-generated method stub
        NameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        locationColumn.setCellValueFactory(
                        cellData -> new ReadOnlyStringWrapper(cellData.getValue().getLocation().getFieldDescription()));
        sensorTable.setEditable(false);
    }

    public SensorListViewController setMainController(SimulatorController mainController) {
        this.mainController = mainController;
        return this;
    }

    public SensorListViewController setData(ObservableList<SensorData> ¢) {
        this.sensorTable.setItems(¢);
        return this;
    }

}
