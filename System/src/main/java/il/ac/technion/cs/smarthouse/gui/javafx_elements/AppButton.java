package il.ac.technion.cs.smarthouse.gui.javafx_elements;

import javafx.scene.control.Button;

/**
 * @author RON
 * @since 10-06-2017
 */
public class AppButton extends Button {
    public AppButton(String text, Runnable onChangeFunction) {
        this.setText(text);
        this.setOnMouseClicked(e -> onChangeFunction.run());
    }
}
