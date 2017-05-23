package il.ac.technion.cs.smarthouse.simulator.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import il.ac.technion.cs.smarthouse.simulator.model.Location;
import il.ac.technion.cs.smarthouse.simulator.model.SensorData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class RoomViewController implements Initializable {

    @FXML Button editButton;
    @FXML private AnchorPane imagePane;
    private String image_url;
    private Location location;
    List<SensorLabel> labels = new ArrayList<>();
    boolean inEditMode;
    private Pane pane;
    private SimulatorController mainController;

    public RoomViewController setMainController(SimulatorController mainController) {
        this.mainController = mainController;
        return this;
    }

    private void addSensor(double x, double y) {
        TextInputDialog dialog = new TextInputDialog("sensor name");
        dialog.setTitle("Create Sensor");
        dialog.setHeaderText("Config your simulator");
        dialog.setContentText("Please enter sensor name:");

        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent())
            return;
        String name = result.get();
        SensorLabel label = new SensorLabel(x, y, name);
        SensorData sensor = new SensorData(name, label, this.location);
        mainController.addSensor(sensor);
        RoomViewController.this.labels.add(label);
        pane.getChildren().add(label);
    }

    public Pane getLabelPane() {
        return pane;
    }

    @Override public void initialize(URL location1, ResourceBundle __) {
        pane = new Pane();
        pane.setOnMouseClicked(event -> {
            if (event.getTarget() == pane && inEditMode)
                addSensor(event.getX(), event.getY());
        });

        editButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override public void handle(ActionEvent __1) {
                String buttonText = RoomViewController.this.inEditMode ? "Edit" : "Save";
                editButton.setText(buttonText);
                for (SensorLabel ¢ : RoomViewController.this.labels)
                    ¢.switchMovableState();
                RoomViewController.this.inEditMode = !RoomViewController.this.inEditMode;
            }
        });

    }

    public RoomViewController setImageUrl(String location) {
        this.image_url = location;
        imagePane.getChildren().addAll((new StackPane(new ImageView(new Image(getClass().getResourceAsStream(image_url))), pane)));
        return this;
    }

    public RoomViewController setLocation(Location ¢) {
        this.location = ¢;
        return this;
    }

    public Location getLocation() {
        return location;
    }

}
