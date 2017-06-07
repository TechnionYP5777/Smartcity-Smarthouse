package il.ac.technion.cs.smarthouse.system.main;

import java.net.URL;

import il.ac.technion.cs.smarthouse.mvp.GuiController;
import il.ac.technion.cs.smarthouse.system.SystemCore;

public abstract class SystemGuiController extends GuiController<SystemCore> {
    public static final String FXML_BASE_PATH = "/fxmls/system/";
    
    public static <T extends SystemGuiController> T createRootController(final String fxmlFileName, final SystemCore c) {
        final URL fxmlLocation = SystemGuiController.class.getResource(FXML_BASE_PATH + fxmlFileName);
        return GuiController.createRootController(fxmlLocation, c);
    }
    
    protected final <T extends SystemGuiController> T createChildController(final String fxmlFileName) {
        final URL fxmlLocation = SystemGuiController.class.getResource(FXML_BASE_PATH + fxmlFileName);
        return super.createChildController(fxmlLocation);
    }
}
