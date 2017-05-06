package il.ac.technion.cs.smarthouse.system.gui.applications;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.applications.ApplicationsCore;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
        
    }

    public void setAppsHandler(final ApplicationsCore appsHandler) {
        this.appsHandler = appsHandler;

        initVBox();
        initListView();
        initPlusBtn();
        
        updateListView();
    }

    private void initVBox() {
        vBox.setSpacing(10);
    }

    private void initListView() {
        updateListView();
        listView.setOnMouseClicked(e -> {
            int index = listView.getSelectionModel().getSelectedIndex();
            if (index >= 0)
                appsHandler.getApplicationManagers().get(index).reopen(appView);
        });
    }

    private void initPlusBtn() {
        plusButton.setStyle("-fx-font: 42 arial; -fx-base: #b6e7c9;");
        plusButton.setOnAction(e -> {
            try {
                final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("applications_installer_view.fxml"));
                JavaFxHelper.placeNodeInPane(fxmlLoader.load(), appView);

                final ApplicationsInstallerViewController a = (ApplicationsInstallerViewController) fxmlLoader.getController();
                a.setApplicationsHandler(appsHandler).setApplicationViewController(this);

            } catch (final Exception e1) {
                log.error("An exception while loading the ApplicationsInstallerViewController (after pressing the plus button)", e1);
            }
        });
    }

    public void updateListView() {
        listView.setItems(FXCollections.observableArrayList(appsHandler.getInstalledApplicationNames()));
    }
}
