package il.ac.technion.cs.eldery.applications.gui;

import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class ConfigController implements Initializable {

    @FXML public Button Apply;
    @FXML public Button Cancel;
    @FXML public Label Head;
    @FXML public HBox ButtonBox;
    @FXML public TextField secs;
    @FXML public TextField cels;
    Controller mainController;

    public void subscribe(Controller mainContr) {
        this.mainController = mainContr;
    }

    @SuppressWarnings("static-access") @Override public void initialize(URL location, ResourceBundle __) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("stove_app_ui.fxml"));
        mainController = loader.getController();

        ButtonBox.setHgrow(Apply, Priority.ALWAYS);
        ButtonBox.setHgrow(Cancel, Priority.ALWAYS);

        int btnCount = ButtonBox.getChildren().size();
        Apply.prefWidthProperty().bind(ButtonBox.widthProperty().divide(btnCount));
        Cancel.prefWidthProperty().bind(ButtonBox.widthProperty().divide(btnCount));

        Cancel.setOnAction(new EventHandler<ActionEvent>() {

            @SuppressWarnings("hiding") @Override public void handle(ActionEvent __) {
                Stage stage = (Stage) Cancel.getScene().getWindow();
                // do what you have to do
                stage.close();
            }
        });
        Apply.setOnAction(new EventHandler<ActionEvent>() {
            @SuppressWarnings("hiding") @Override public void handle(ActionEvent __) {
                Stage stage = (Stage) Apply.getScene().getWindow();
                mainController.set_seconds(Integer.parseInt(secs.getText()));
                mainController.set_temperture(Integer.parseInt(cels.getText()));
                stage.close();
            }
        });
    }
}
