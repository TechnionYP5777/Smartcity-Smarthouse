package il.ac.technion.cs.smarthouse.gui.javafx_elements;

import java.util.function.Consumer;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class AppTextField extends AppOk<TextField, String> {

    public AppTextField(Consumer<String> onChangeFunction, String initialValue) {
        super(onChangeFunction, new TextField(initialValue));
        node.setOnKeyPressed(e -> handleKeyPressed(e));
    }

    @Override
    protected String getValue() {
        return node.getText();
    }

    protected void handleKeyPressed(final KeyEvent e) {
        if (e != null && e.getCode().equals(KeyCode.ENTER)) {
            finishEdit();
            return;
        }

        if (e == null || e.getCode().isLetterKey() || e.getCode().isDigitKey() || e.getCode().isWhitespaceKey()
                        || e.getCode().equals(KeyCode.BACK_SPACE))
            startEdit();
    }
}
