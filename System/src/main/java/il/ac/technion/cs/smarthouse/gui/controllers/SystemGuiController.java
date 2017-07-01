package il.ac.technion.cs.smarthouse.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.smarthouse.gui_controller.GuiController;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.SystemMode;

/**
 * This is the abstract controller of the entire system GUI
 * 
 * @author RON
 * @since 07-06-2017
 */

public abstract class SystemGuiController extends GuiController<SystemCore> {
    public static final String FXML_BASE_PATH = "/fxmls/system/";

    public static <T extends SystemGuiController> T createRootController(final String fxmlFileName,
                    final SystemCore c) {
        return GuiController.createRootController(SystemGuiController.class.getResource(FXML_BASE_PATH + fxmlFileName),
                        c);
    }

    protected final <T extends SystemGuiController> T createChildController(final String fxmlFileName) {
        return super.createChildController(SystemGuiController.class.getResource(FXML_BASE_PATH + fxmlFileName));
    }

    @Override
    protected final <T extends GuiController<SystemCore>> void initialize(SystemCore model, T parent, URL location,
                    ResourceBundle b) {
        initialize(model, parent, model.getSystemMode(), location, b);
    }

    protected abstract <T extends GuiController<SystemCore>> void initialize(SystemCore model, T parentController,
                    SystemMode m, URL location, ResourceBundle b);
}
