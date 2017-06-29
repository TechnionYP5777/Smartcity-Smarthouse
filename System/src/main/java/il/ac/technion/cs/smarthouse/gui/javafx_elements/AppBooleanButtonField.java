package il.ac.technion.cs.smarthouse.gui.javafx_elements;

import java.util.function.Consumer;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class AppBooleanButtonField extends HBox {

    public class SwitchButton extends Label {
        SimpleBooleanProperty switchedOn = new SimpleBooleanProperty(true);

        public SwitchButton() {
            Button switchBtn = new Button();
            switchBtn.setPrefWidth(30);
            switchBtn.setOnAction(t -> switchedOn.set(!switchedOn.get()));

            setGraphic(switchBtn);

            switchedOn.addListener((v, t, t1) -> {
                if (t1) {
                    setText("ON");
                    setStyle("-fx-background-color: green;-fx-text-fill:white; -fx-padding: 0 0 0 5; -fx-background-radius: 2;");
                    setContentDisplay(ContentDisplay.RIGHT);
                } else {
                    setText("OFF");
                    setStyle("-fx-background-color: grey;-fx-text-fill:black; -fx-padding: 0 5 0 0; -fx-background-radius: 2;");
                    setContentDisplay(ContentDisplay.LEFT);
                }
            });

            switchedOn.set(false);
        }

        public SimpleBooleanProperty switchOnProperty() {
            return switchedOn;
        }
    }

    public AppBooleanButtonField(Consumer<Boolean> onChangeFunction, boolean initialValue) {
        SwitchButton b = new SwitchButton();
        b.switchOnProperty().set(initialValue);
        b.switchOnProperty().addListener((v, t, t1) -> onChangeFunction.accept(t1));

        this.getChildren().add(b);

        setHgrow(b, Priority.ALWAYS);
    }

}
