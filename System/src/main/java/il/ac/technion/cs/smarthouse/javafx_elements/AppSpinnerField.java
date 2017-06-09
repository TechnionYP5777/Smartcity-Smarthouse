package il.ac.technion.cs.smarthouse.javafx_elements;

import java.util.function.Consumer;
import java.util.function.Predicate;

import javafx.geometry.Pos;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class AppSpinnerField<T> extends Spinner<T> {

    public static AppSpinnerField<Double> createDoubleAppSpinner(Consumer<Double> onChangeFunction, double initialValue) {
        AppSpinnerField<Double> a = new AppSpinnerField<>(onChangeFunction, s -> {
            try {
                Double.parseDouble(s);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        });
        a.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(Double.MIN_VALUE, Double.MAX_VALUE,
                        initialValue));
        return a;
    }

    public static AppSpinnerField<Integer> createIntegerAppSpinner(Consumer<Integer> onChangeFunction, int initialValue) {
        AppSpinnerField<Integer> a = new AppSpinnerField<>(onChangeFunction, s -> {
            try {
                Integer.parseInt(s);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        });
        a.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE,
                        initialValue));
        return a;
    }

    String lastGoodValue = "";

    AppSpinnerField(Consumer<T> onChangeFunction, Predicate<String> canParse) {
        this.setEditable(true);

        this.valueProperty().addListener(v -> {
            onChangeFunction.accept(getValue());
            lastGoodValue = getValue().toString();
        });

        getEditor().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER && !canParse.test(getEditor().textProperty().get()))
                getEditor().textProperty().set(lastGoodValue);
        });

        getEditor().setAlignment(Pos.CENTER_RIGHT);
    }

}
