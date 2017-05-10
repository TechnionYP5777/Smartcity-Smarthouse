package il.ac.technion.cs.smarthouse.applications.sos;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

public class SosController implements Initializable {

    @FXML public Label stateLabel;
    @FXML public Button killerButton;
    @FXML public AnchorPane mainPane;

    boolean inAlertMode;

    @Override public void initialize(final URL arg0, final ResourceBundle arg1) {
        stateLabel.setFont(new Font("Arial", 20));
        mainPane.setStyle("-fx-background-color: green");
    }

    public void sosBtnPressed() {
        stateLabel.setText("Elderly is in Danger!!!");
        killerButton.setVisible(true);
        mainPane.setStyle("-fx-background-color: red");
        inAlertMode = true;
    }

    public void sosBtnUnpress() {
        stateLabel.setText("Elderly is OK");
        killerButton.setVisible(false);
        mainPane.setStyle("-fx-background-color: green");
        inAlertMode = false;
    }

    public Button getBtn() {
        return killerButton;
    }

}
