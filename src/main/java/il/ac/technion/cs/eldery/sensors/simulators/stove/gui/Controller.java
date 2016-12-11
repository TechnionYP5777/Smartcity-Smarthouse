package il.ac.technion.cs.eldery.sensors.simulators.stove.gui;

import java.net.*;
import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/** @author Sharon
 * @since 9.12.16 */
public class Controller implements Initializable {
  private boolean on;
  @FXML public Button onOffButton;
  @FXML public Label tempLabel;
  @FXML public Slider tempSlider;

  @Override public void initialize(final URL location, final ResourceBundle __) {
    onOffButton.setOnAction(event -> {
      on = !on;
      onOffButton.setText("Turn " + (on ? "off" : "on"));
      tempLabel.setDisable(!on);
      tempSlider.setDisable(!on);
    });
    tempSlider.valueProperty().addListener((ov, oldVal, newVal) -> {
      tempLabel.setText("Temperature: " + Math.round(newVal.doubleValue()));
    });
  }
}
