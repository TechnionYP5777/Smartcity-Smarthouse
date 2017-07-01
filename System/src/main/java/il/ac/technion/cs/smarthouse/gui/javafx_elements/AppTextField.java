package il.ac.technion.cs.smarthouse.gui.javafx_elements;

import java.util.function.Consumer;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * A text field with an OK button that can be used in the applications' GUI
 * builder.
 * 
 * @author RON
 * @author Yarden
 * @since 10-06-2017
 */
public class AppTextField extends AppOk<TextField, String> {

    public AppTextField(Consumer<String> onChangeFunction, String initialValue) {
        super(onChangeFunction, new TextField(initialValue));
        node.setOnKeyPressed(e -> handleKeyPressed(e));
    }

    /*
     * (non-Javadoc)
     * 
     * @see il.ac.technion.cs.smarthouse.gui.javafx_elements.AppOk#getValue()
     */
    @Override
    protected String getValue() {
        return node.getText();
    }

    /**
     * Handles all possible keyboard events.
     * 
     * @param e
     *            The given keyboard event
     */
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
