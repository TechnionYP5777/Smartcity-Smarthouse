package il.ac.technion.cs.smarthouse.simulator.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import il.ac.technion.cs.smarthouse.simulator.model.Location;
import il.ac.technion.cs.smarthouse.simulator.model.SensorData;
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

    public RoomViewController setMainController(final SimulatorController mainController) {
        this.mainController = mainController;
        return this;
    }

    private void addSensor(final double x, final double y) {
        final TextInputDialog dialog = new TextInputDialog("sensor name");
        dialog.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("Homeicon.png"))));
        dialog.setTitle("Create Sensor");
        dialog.setHeaderText("Config your simulator");
        dialog.setContentText("Please enter sensor name:");
        final Optional<String> result = dialog.showAndWait();
        if (!result.isPresent())
            return;
        final String name = result.get();
        final SensorLabel label = new SensorLabel(x, y, name);
        final SensorData sensor = new SensorData(name, label, location);
        mainController.addSensor(sensor);
        RoomViewController.this.labels.add(label);
        pane.getChildren().add(label);
    }

    public Pane getLabelPane() {
        return pane;
    }

    @Override
    public void initialize(final URL location1, final ResourceBundle __) {
        pane = new Pane();
        pane.setOnMouseClicked(event -> {
            if (event.getTarget() == pane && inEditMode)
                addSensor(event.getX(), event.getY());
        });

        editButton.setOnAction(__1 -> {
            final String buttonText = inEditMode ? "Edit" : "Save";
            editButton.setText(buttonText);
            labels.forEach(c -> c.switchMovableState());
            inEditMode = !inEditMode;
        });

    }

    public RoomViewController setImageUrl(final String location) {
        image_url = location;
        imagePane.getChildren().addAll(
                        new StackPane(new ImageView(new Image(getClass().getResourceAsStream(image_url))), pane));
        return this;
    }

    public RoomViewController setLocation(final Location ¢) {
        location = ¢;
        return this;
    }

    public Location getLocation() {
        return location;
    }

}
