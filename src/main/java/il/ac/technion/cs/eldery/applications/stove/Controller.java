package il.ac.technion.cs.eldery.applications.stove;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/** @author Roy
 * @since 19.12.16 */
public class Controller implements Initializable {
    @FXML public Label timeLabel;
    @FXML public Label tempLabel;
    @FXML public Button stoveConfigButton;
    Timeline timeline;
    DoubleProperty timeSeconds = new SimpleDoubleProperty();
    Duration time = Duration.ZERO;
    int degrees = 150;
    int seconds = 30;

    public int get_alert_temperture() {
        return degrees;
    }

    public int get_alert_seconds() {
        return seconds;
    }

    public void set_alert_temperture(final int degrees) {
        this.degrees = degrees;
    }

    public void set_alert_seconds(final int seconds) {
        this.seconds = seconds;
    }
    
    private static void alert(String messege){
        final Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("ALERT");
        alert.setHeaderText("The stove is out of line");
        alert.setContentText(messege);
        alert.showAndWait();
    }
    
    public void turnOn(){
        timeline = new Timeline(new KeyFrame(Duration.millis(100), ¢ -> {
            time = time.add(((KeyFrame) ¢.getSource()).getTime());
            timeSeconds.set(time.toSeconds());
            timeLabel.setText("The Stove is Running for: " + timeSeconds.get() + " (Secs)");
            if(timeSeconds.get() > Controller.this.get_alert_seconds()) alert("Stove is runnig too long");
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    
    public void turnOf(){
        timeline.stop();
        time = Duration.ZERO;
        timeLabel.setTextFill(Color.BLACK);
        timeSeconds.set(time.toSeconds());
        timeLabel.setText("The Stove is: Off");
    }
    
    public void updateTemperture(int temp){
        tempLabel.setText("The Stove Temperture is: "+temp);
        if(temp>this.degrees) alert("Stove is too hot");
    }

    @Override public void initialize(final URL location, final ResourceBundle __) {

        stoveConfigButton.setOnAction(new EventHandler<ActionEvent>() {
            @SuppressWarnings({ "hiding" }) @Override public void handle(final ActionEvent __) {
                try {
                    final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("stove_app_config.fxml"));
                    final Parent root1 = (Parent) fxmlLoader.load();
                    final Stage stage = new Stage();
                    stage.setScene(new Scene(root1));
                    stage.show();
                    final ConfigController configController = fxmlLoader.getController();
                    configController.subscribe(Controller.this);
                } catch (final Exception ¢) {
                    ¢.printStackTrace();
                }
            }
        });
    }
}
