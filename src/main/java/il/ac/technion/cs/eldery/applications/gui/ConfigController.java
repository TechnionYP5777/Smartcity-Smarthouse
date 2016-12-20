package il.ac.technion.cs.eldery.applications.gui;

import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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

    static boolean validateInput(String time, String degrees) {
        return time != null && degrees != null && !"".equals(degrees) && !"".equals(time) && time.chars().allMatch(Character::isDigit)
                && degrees.chars().allMatch(Character::isDigit);
    }

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
                String time = secs.getText();
                String degrees = cels.getText();
                if (validateInput(time, degrees)) {
                    mainController.set_seconds(Integer.parseInt(secs.getText()));
                    mainController.set_temperture(Integer.parseInt(cels.getText()));
                    stage.close();
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Bad Input");
                    alert.setContentText("Make sure to enter only numbers");
                    alert.showAndWait();
                }
            }
        });
    }
}
