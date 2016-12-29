package il.ac.technion.cs.eldery.applications.stove;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
public class Controller implements Initializable {
    @FXML public Button onOffButton;
    @FXML public Label timeLabel;
    @FXML public Label tempLabel;
    @FXML public Button stoveConfigButton;
    Timeline timeline;
    DoubleProperty timeSeconds = new SimpleDoubleProperty();
    Duration time = Duration.ZERO;
    int degrees = 150;
    int seconds = 30;

    public int get_temperture() {
        return degrees;
    }

    public int get_seconds() {
        return seconds;
    }

    public void set_temperture(final int degrees) {
        this.degrees = degrees;
    }

    public void set_seconds(final int seconds) {
        this.seconds = seconds;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" }) @Override public void initialize(final URL location, final ResourceBundle __) {
        onOffButton.setOnAction(new EventHandler() {
            boolean start = true;

            @Override @SuppressWarnings("hiding") public void handle(final Event __) {
                if (!start) {
                    timeline.stop();
                    start = true;
                    time = Duration.ZERO;
                    timeLabel.setTextFill(Color.BLACK);
                    timeSeconds.set(time.toSeconds());
                    timeLabel.setText("The Stove is: Off");
                    onOffButton.setText("Turn On");
                } else {
                    onOffButton.setText("Turn Off");
                    timeline = new Timeline(new KeyFrame(Duration.millis(100), ¢ -> {
                        time = time.add(((KeyFrame) ¢.getSource()).getTime());
                        timeSeconds.set(time.toSeconds());
                        timeLabel.setTextFill(timeSeconds.get() > Controller.this.get_seconds() ? Color.RED : Color.BLACK);
                        timeLabel.setText("The Stove is Running for: " + timeSeconds.get() + " (Secs)");
                    }));
                    timeline.setCycleCount(Animation.INDEFINITE);
                    timeline.play();
                    start = false;
                }
            }
        });

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
