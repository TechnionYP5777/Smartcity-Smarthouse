package il.ac.technion.cs.smarthouse.sensors.shutter.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

/**
 * This class is responsible for the visual part of the shutters sensor.
 * 
 * @author Alex
 * @since 9.05.17
 */
public class shutter_uiController implements Initializable {
    @FXML private Button openButton;
    @FXML private Button closeButton;
    @FXML private Text stateText;

    // Event Listener on Button[#openButton].onAction
    @FXML
    public void openClick(final ActionEvent e) {

        stateText.setText("Shutters Open");
    }

    // Event Listener on Button[#closeButton].onAction
    @FXML
    public void closeClick(final ActionEvent e) {
        stateText.setText("Shutters closed");
    }

    @Override
    public void initialize(final URL location, final ResourceBundle b) {
        // TODO Auto-generated method stub

    }
}
