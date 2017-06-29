package il.ac.technion.cs.smarthouse.gui.controllers.main_system;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.gui.controllers.SystemGuiController;
import il.ac.technion.cs.smarthouse.gui.controllers.applications.ApplicationViewController;
import il.ac.technion.cs.smarthouse.gui.javafx_elements.LogConsole;
import il.ac.technion.cs.smarthouse.gui_controller.GuiController;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.SystemMode;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem.ReadOnlyFileNode;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
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
    @FXML SplitPane mainSplitPane;

    TextArea loggerView = new TextArea();
    TitledPane consolePane = new TitledPane("Console", loggerView);
    TreeTableView<String> fsTreeView = new TreeTableView<>();

    private static Logger log = LoggerFactory.getLogger(MainSystemGuiController.class);

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

                setupFSTreeView(model);

                model.getFileSystem().subscribe((path, data) -> {
                    fsTreeView = new TreeTableView<>();

                    Platform.runLater(() -> {
                        mainSplitPane.getItems().remove(1);
                        setupFSTreeView(model);

                    });

                }, FileSystemEntries.SENSORS.buildPath());

            }

        } catch (final Exception e) {
            log.error("The controller encountered exception", e);
            // TODO: should throw runtime exception?
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

    public void setupFSTreeView(SystemCore model) {

        TreeItem<String> rootElement = new TreeItem<>("File System View");
        rootElement.setExpanded(true);

        createTree(model.getFileSystem().getReadOnlyFileSystem(), rootElement);
        TreeTableColumn<String, String> fsViewCol = new TreeTableColumn<>();
        fsViewCol.prefWidthProperty().bind(fsTreeView.widthProperty());

        fsViewCol.setCellValueFactory(
                        (CellDataFeatures<String, String> p) -> new ReadOnlyStringWrapper(p.getValue().getValue()));

        fsTreeView.setRoot(rootElement);
        fsTreeView.getColumns().add(fsViewCol);

        mainSplitPane.getItems().add(fsTreeView);
        mainSplitPane.getDividers().get(0).setPosition(0.7);

    }

    private void createTree(ReadOnlyFileNode currNode, TreeItem<String> currTreeNode) {

        TreeItem<String> newTreeNode = new TreeItem<>(currNode.getName());
        if (currNode.isLeaf())
            return;
        for (ReadOnlyFileNode child : currNode.getChildren())
            createTree(child, newTreeNode);
        newTreeNode.setExpanded(true);
        currTreeNode.getChildren().add(newTreeNode);

    }
}
