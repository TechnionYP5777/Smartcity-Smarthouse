package il.ac.technion.cs.eldery.applications.gui;

import java.net.*;
import java.util.*;

import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;

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

    public void set_temperture(int degrees) {
        this.degrees = degrees;
    }

    public void set_seconds(int seconds) {
        this.seconds = seconds;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" }) @Override public void initialize(final URL location, final ResourceBundle __) {
        onOffButton.setOnAction(new EventHandler() {
            boolean start = true;

            @SuppressWarnings("hiding") @Override public void handle(Event __) {
                if (!start) {
                    timeline.stop();
                    this.start = true;
                    time = Duration.ZERO;
                    timeLabel.setTextFill(Color.BLACK);
                    timeSeconds.set(time.toSeconds());
                    timeLabel.setText("The Stove is: Off");
                    onOffButton.setText("Turn On");
                } else {
                    onOffButton.setText("Turn Off");
                    timeline = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent e) {
                            Duration duration = ((KeyFrame) e.getSource()).getTime();
                            time = time.add(duration);
                            timeSeconds.set(time.toSeconds());
                            timeLabel.setTextFill(timeSeconds.get() > Controller.this.get_seconds() ? Color.RED : Color.BLACK);
                            timeLabel.setText("The Stove is Running for: " + timeSeconds.get() + " (Secs)");
                        }
                    }));
                    timeline.setCycleCount(Animation.INDEFINITE);
                    timeline.play();
                    this.start = false;
                }
            }
        });

        stoveConfigButton.setOnAction(new EventHandler<ActionEvent>() {
            @SuppressWarnings({ "hiding" }) @Override public void handle(ActionEvent __) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("stove_app_config.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root1));
                    stage.show();
                    ConfigController configController = fxmlLoader.getController();
                    configController.subscribe(Controller.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
