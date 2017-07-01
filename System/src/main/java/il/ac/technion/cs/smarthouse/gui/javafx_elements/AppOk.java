package il.ac.technion.cs.smarthouse.gui.javafx_elements;

import java.util.function.Consumer;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * An abstract element composed of some Node and an OK button that appears when
 * the Node's value changes. This element will be used in the applications' GUI
 * builder.
 * 
 * @author RON
 * @author Yarden
 * @since 26-06-2017
 */
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

    /**
     * Defines what happens when the node's value changes.
     */
    protected void startEdit() {
        okBtn.setVisible(true);
    }

    /**
     * Saves the changes and restores the AppOK object to unchanged state.
     */
    protected void finishEdit() {
        okBtn.setVisible(false);
        onChangeFunction.accept(getValue());
        this.requestFocus();
    }

    /**
     * 
     * @return The value of the inner node
     */
    protected abstract V getValue();
}