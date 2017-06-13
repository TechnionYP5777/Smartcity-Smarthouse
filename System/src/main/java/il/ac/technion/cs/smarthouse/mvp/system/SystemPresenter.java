package il.ac.technion.cs.smarthouse.mvp.system;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.gui.main_system.MainSystemGuiController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * A presenter class for the whole system. The model and gui should be started
 * from here.
 * 
 * @author RON
 * @author Yarden
 * @since 07-06-2017
 */
public class SystemPresenter {
    static final Logger log = LoggerFactory.getLogger(SystemPresenter.class);

    static final String APP_ROOT_FXML = "main_system_ui.fxml";

    SystemCore model;
    MainSystemGuiController viewController;
    Stage viewPrimaryStage;
    final List<Runnable> viewOnCloseListeners = new ArrayList<>();

    SystemPresenter() {}

    public SystemCore getSystemModel() {
        return model;
    }

    public MainSystemGuiController getSystemView() {
        return viewController;
    }

    public void addOnCloseListener(final Runnable onCloseListener) {
        viewOnCloseListeners.add(onCloseListener);
    }

    public void close() {
        if (viewPrimaryStage != null)
            viewPrimaryStage.close();
    }

    public static class View {
        // TODO: not now...
    }

    class MainSystemGui extends Application {
        static final String APP_NAME = "Smarthouse";
        static final String APP_LOGO = "/icons/smarthouse-icon.png";
        static final double APP_WIDTH = 1000;
        static final double APP_HEIGHT = 800;

        @Override
        public void start(Stage primaryStage) throws Exception {
            log.info("Initializing system ui...");

            viewController = SystemGuiController.createRootController(APP_ROOT_FXML, model);
            viewPrimaryStage = primaryStage;

            final Scene scene = new Scene(viewController.getRootViewNode(), APP_WIDTH, APP_HEIGHT);
            primaryStage.setTitle(APP_NAME);
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(APP_LOGO)));
            primaryStage.setScene(scene);

            ButtonType userType = new ButtonType("User Mode");
            ButtonType devType = new ButtonType("Developer Mode");
            Alert alert = new Alert(AlertType.INFORMATION, "Please select a mode:", userType, devType);
            alert.setTitle("Smarthouse Mode Selection");
            alert.setHeaderText("The Smarthouse has two operation modes.");
            alert.showAndWait().ifPresent(response -> viewController.setMode(response == devType));

            primaryStage.setOnCloseRequest(e -> {
                log.info("System ui closing...");
                viewOnCloseListeners.forEach(a -> a.run());
            });

            primaryStage.show();
        }
    }
}
