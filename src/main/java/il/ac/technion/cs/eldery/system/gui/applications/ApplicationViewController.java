package il.ac.technion.cs.eldery.system.gui.applications;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.eldery.applications.sos.SosAppGui;
import il.ac.technion.cs.eldery.applications.stove.StoveModuleGui;
import il.ac.technion.cs.eldery.system.applications.ApplicationManager;
import il.ac.technion.cs.eldery.system.applications.ApplicationsHandler;
import il.ac.technion.cs.eldery.system.applications.api.exceptions.OnLoadException;
import il.ac.technion.cs.eldery.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.eldery.system.applications.installer.ApplicationPath.PathType;
import il.ac.technion.cs.eldery.system.exceptions.AppInstallerException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class ApplicationViewController implements Initializable {
    @FXML ListView<String> listView;
    @FXML AnchorPane appView;
    @FXML Button plusButton;
    
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
    
    private void initPlusBtn() {
        this.plusButton.setOnAction(e -> {
            try {
                appsHandler.addApplication(new ApplicationPath<>(PathType.CLASS_NAME, StoveModuleGui.class.getName()));
            } catch (AppInstallerException | IOException | OnLoadException e1) {
                e1.printStackTrace();
            }
            
            updateListView();
        });
    }
    
    private void updateListView() {
        ObservableList<String> names = FXCollections.observableArrayList();
        for (ApplicationManager m : appsHandler.getApplicationManagers())
            names.add(m.getApplicationName());

        listView.setItems(names);
    }
}
