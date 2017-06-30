package il.ac.technion.cs.smarthouse.developers_api.application_builder;

import javafx.fxml.Initializable;
import javafx.scene.Node;

/**
 * @author RON
 * @since 10-06-2017
 */

public interface CustomRegionBuilder {
    public CustomRegionBuilder setTitle(String title);

    public CustomRegionBuilder add(Node n);

    public CustomRegionBuilder add(String fxmlFileName, GuiBinderObject<? extends Initializable> outController);
}
