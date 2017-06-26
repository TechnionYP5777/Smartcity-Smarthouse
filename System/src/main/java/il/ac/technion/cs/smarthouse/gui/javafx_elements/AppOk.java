package il.ac.technion.cs.smarthouse.gui.javafx_elements;

import java.util.function.Consumer;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public abstract class AppOk<NodeType extends Node, V> extends HBox {
    protected final NodeType node;
    private final Button okBtn = new Button("OK");
    protected final Consumer<V> onChangeFunction;

    public AppOk(Consumer<V> onChangeFunction, NodeType node) {
        this.node = node;
        this.onChangeFunction = onChangeFunction;

        okBtn.managedProperty().bind(okBtn.visibleProperty());
        okBtn.setVisible(false);

        okBtn.setOnMouseClicked(e -> finishEdit());

        setHgrow(node, Priority.ALWAYS);
        this.spacingProperty().set(5);

        this.getChildren().add(node);
        this.getChildren().add(okBtn);
    }

    protected void startEdit() {
        okBtn.setVisible(true);
    }

    protected void finishEdit() {
        okBtn.setVisible(false);
        onChangeFunction.accept(getValue());
        this.requestFocus();// TODO: is this needed?
    }

    protected abstract V getValue();
}
