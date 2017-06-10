package il.ac.technion.cs.smarthouse.javafx_elements;

import javafx.scene.control.Button;

public class AppButton extends Button {
    public AppButton(String text, Runnable onChangeFunction) {
        this.setText(text);
        this.setOnMouseClicked(e -> onChangeFunction.run());
    }
}
