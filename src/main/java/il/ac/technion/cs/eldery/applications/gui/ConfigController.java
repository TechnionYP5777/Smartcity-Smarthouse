package il.ac.technion.cs.eldery.applications.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class ConfigController implements Initializable{
    @FXML public Button Apply;
    @FXML public Button Cancel;
    @FXML public Label Head;
    @FXML public HBox ButtonBox;
    @SuppressWarnings("static-access")
    @Override public void initialize(URL location, ResourceBundle resources) {
        
        ButtonBox.setHgrow(Apply, Priority.ALWAYS);
        ButtonBox.setHgrow(Cancel, Priority.ALWAYS);

        int btnCount = ButtonBox.getChildren().size();
        Apply.prefWidthProperty().bind(ButtonBox.widthProperty().divide(btnCount));
        Cancel.prefWidthProperty().bind(ButtonBox.widthProperty().divide(btnCount));
    }
}
