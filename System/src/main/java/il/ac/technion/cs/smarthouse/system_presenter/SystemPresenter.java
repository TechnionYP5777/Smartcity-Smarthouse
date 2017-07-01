package il.ac.technion.cs.smarthouse.system_presenter;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.gui.controllers.SystemGuiController;
import il.ac.technion.cs.smarthouse.gui.controllers.main_system.MainSystemGuiController;
import il.ac.technion.cs.smarthouse.notification_center.NotificationsCenter;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.SystemMode;
import il.ac.technion.cs.smarthouse.system.failure_detector.SystemFailureDetector;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * A presenter class for the whole system. The model and the GUI should be
 * started from here.
 * 
 * @author RON
 * @author Yarden
 * @since 07-06-2017
 */
public class SystemPresenter {
    static final Logger log = LoggerFactory.getLogger(SystemPresenter.class);

    static final String APP_ROOT_FXML = "main_system_ui.fxml";
    static final String DEV_CSS = "/fxmls/system/css/modena_dark.css";

    static final String APP_NAME = "Smarthouse";
    static final String APP_LOGO = "/icons/smarthouse-icon.png";
    static final double APP_WIDTH = 1000;
    static final double APP_HEIGHT = 800;

    final SystemCore model;
    MainSystemGuiController viewController;
    Stage viewPrimaryStage;
    final List<Runnable> viewOnCloseListeners = new ArrayList<>();

    SystemPresenter(final boolean createGui, final boolean createPrimaryStage, final boolean showModePopup,
                    final SystemMode defaultMode, final boolean enableFailureDetector,
                    final boolean enableNotifications) {
        model = new SystemCore();
        model.setSystemMode(defaultMode);

        if (enableFailureDetector)
            SystemFailureDetector.enable(model.getSystemMode()); // initial FD

        if (!createGui)
            return;

        if (enableNotifications)
            NotificationsCenter.enable();

        viewOnCloseListeners.add(() -> model.shutdown());
        viewOnCloseListeners.add(() -> System.exit(0));

        if (!createPrimaryStage)
            viewController = SystemGuiController.createRootController(APP_ROOT_FXML, model);
        else {
            JavaFxHelper.startGui(new MainSystemGui(showModePopup));

            // in case of showModePopup changing the mode
            if (enableFailureDetector)
                SystemFailureDetector.enable(model.getSystemMode());
        }
    }

    public SystemCore getSystemModel() {
        return model;
    }

    public MainSystemGuiController getSystemView() {
        return viewController;
    }

    public MainSystemGuiController getViewController() {
        return viewController;
    }

    public void addOnCloseListener(final Runnable onCloseListener) {
        viewOnCloseListeners.add(onCloseListener);
    }

    public void close() {
        if (viewPrimaryStage != null)
            viewPrimaryStage.close();
    }

    class MainSystemGui extends Application {
        final boolean showModePopup;

        public MainSystemGui(final boolean showModePopup) {
            this.showModePopup = showModePopup;
        }

        @Override
        public void start(Stage primaryStage) throws Exception {
            getMode();

            log.info("\n\tInitializing system ui in " + model.getSystemMode() + "...");

            viewController = SystemGuiController.createRootController(APP_ROOT_FXML, model);
            viewPrimaryStage = primaryStage;

            final Scene scene = new Scene(viewController.getRootViewNode(), APP_WIDTH, APP_HEIGHT);
            primaryStage.setTitle(APP_NAME);
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(APP_LOGO)));

            if (model.getSystemMode() == SystemMode.DEVELOPER_MODE)
                scene.getStylesheets().add(getClass().getResource(DEV_CSS).toExternalForm());

            primaryStage.setScene(scene);

            primaryStage.setOnCloseRequest(e -> {
                log.info("\n\tSystem ui closing...");
                viewOnCloseListeners.forEach(a -> a.run());
            });

            primaryStage.show();
        }

        private void getMode() {
            if (!showModePopup)
                return;

            log.info("\n\tAsking for mode...");

            ButtonType userType = new ButtonType("User Mode"), devType = new ButtonType("Developer Mode");
            Alert alert = new Alert(AlertType.INFORMATION, "Please select a mode:", userType, devType);
            alert.setTitle("Smarthouse Mode Selection");
            alert.setHeaderText("The Smarthouse has two operation modes.");
            Stage s = (Stage) alert.getDialogPane().getScene().getWindow();
            s.getIcons().add(new Image(getClass().getResourceAsStream(APP_LOGO)));
            s.setOnCloseRequest(e -> System.exit(0));

            ButtonType response = alert.showAndWait().get();
            if (response != null)
                model.setSystemMode(response != devType ? SystemMode.USER_MODE : SystemMode.DEVELOPER_MODE);
        }
    }
}
