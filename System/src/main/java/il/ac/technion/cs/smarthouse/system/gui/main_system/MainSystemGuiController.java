package il.ac.technion.cs.smarthouse.system.gui.main_system;

import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.smarthouse.mvp.SystemPresenter;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.gui.applications.ApplicationViewController;
import javafx.fxml.FXML;
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

public class MainSystemGuiController extends SystemPresenter {
    PresenterInfo appsPresenterInfo;
    
    @FXML TabPane tabs;
    @FXML Tab homeTab;
    @FXML Tab userTab;
    @FXML Tab appsTab;
    @FXML Tab sensorsTab;
    @FXML ImageView homePageImageView;
    @FXML HBox homeTabHBox;

    @Override public void init(SystemCore model, URL location, ResourceBundle __) {
        try {
            // home tab:
            homeTab.setContent(homeTabHBox);
            homePageImageView.setImage(new Image(getClass().getResourceAsStream("/icons/smarthouse-icon-logo.png")));
            homePageImageView.setFitHeight(200);
            // homePageImageView.fitHeightProperty().bind(homeTabHBox.heightProperty().divide(2));
            BackgroundImage myBI = new BackgroundImage(new Image(getClass().getResourceAsStream("/backgrounds/bg_4.png"), 0, 200, false, false),
                    BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
            homeTabHBox.setBackground(new Background(myBI));

            // user tab:
            userTab.setContent(createChildPresenter("user information.fxml").getRootViewNode());

            // sensors tab:
            sensorsTab.setContent(createChildPresenter("house_mapping.fxml").getRootViewNode());

            // applications tab:
            appsPresenterInfo = createChildPresenter("application_view.fxml");
            appsTab.setContent(appsPresenterInfo.getRootViewNode());

        } catch (final Exception ¢) {
            ¢.printStackTrace();
        }
    }
    
    public void gotoAppsTab() {
        tabs.getSelectionModel().select(appsTab);
        appsPresenterInfo.<ApplicationViewController>getPresenter().selectFirstApp();
    }
}
