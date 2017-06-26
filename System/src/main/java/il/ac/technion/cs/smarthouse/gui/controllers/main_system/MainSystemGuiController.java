package il.ac.technion.cs.smarthouse.gui.controllers.main_system;

import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.smarthouse.gui.controllers.SystemGuiController;
import il.ac.technion.cs.smarthouse.gui.controllers.applications.ApplicationViewController;
import il.ac.technion.cs.smarthouse.gui.javafx_elements.LogConsole;
import il.ac.technion.cs.smarthouse.gui_controller.GuiController;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.SystemMode;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MainSystemGuiController extends SystemGuiController {
    ApplicationViewController appsPresenterInfo;

    @FXML TabPane tabs;
    @FXML Tab homeTab;
    @FXML Tab userTab;
    @FXML Tab appsTab;
    @FXML Tab sensorsTab;
    @FXML Tab dashboardTab;
    @FXML ImageView homePageImageView;
    @FXML HBox homeTabHBox;
    @FXML VBox homeVBox;
    @FXML Pane dummyPaneLeft;
    @FXML Pane dummyPaneRight;
    @FXML AnchorPane mainAnchorPane;
    @FXML VBox mainVBox;
    
    TextArea loggerView = new TextArea();
    TitledPane consolePane = new TitledPane("Console", loggerView);

    @Override
    protected <T extends GuiController<SystemCore>> void initialize(final SystemCore model, final T parent,
                    final SystemMode m, final URL location, final ResourceBundle b) {
        try {
            // home tab:
            homeTab.setContent(homeTabHBox);
            homePageImageView.setImage(new Image(getClass().getResourceAsStream("/icons/smarthouse-icon-logo.png")));
            homePageImageView.setFitHeight(250);

            // user tab:
            userTab.setContent(createChildController("user information.fxml").getRootViewNode());

            // sensors tab:
            sensorsTab.setContent(createChildController("house_mapping.fxml").getRootViewNode());

            // applications tab:
            appsPresenterInfo = createChildController("application_view.fxml");
            appsTab.setContent(appsPresenterInfo.getRootViewNode());

            // Dashboard tab:
            dashboardTab.setContent(createChildController("dashboard_ui.fxml").getRootViewNode());

            HBox.setHgrow(dummyPaneLeft, Priority.ALWAYS);
            HBox.setHgrow(dummyPaneRight, Priority.ALWAYS);
            HBox.setHgrow(homeVBox, Priority.ALWAYS);
            homeVBox.setPadding(new Insets(50));

            homeVBox.setAlignment(Pos.BASELINE_LEFT);

            if (m == SystemMode.DEVELOPER_MODE) {
                addDescriptionLine("Welcome! You are in Developer Mode.");
                addDescriptionLine("In this mode you can:");
                addDescriptionLine("- Test your application (\"Applications\" tab).");
                addDescriptionLine("- View widgets you add (\"Dashboard\" tab).");

            } else {
                addDescriptionLine("Welcome! You are in User Mode.");
                addDescriptionLine("In this mode you can:");
                addDescriptionLine("- Add applications and view them (\"Applications\" tab).");
                addDescriptionLine("- Design your own home structure, add sensors and view them (\"Sensors\" tab).");
                addDescriptionLine("- Register, add emergency contacts and view them (\"User Information\" tab).");
                addDescriptionLine(
                                "- View specific data collected from the sensors in your Smarthouse, using widgets (\"Dashboard\" tab).");

            }

            if (m == SystemMode.DEVELOPER_MODE) {
                tabs.getTabs().removeAll(userTab, sensorsTab);
                
                mainVBox.getChildren().add(consolePane);
                consolePane.autosize();
                
                LogConsole.setLogConsole(loggerView);
                loggerView.setEditable(false);

            }

        } catch (final Exception ¢) {
            ¢.printStackTrace();
        }
    }

    public void gotoAppsTab() {
        tabs.getSelectionModel().select(appsTab);
        appsPresenterInfo.selectFirstApp();
    }

    public void addDescriptionLine(final String description) {
        final Label label = new Label(description);
        label.setStyle("-fx-font: 20 System");
        homeVBox.getChildren().add(label);
    }

}
