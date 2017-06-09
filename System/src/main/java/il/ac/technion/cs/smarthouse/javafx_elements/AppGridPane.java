package il.ac.technion.cs.smarthouse.javafx_elements;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class AppGridPane extends GridPane {
    private int rowCounter;
    
    public AppGridPane() {
        setHgap(10);
        setVgap(10);
        
        for (int i = 0; i < 2; ++i) {
            ColumnConstraints c = new ColumnConstraints();
            c.setHgrow(Priority.SOMETIMES);
            getColumnConstraints().add(c);
        }
        
        setAlignment(Pos.TOP_LEFT);
    }
    
    public void addRow(Node titleNode, Node valueNode) {
        super.addRow(rowCounter++, titleNode, valueNode);
        GridPane.setHalignment(titleNode, HPos.RIGHT);
        GridPane.setHgrow(titleNode, Priority.SOMETIMES);
        GridPane.setHgrow(valueNode, Priority.ALWAYS);
    }
    
    public void addRow(Node n) {
        super.addRow(rowCounter++, n);
        GridPane.setColumnSpan(n, 2);
    }
}
