package il.ac.technion.cs.smarthouse.system.gui.applications;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.applications.ApplicationManager;
import il.ac.technion.cs.smarthouse.system.applications.ApplicationsCore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ApplicationViewController implements Initializable {
    private static Logger log = LoggerFactory.getLogger(ApplicationViewController.class);
    
    @FXML ListView<String> listView;
    @FXML AnchorPane appView;
    @FXML Button plusButton;
    @FXML VBox vBox;
    File file;

    private ApplicationsCore appsHandler;

    @Override public void initialize(final URL location, final ResourceBundle __) {
        // no special actions to be done here
    }

    public void setAppsHandler(final ApplicationsCore appsHandler) {
        this.appsHandler = appsHandler;

        initVBox();
        initListView();
        initPlusBtn();
    }

    private void initVBox() {
        vBox.setSpacing(10);
    }

    private void initListView() {
        updateListView();
        listView.setOnMouseClicked(e -> appsHandler.getApplicationManagers().get(listView.getSelectionModel().getSelectedIndex()).reopen(appView));
    }

    private void initPlusBtn() {
        plusButton.setStyle("-fx-font: 42 arial; -fx-base: #b6e7c9;");
        plusButton.setOnAction(e -> {
            try {
                final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("applications_installer_view.fxml"));
                final Node n = fxmlLoader.load();

                final ApplicationsInstallerViewController a = (ApplicationsInstallerViewController) fxmlLoader.getController();
                a.setApplicationsHandler(appsHandler);
                a.setApplicationViewController(this);

                AnchorPane.setTopAnchor(n, 0.0);
                AnchorPane.setRightAnchor(n, 0.0);
                AnchorPane.setLeftAnchor(n, 0.0);
                AnchorPane.setBottomAnchor(n, 0.0);

                appView.getChildren().setAll(n);
            } catch (final Exception e1) {
                log.error("An exception while loading the ApplicationsInstallerViewController (after pressing the plus button)", e1);
            }
        });
    }

    public void updateListView() {
        final ObservableList<String> names = FXCollections.observableArrayList();
        for (final ApplicationManager ¢ : appsHandler.getApplicationManagers())
            names.add(¢.getApplicationName());

        listView.setItems(names);
    }
}
