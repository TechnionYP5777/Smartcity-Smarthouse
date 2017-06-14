package il.ac.technion.cs.smarthouse.developers_api.application_builder;

import javafx.fxml.Initializable;
import javafx.scene.Node;

public interface CustomRegionBuilder {
    public CustomRegionBuilder setTitle(String title);
    
    public CustomRegionBuilder add(Node n);
    
    public CustomRegionBuilder add(String fxmlFileName, GuiBinderObject<? extends Initializable> outController);
}
