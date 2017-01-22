package il.ac.technion.cs.eldery.applications.stove;

import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.eldery.system.EmergencyLevel;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/** @author Roy
 * @since 19.12.16 */
public class StoveAppController implements Initializable {
    @FXML public Label timeLabel;
    @FXML public Label tempLabel;
    @FXML public Button stoveConfigButton;
    Timeline timeline;
    DoubleProperty timeSeconds = new SimpleDoubleProperty();
    Duration time = Duration.ZERO;
    int degrees = 120;
    int seconds = 30;
    boolean alertTemp;
    boolean alertTime;
    boolean isOn;
    StoveModuleGui instance;
    
    public void setInstance(StoveModuleGui instance){
        this.instance=instance;
    }

    public int get_alert_temperature() {
        return degrees;
    }

    public int get_alert_seconds() {
        return seconds;
    }

    public void set_alert_temperature(final int degrees) {
        this.degrees = degrees;
    }

    public void set_alert_seconds(final int seconds) {
        this.seconds = seconds;
    }
    
    private void alert(String messege){
        this.instance.sendAlert(messege, EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
    }
    
    public void turnOn(){
        if (isOn)
            return;
        isOn = !isOn;
        timeline = new Timeline(new KeyFrame(Duration.millis(100), ¢ -> {
            time = time.add(((KeyFrame) ¢.getSource()).getTime());
            timeSeconds.set(time.toSeconds());
            timeLabel.setText("The Stove is Running for: " + timeSeconds.get() + " (Secs)");
            if (timeSeconds.get() <= StoveAppController.this.get_alert_seconds()) alertTime = false;
            if (timeSeconds.get() > StoveAppController.this.get_alert_seconds() && !alertTime){
                alert("Stove is runnig too long");
                alertTime = true;
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    
    public void turnOf(){
        if (!isOn)
            return;
        isOn = !isOn;
        timeline.stop();
        time = Duration.ZERO;
        timeLabel.setTextFill(Color.BLACK);
        timeSeconds.set(time.toSeconds());
        timeLabel.setText("The Stove is: Off");
    }
    
    public void updateTemperture(int temp){
        tempLabel.setText("The Stove Temperture is: "+temp);
        if (temp <= get_alert_temperature()) alertTemp = false;
        if (temp <= get_alert_temperature() || alertTemp)
            return;
        alert("Stove is too hot");
        alertTemp = true;
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
                    configController.subscribe(StoveAppController.this);
                } catch (final Exception ¢) {
                    ¢.printStackTrace();
                }
            }
        });
    }
}
