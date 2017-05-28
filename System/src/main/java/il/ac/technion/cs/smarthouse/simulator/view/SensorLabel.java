package il.ac.technion.cs.smarthouse.simulator.view;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

@SuppressWarnings("restriction")
public class SensorLabel extends StackPane {
    private final double PADDING = 5;
    private CustomLabel text = new CustomLabel(PADDING, PADDING);
    private boolean movable;

    SensorLabel(double x, double y, String text) {
        this.movable = true;
        this.text.setText(text);
        this.text.setStyle("-fx-background-color: white;");
        this.text.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        relocate(x - PADDING, y - PADDING);
        getChildren().add(this.text);
        getStyleClass().add("editable-draggable-text");
        enableDrag();
    }

    public void switchMovableState() {
        this.movable = !this.movable;
    }

    // make a node movable by dragging it around with the mouse.
    private void enableDrag() {
        final Delta dragDelta = new Delta();
        setOnMousePressed(mouseEvent -> {
            if (SensorLabel.this.movable) {
                this.toFront();
                // record a delta distance for the drag and drop operation.
                dragDelta.x = mouseEvent.getX();
                dragDelta.y = mouseEvent.getY();
                getScene().setCursor(Cursor.MOVE);
            }
        });
        setOnMouseReleased(mouseEvent -> {
            if (SensorLabel.this.movable)
                getScene().setCursor(Cursor.HAND);
        });
        setOnMouseDragged(mouseEvent -> {
            if (SensorLabel.this.movable) {
                double newX = getLayoutX() + mouseEvent.getX() - dragDelta.x;
                if (newX > 0 && newX < getScene().getWidth())
                    setLayoutX(newX);
                double newY = getLayoutY() + mouseEvent.getY() - dragDelta.y;
                if (newY > 0 && newY < getScene().getHeight())
                    setLayoutY(newY);
            }
        });
        setOnMouseEntered(mouseEvent -> {
            if (SensorLabel.this.movable && !mouseEvent.isPrimaryButtonDown() && !mouseEvent.isPrimaryButtonDown())
                getScene().setCursor(Cursor.HAND);
        });
        setOnMouseExited(mouseEvent -> {
            if (SensorLabel.this.movable && !mouseEvent.isPrimaryButtonDown() && !mouseEvent.isPrimaryButtonDown())
                getScene().setCursor(Cursor.DEFAULT);
        });
    }

    // records relative x and y co-ordinates.
    private class Delta {
        public Delta() {}

        double x, y;
    }

    private class CustomLabel extends Label {
        private final double RIGHT_MARGIN = 5;

        CustomLabel(double x, double y) {
            relocate(x, y);
            getStyleClass().add("editable-text");
            FontMetrics metrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(getFont());
            setPrefWidth(RIGHT_MARGIN);
            textProperty().addListener((observable, oldTextString,
                            newTextString) -> setPrefWidth(metrics.computeStringWidth(newTextString) + RIGHT_MARGIN));
            Platform.runLater(this::requestFocus);
        }
    }
}
