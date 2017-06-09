package il.ac.technion.cs.smarthouse.javafx_elements;

import java.util.function.Consumer;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class AppComboBoxField<T> extends HBox {
    @SafeVarargs
    public AppComboBoxField(Consumer<T> onChangeFunction, T initialValue, T... comboOptions) {
        ComboBox<T> b = new ComboBox<>();
        b.getItems().addAll(comboOptions);
        b.setOnAction(e -> onChangeFunction.accept(b.getValue()));
        b.setValue(initialValue);
        b.setMaxWidth(Double.MAX_VALUE);
        
        this.getChildren().add(b);
        
        setHgrow(b, Priority.ALWAYS);
    }
}
