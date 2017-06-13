package il.ac.technion.cs.smarthouse.javafx_elements;

import java.util.function.Consumer;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class AppComboBoxField<T> extends ComboBox<T> {
    @SafeVarargs
    public AppComboBoxField(Consumer<T> onChangeFunction, T initialValue, T... comboOptions) {
        getItems().addAll(comboOptions);
        setOnAction(e -> onChangeFunction.accept(getValue()));
        if (initialValue != null)
            setValue(initialValue);
        else if (comboOptions.length > 0)
            setValue(comboOptions[0]);
        setMaxWidth(Double.MAX_VALUE);
        
        HBox.setHgrow(this, Priority.ALWAYS);
    }
}
