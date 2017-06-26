package il.ac.technion.cs.smarthouse.gui.javafx_elements;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javafx.geometry.Pos;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class AppSpinnerField<T> extends AppOk<Spinner<T>, T> {

    public static AppSpinnerField<Double> createDoubleAppSpinner(Consumer<Double> onChangeFunction,
                    double initialValue) {
        return new AppSpinnerField<>(onChangeFunction, s -> {
            try {
                Double.parseDouble(s);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }, new SpinnerValueFactory.DoubleSpinnerValueFactory(Double.MIN_VALUE, Double.MAX_VALUE, initialValue));
    }

    public static AppSpinnerField<Integer> createIntegerAppSpinner(Consumer<Integer> onChangeFunction,
                    int initialValue) {
        return new AppSpinnerField<>(onChangeFunction, s -> {
            try {
                Integer.parseInt(s);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }, new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE, initialValue));
    }

    Predicate<String> canParse;

    AppSpinnerField(Consumer<T> onChangeFunction, Predicate<String> canParse, SpinnerValueFactory<T> factory) {
        super(onChangeFunction, new Spinner<>());
        this.canParse = canParse;
        node.setValueFactory(factory);
        node.setEditable(true);

        node.valueProperty().addListener(event -> super.finishEdit());

        node.getEditor().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() != KeyCode.ENTER)
                startEdit();
            else {
                fixText();
                event.consume();
            }
        });

        node.getEditor().setAlignment(Pos.CENTER_RIGHT);
    }

    @Override
    protected T getValue() {
        return node.getValue();
    }

    private String fixText() {
        String currTextValue = node.getEditor().textProperty().get().replaceAll("[^\\d.]", "");
        if (!canParse.test(currTextValue))
            node.getEditor().textProperty().set("0");
        else {
            node.getEditor().textProperty().set(currTextValue);
            node.getValueFactory().setValue(node.getValueFactory().getConverter().fromString(currTextValue));
        }
        return null;
    }

    @Override
    protected void finishEdit() {
        Optional.ofNullable(fixText()).ifPresent(
                        s -> node.getValueFactory().setValue(node.getValueFactory().getConverter().fromString(s)));
        super.finishEdit();
    }

}
