package il.ac.technion.cs.eldery.system.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.eldery.system.DatabaseHandler;
import il.ac.technion.cs.eldery.system.SystemCore;
import il.ac.technion.cs.eldery.system.applications.ApplicationsCore;
import il.ac.technion.cs.eldery.system.gui.applications.ApplicationViewController;
import il.ac.technion.cs.eldery.system.gui.mapping.MappingController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;

public class MainSystemGuiController implements Initializable {
    private MappingController mappingController;
    private ApplicationViewController appsController;
    private UserInfoController userController;
    @FXML Tab homeTab;
    @FXML Tab userTab;
    @FXML Tab appsTab;
    @FXML Tab sensorsTab;

    @Override public void initialize(final URL arg0, final ResourceBundle arg1) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("user information.fxml"));

            // user tab:
            userTab.setContent((Node) loader.load());
            userController = loader.getController();

            // sensors tab:
            loader = new FXMLLoader(this.getClass().getResource("/il/ac/technion/cs/eldery/system/gui/mapping/house_mapping.fxml"));
            sensorsTab.setContent(loader.load());
            mappingController = loader.getController();

            // applications tab:
            loader = new FXMLLoader(this.getClass().getResource("/il/ac/technion/cs/eldery/system/gui/applications/application_view.fxml"));
            appsTab.setContent(loader.load());
            appsController = loader.getController();

        } catch (final IOException ¢) {
            ¢.printStackTrace();
        }
    }

    public MainSystemGuiController setDatabaseHandler(final DatabaseHandler dbHandler) {
        mappingController.setDatabaseHandler(dbHandler);
        return this;
    }

    public MainSystemGuiController setApplicationsHandler(final ApplicationsCore appsHandler) {
        appsController.setAppsHandler(appsHandler);
        return this;
    }

    public MainSystemGuiController setSysCore(final SystemCore sysCore) {
        userController.setSystemCore(sysCore);
        return this;
    }

}
