package il.ac.technion.cs.smarthouse.simulator.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.smarthouse.simulator.model.Location;
import il.ac.technion.cs.smarthouse.simulator.model.SensorData;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

public class SimulatorController implements Initializable {
    @FXML Tab tab1;
    @FXML Tab tab2;
    @FXML Tab tab3;
    @FXML Tab tab4;
    @FXML AnchorPane sidePane;
    private SensorListViewController tableController;
    private RoomViewController kitchen;
    private RoomViewController livingroom;
    private RoomViewController bathroom;
    private RoomViewController bedroom;
    private ObservableList<SensorData> sensors = FXCollections.observableArrayList();

    public ObservableList<SensorData> getSensors() {
        return sensors;
    }

    public SimulatorController removeSensor(SensorData ¢) {
        sensors.remove(¢);
        switch (¢.getLocation()) {
            case KITCHEN:
                kitchen.getLabelPane().getChildren().remove(¢.getLabel());
                break;
            case BATHROOM:
                bathroom.getLabelPane().getChildren().remove(¢.getLabel());
                break;
            case BEDROOM:
                bedroom.getLabelPane().getChildren().remove(¢.getLabel());
                break;
            case LIVINGROOM:
                livingroom.getLabelPane().getChildren().remove(¢.getLabel());
                break;
            default:
                break;
        }
        return this;
    }

    public void addSensor(SensorData ¢) {
        sensors.add(¢);
        tableController.setData(sensors);
    }

    @Override
    public void initialize(URL location, ResourceBundle __) {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("room_ui.fxml"));

        tab1.setText("Bedroom");
        try {
            tab1.setContent((Node) loader.load());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        bedroom = loader.getController();
        bedroom.setImageUrl("house.jpg").setLocation(Location.BEDROOM).setMainController(this);

        // user tab:
        loader = new FXMLLoader(this.getClass().getResource("room_ui.fxml"));
        tab2.setText("Bathroom");
        try {
            tab2.setContent((Node) loader.load());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        bathroom = loader.getController();
        bathroom.setImageUrl("house.jpg").setLocation(Location.BATHROOM).setMainController(this);

        // user tab:
        loader = new FXMLLoader(this.getClass().getResource("room_ui.fxml"));
        tab3.setText("Kitchen");
        try {
            tab3.setContent((Node) loader.load());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        kitchen = loader.getController();
        kitchen.setImageUrl("house.jpg").setLocation(Location.KITCHEN).setMainController(this);

        // user tab:
        loader = new FXMLLoader(this.getClass().getResource("room_ui.fxml"));
        tab4.setText("Livingroom");
        try {
            tab4.setContent((Node) loader.load());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        livingroom = loader.getController();
        livingroom.setImageUrl("house.jpg").setLocation(Location.LIVINGROOM).setMainController(this);

        final FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("sensors_list_ui.fxml"));
        try {
            JavaFxHelper.placeNodeInPane(fxmlLoader.load(), sidePane);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tableController = fxmlLoader.getController();
        tableController.setMainController(this);
    }
}
