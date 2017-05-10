package il.ac.technion.cs.smarthouse.applications.stove;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    StoveAppController mainController;

    static boolean validateInput(final String time, final String degrees) {
        return time != null && degrees != null && !"".equals(degrees) && !"".equals(time) && time.chars().allMatch(Character::isDigit)
                && degrees.chars().allMatch(Character::isDigit);
    }

    public void subscribe(final StoveAppController mainContr) {
        mainController = mainContr;
    }

    @Override public void initialize(final URL location, final ResourceBundle __) {

        mainController = new FXMLLoader(getClass().getResource("stove_app_ui.fxml")).getController();

        HBox.setHgrow(Apply, Priority.ALWAYS);
        HBox.setHgrow(Cancel, Priority.ALWAYS);

        final int btnCount = ButtonBox.getChildren().size();
        Apply.prefWidthProperty().bind(ButtonBox.widthProperty().divide(btnCount));
        Cancel.prefWidthProperty().bind(ButtonBox.widthProperty().divide(btnCount));

        Cancel.setOnAction(new EventHandler<ActionEvent>() {

            @Override public void handle(final ActionEvent __1) {
                // do what you have to do
                ((Stage) Cancel.getScene().getWindow()).close();
            }
        });
        Apply.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(final ActionEvent __1) {
                final Stage stage = (Stage) Apply.getScene().getWindow();
                final String time = secs.getText(), degrees = cels.getText();
                if (validateInput(time, degrees)) {
                    mainController.set_alert_seconds(Integer.parseInt(secs.getText()));
                    mainController.set_alert_temperature(Integer.parseInt(cels.getText()));
                    stage.close();
                } else {
                    final Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Bad Input");
                    alert.setContentText("Make sure to enter only numbers");
                    alert.showAndWait();
                }
            }
        });
    }
}
