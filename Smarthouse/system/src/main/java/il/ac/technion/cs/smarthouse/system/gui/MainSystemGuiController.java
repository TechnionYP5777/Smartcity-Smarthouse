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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;

public class MainSystemGuiController implements Initializable {
    private MappingController mappingController;
    private ApplicationViewController appsController;
    private UserInfoController userController;
    public SystemCore sysCore;
    @FXML Tab homeTab;
    @FXML Tab userTab;
    @FXML Tab appsTab;
    @FXML Tab sensorsTab;
    @FXML ImageView homePageImageView;
    @FXML HBox homeTabHBox;

    @Override public void initialize(final URL arg0, final ResourceBundle arg1) {
        try {
            this.sysCore = new SystemCore();
            sysCore.initializeSystemComponents();

            FXMLLoader loader;

            // home tab:
            homeTab.setContent(homeTabHBox);
            homePageImageView.setImage(new Image(getClass().getResourceAsStream("/icons/smarthouse-icon-logo.png")));
            homePageImageView.setFitHeight(200);
            //homePageImageView.fitHeightProperty().bind(homeTabHBox.heightProperty().divide(2));
            BackgroundImage myBI = new BackgroundImage(new Image(getClass().getResourceAsStream("/backgrounds/bg_4.png"), 0, 200, false, false), BackgroundRepeat.REPEAT,
                    BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
            homeTabHBox.setBackground(new Background(myBI));
            

            // user tab:
            loader = new FXMLLoader(this.getClass().getResource("user information.fxml"));
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
