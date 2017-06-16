package il.ac.technion.cs.smarthouse.system.gui.main_system;

import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.smarthouse.mvp.GuiController;
import il.ac.technion.cs.smarthouse.mvp.system.SystemGuiController;
import il.ac.technion.cs.smarthouse.mvp.system.SystemMode;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.gui.applications.ApplicationViewController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainSystemGuiController extends SystemGuiController {
    ApplicationViewController appsPresenterInfo;

    @FXML TabPane tabs;
    @FXML Tab homeTab;
    @FXML Tab userTab;
    @FXML Tab appsTab;
    @FXML Tab sensorsTab;
    @FXML ImageView homePageImageView;
    @FXML HBox homeTabHBox;
    @FXML VBox homeVBox;

    @Override
    protected <T extends GuiController<SystemCore, SystemMode>> void initialize(SystemCore model, T parent,
                    SystemMode m, URL location, ResourceBundle b) {
        try {
            // home tab:
            homeTab.setContent(homeTabHBox);
            homePageImageView.setImage(new Image(getClass().getResourceAsStream("/icons/smarthouse-icon-logo.png")));
            homePageImageView.setFitHeight(300);
            // homePageImageView.fitHeightProperty().bind(homeTabHBox.heightProperty().divide(2));
//            final BackgroundImage myBI = new BackgroundImage(
//                            new Image(getClass().getResourceAsStream("/backgrounds/bg_4.png"), 0, 200, false, false),
//                            BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
//                            BackgroundSize.DEFAULT);
//            homeTabHBox.setBackground(new Background(myBI));

            // user tab:
            userTab.setContent(createChildController("user information.fxml").getRootViewNode());

            // sensors tab:
            sensorsTab.setContent(createChildController("house_mapping.fxml").getRootViewNode());

            // applications tab:
            appsPresenterInfo = createChildController("application_view.fxml");
            appsTab.setContent(appsPresenterInfo.getRootViewNode());
            
            homeVBox.setAlignment(Pos.BASELINE_LEFT);
            
            if (m == SystemMode.DEVELOPER_MODE) {
                addDescriptionLine("Welcome! You are in Developer Mode.");
                addDescriptionLine("In this mode you can test your application.");
            } else {
                addDescriptionLine("Welcome! You are in User Mode.");
                addDescriptionLine("In this mode you can:");
                addDescriptionLine("- Add applications and view them");
                addDescriptionLine("   (\"applications\" tab).");
                addDescriptionLine("- Design your own home structure, add");
                addDescriptionLine("   sensors and view them (\"sensors\" tab).");
                addDescriptionLine("- Register, add emergency contacts and");
                addDescriptionLine("   view them (\"user information\" tab).");
            }

            if (m == SystemMode.DEVELOPER_MODE)
                tabs.getTabs().removeAll(userTab, sensorsTab);

        } catch (final Exception ¢) {
            ¢.printStackTrace();
        }
    }

    public void gotoAppsTab() {
        tabs.getSelectionModel().select(appsTab);
        appsPresenterInfo.selectFirstApp();
    }

    public void addDescriptionLine(String description) {
        Label label = new Label(description);
        label.setStyle("-fx-font: 20 System");
        homeVBox.getChildren().add(label);
    }
}
