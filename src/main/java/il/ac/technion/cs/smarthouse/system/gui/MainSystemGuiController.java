package il.ac.technion.cs.smarthouse.system.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.gui.applications.ApplicationViewController;
import il.ac.technion.cs.smarthouse.system.gui.mapping.MappingController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;

public class MainSystemGuiController implements Initializable {
    private MappingController mappingController;
    private ApplicationViewController appsController;
    private UserInfoController userController;
    private SystemCore sysCore;
    @FXML Tab homeTab;
    @FXML Tab userTab;
    @FXML Tab appsTab;
    @FXML Tab sensorsTab;

    @Override public void initialize(final URL arg0, final ResourceBundle arg1) {
        try {
            
            this.sysCore = new SystemCore();
            sysCore.initializeSystemComponents();
            
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("user information.fxml"));

            // user tab:
            userTab.setContent((Node) loader.load());
            userController = loader.getController();
            userController.setSystemCore(this.sysCore);

            // sensors tab:
            loader = new FXMLLoader(this.getClass().getResource("mapping/house_mapping.fxml"));
            sensorsTab.setContent(loader.load());
            mappingController = loader.getController();
            mappingController.setDatabaseHandler(sysCore.databaseHandler);

            // applications tab:
            loader = new FXMLLoader(this.getClass().getResource("applications/application_view.fxml"));
            appsTab.setContent(loader.load());
            appsController = loader.getController();
            appsController.setAppsHandler(sysCore.applicationsHandler);

        } catch (final IOException ¢) {
            ¢.printStackTrace();
        }
    }

}
