package il.ac.technion.cs.smarthouse.gui.controllers.applications;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.smarthouse.gui.controllers.SystemGuiController;
import il.ac.technion.cs.smarthouse.gui_controller.GuiController;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.SystemMode;
import il.ac.technion.cs.smarthouse.system.applications.ApplicationsCore;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath.PathType;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ApplicationViewController extends SystemGuiController {
    @FXML ListView<String> listView;
    @FXML AnchorPane appView;
    @FXML Button plusButton;
    @FXML VBox vBox;
    @FXML VBox dnd_box;
    @FXML Label dnd_label;
    @FXML SplitPane splitPane;
    @FXML TitledPane titledPane;
    @FXML TitledPane titledPaneInstaller;
    @FXML VBox vBoxInstaller;
    @FXML AnchorPane anchorPaneInstaller;
    @FXML SplitPane leftPanelSplitPane;

    private ApplicationsCore appsHandler;

    @Override
    protected <T extends GuiController<SystemCore>> void initialize(SystemCore model, T parent,
                    SystemMode m, URL location, ResourceBundle b) {
        appsHandler = model.getSystemApplicationsHandler();

        model.getSystemApplicationsHandler().setOnAppsListChange(this::updateListView);

        initDndLabel();

        initListView();
        initDndField();
        initPlusBtn();

        updateListView();

        initMode(m);
    }

    private void initMode(SystemMode m) {
        switch (m) {
            case DEVELOPER_MODE:
                titledPaneInstaller.setVisible(false);
                leftPanelSplitPane.setDividerPositions(1);
                break;
            case USER_MODE:
                anchorPaneInstaller.setMinHeight(200);
                break;
            default:
                break;
        }
    }

    private void initDndLabel() {
        dnd_label.setText("Drag & drop\napplication's JAR here");
        JavaFxHelper.placeNodeInPane(dnd_label, dnd_box);
    }

    private void initListView() {
        updateListView();
        listView.setOnMouseClicked(e -> {
            final int index = listView.getSelectionModel().getSelectedIndex();
            if (index >= 0)
                appsHandler.getApplicationManagers().get(index).reopen(appView);
        });
    }

    private void initPlusBtn() {
        plusButton.setOnAction(e -> browseForJar());
    }

    private void initDndField() {
        dnd_box.setOnDragOver(event -> {
            if (!event.getDragboard().hasFiles())
                event.consume();
            else
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        });

        dnd_box.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                success = true;
                for (File file : db.getFiles())
                    installApp(file.getAbsolutePath());
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void browseForJar() {
        final Stage stage = new Stage();
        final FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("JAR files (*jar)", "*.jar"));
        fileChooser.setTitle("Locate the Application's Jar");
        final File res = fileChooser.showOpenDialog(stage);
        if (res != null)
            installApp(res.getAbsolutePath());
    }

    private void installApp(final String fullPath) {
        getModel().getSystemApplicationsHandler().addApplication(new ApplicationPath(PathType.JAR_PATH, fullPath));
        updateListView();
    }

    public void updateListView() {
        listView.setItems(FXCollections.observableArrayList(appsHandler.getInstalledApplicationNames()));
    }

    public void selectFirstApp() {
        if (!appsHandler.getApplicationManagers().isEmpty())
            Platform.runLater(() -> appsHandler.getApplicationManagers().get(0).reopen(appView));
    }
}
