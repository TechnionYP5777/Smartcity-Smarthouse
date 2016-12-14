package il.ac.technion.cs.eldery.applications.gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StoveModuleGui extends Application{
    
    Timeline timeline;
    private Label timerLabel = new Label();
    DoubleProperty timeSeconds = new SimpleDoubleProperty();
    Duration time = Duration.ZERO;
    
    static void openNewWindow(){
        Stage stage = new Stage();
        Scene scene = new Scene(new Group());
        stage.setWidth(450);
        stage.setHeight(250);
        stage.setTitle("Stove Config");
        final Label label = new Label("Stove Config");
        label.setFont(new Font("Arial", 20));
        
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label);
 
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(final String[] args) {
        launch(args);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new Group());
        primaryStage.setTitle("Stove configuration");
        primaryStage.setWidth(450);
        primaryStage.setHeight(250);
 
        final Label label = new Label("Stove Feed");
        label.setFont(new Font("Arial", 20));
        
        timerLabel.textProperty().bind(timeSeconds.asString());
        timerLabel.setTextFill(Color.RED);
        timerLabel.setFont(new Font("Arial", 20));
        
        final Label timePref = new Label("The Stove is runnig for: ");
        timePref.setTextFill(Color.RED);
        timePref.setFont(new Font("Arial", 20));
        
        Button button = new Button();
        button.setText("Start/Reset");
        button.setOnAction(new EventHandler() {
            boolean start = true;
            @Override
            public void handle(Event __) {
                if (!start) {
                    timeline.stop();
                    this.start = true;
                    time = Duration.ZERO;
                    timeSeconds.set(time.toSeconds());
                } else {
                    timeline = new Timeline(
                        new KeyFrame(Duration.millis(100),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                Duration duration = ((KeyFrame)e.getSource()).getTime();
                                time = time.add(duration);
                                timeSeconds.set(time.toSeconds());
                            }
                        })
                    );
                    timeline.setCycleCount(Animation.INDEFINITE);
                    timeline.play();
                    this.start=false;
                }
            }
        });
        
        Button btnOpenNewWindow = new Button();
        btnOpenNewWindow.setText("Stove Config");
        btnOpenNewWindow.setOnAction(new EventHandler<ActionEvent>() {
            @SuppressWarnings("static-access")
            @Override
            public void handle(ActionEvent __) {
                   StoveModuleGui.this.openNewWindow();
            }
        });
        final HBox hbox = new HBox();
        hbox.getChildren().addAll(timePref,this.timerLabel);
        
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label,hbox,button,btnOpenNewWindow);
 
        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
