package il.ac.technion.cs.smarthouse.mvp.system;

import il.ac.technion.cs.smarthouse.mvp.GuiController;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import javafx.scene.Node;

public abstract class SystemGuiController extends GuiController<SystemCore, SystemMode> {
    public static final String FXML_BASE_PATH = "/fxmls/system/";
    
    public static <T extends SystemGuiController> T createRootController(final String fxmlFileName, final SystemCore c, final SystemMode m) {
        return GuiController.createRootController(SystemGuiController.class.getResource(FXML_BASE_PATH + fxmlFileName), c, m);
    }
    
    protected final <T extends SystemGuiController> T createChildController(final String fxmlFileName) {
        return super.createChildController(SystemGuiController.class.getResource(FXML_BASE_PATH + fxmlFileName));
    }
    
    public SystemMode getSystemMode() {
        return getExtraData();
    }
    
    public void setMyTheme(Node n) {
        if (getSystemMode() == SystemMode.DEVELOPER_MODE)
            n.setStyle("-fx-background-color: #262626");
    }
    
    public void setMyTheme() {
        setMyTheme(getRootViewNode());
    }
}
