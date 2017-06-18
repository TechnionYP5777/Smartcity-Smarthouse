package il.ac.technion.cs.smarthouse.gui.javafx_elements;

import java.util.function.Consumer;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class AppTextField extends HBox {
    private final TextField textField = new TextField();
    private final Button okBtn = new Button("OK");
    private final Consumer<String> onChangeFunction;

    public AppTextField(Consumer<String> onChangeFunction, String initialValue) {
        this.onChangeFunction = onChangeFunction;
        
        textField.setText(initialValue);
        
        okBtn.managedProperty().bind(okBtn.visibleProperty());
        okBtn.setVisible(false);
        
        textField.setOnKeyPressed(e -> handleKeyPressed(e));
        okBtn.setOnMouseClicked(e -> finishEdit());

        setHgrow(textField, Priority.ALWAYS);
        this.spacingProperty().set(5);

        this.getChildren().add(textField);
        this.getChildren().add(okBtn);
    }

    private void handleKeyPressed(final KeyEvent e) {
        if (e != null && e.getCode().equals(KeyCode.ENTER)) {
            finishEdit();
            return;
        }
        
        if (e == null || e.getCode().isLetterKey() || e.getCode().isDigitKey() || e.getCode().isWhitespaceKey() || e.getCode().equals(KeyCode.BACK_SPACE))
            startEdit();
    }

    private void startEdit() {
        okBtn.setVisible(true);
    }

    private void finishEdit() {
        okBtn.setVisible(false);
        onChangeFunction.accept(textField.getText());
        this.requestFocus();//TODO: is this needed?
    }
}
