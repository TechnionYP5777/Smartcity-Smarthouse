package il.ac.technion.cs.eldery.system.gui.applications;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.ToggleSwitch;

import il.ac.technion.cs.eldery.system.applications.ApplicationManager;
import il.ac.technion.cs.eldery.system.applications.ApplicationsHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class ApplicationViewController implements Initializable {
    @FXML ListView<String> listView;
    @FXML AnchorPane appView;
    @FXML Button plusButton;
    File file;

    private ApplicationsHandler appsHandler;

    @Override public void initialize(URL location, ResourceBundle __) {
        return;
    }

    public void setAppsHandler(ApplicationsHandler appsHandler) {
        this.appsHandler = appsHandler;

        initListView();
        initPlusBtn();
    }

    private void initListView() {
        updateListView();
        listView.setOnMouseClicked(e -> {
            appsHandler.getApplicationManagers().get(listView.getSelectionModel().getSelectedIndex()).reopen(appView);
        });
    }

    @SuppressWarnings("boxing") private void initPlusBtn() {
        this.plusButton.setStyle("-fx-font: 42 arial; -fx-base: #b6e7c9;");
        this.plusButton.setOnAction(e -> {
            try {
                final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("applications_installer_view.fxml"));
                Node n = fxmlLoader.load();

                ApplicationsInstallerViewController a = (ApplicationsInstallerViewController) fxmlLoader.getController(); 
                a.setApplicationsHandler(appsHandler);
                a.setApplicationViewController(this);

                AnchorPane.setTopAnchor(n, 0.0);
                AnchorPane.setRightAnchor(n, 0.0);
                AnchorPane.setLeftAnchor(n, 0.0);
                AnchorPane.setBottomAnchor(n, 0.0);

                appView.getChildren().setAll(n);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    public void updateListView() {
        ObservableList<String> names = FXCollections.observableArrayList();
        for (ApplicationManager m : appsHandler.getApplicationManagers())
            names.add(m.getApplicationName());

        listView.setItems(names);
    }
}
