package il.ac.technion.cs.smarthouse.javafx_elements;

import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AppLabel extends Label {
    public AppLabel(String title) {
        this(title, 18);
    }
    
    public AppLabel(String title, double fontSize) {
        super(title);
        setFont(Font.font(null, FontWeight.BOLD, fontSize));
    }
    
    public AppLabel addShadow() {
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
        
        setEffect(ds);
        
        return this;
    }
}
