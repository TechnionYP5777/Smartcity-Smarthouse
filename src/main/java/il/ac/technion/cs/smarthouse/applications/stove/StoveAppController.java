package il.ac.technion.cs.smarthouse.applications.stove;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.EmergencyLevel;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
import il.ac.technion.cs.smarthouse.system.services.alerts_service.AlertsManager;
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
    static Logger log = LoggerFactory.getLogger(StoveAppController.class);

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

    public void setInstance(final StoveModuleGui instance) {
        this.instance = instance;
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

    private void alert(final String messege) {
        ((AlertsManager) instance.getService(ServiceType.ALERTS_SERVICE)).sendAlert(messege, EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
    }

    public void turnOn() {
        if (isOn)
            return;
        isOn = !isOn;
        timeline = new Timeline(new KeyFrame(Duration.millis(100), ¢ -> {
            time = time.add(((KeyFrame) ¢.getSource()).getTime());
            timeSeconds.set(time.toSeconds());
            timeLabel.setText("The stove is running for: " + timeSeconds.get() + " (Secs)");
            if (timeSeconds.get() <= StoveAppController.this.get_alert_seconds())
                alertTime = false;
            if (timeSeconds.get() > StoveAppController.this.get_alert_seconds() && !alertTime) {
                alert("Stove is running too long");
                timeLabel.setTextFill(Color.RED);
                alertTime = true;
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void turnOf() {
        if (!isOn)
            return;
        isOn = !isOn;
        timeline.stop();
        time = Duration.ZERO;
        timeLabel.setTextFill(Color.BLACK);
        timeSeconds.set(time.toSeconds());
        timeLabel.setText("The stove is: Off");
    }

    public void updateTemperture(final int temp) {
        tempLabel.setText("The stove temperature is: " + temp);
        if (temp <= get_alert_temperature()) {
            alertTemp = false;
            tempLabel.setTextFill(Color.BLACK);
        }
        if (temp <= get_alert_temperature() || alertTemp)
            return;
        tempLabel.setTextFill(Color.RED);
        alert("Stove is too hot");
        alertTemp = true;
    }

    @Override public void initialize(final URL location, final ResourceBundle __) {

        stoveConfigButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(final ActionEvent __1) {
                try {
                    final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("stove_app_config.fxml"));
                    final Parent root1 = (Parent) fxmlLoader.load();
                    final Stage stage = new Stage();
                    stage.setScene(new Scene(root1));
                    stage.show();
                    ((ConfigController) fxmlLoader.getController()).subscribe(StoveAppController.this);
                } catch (final Exception $) {
                    log.error("Failed trying to initialize the ConfigController", $);
                }
            }
        });
    }
}
