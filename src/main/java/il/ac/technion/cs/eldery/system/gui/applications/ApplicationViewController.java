package il.ac.technion.cs.eldery.system.gui.applications;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.ToggleSwitch;

import il.ac.technion.cs.eldery.applications.sos.SosAppGui;
import il.ac.technion.cs.eldery.applications.stove.StoveModuleGui;
import il.ac.technion.cs.eldery.system.applications.ApplicationManager;
import il.ac.technion.cs.eldery.system.applications.ApplicationsHandler;
import il.ac.technion.cs.eldery.system.applications.api.exceptions.OnLoadException;
import il.ac.technion.cs.eldery.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.eldery.system.applications.installer.ApplicationPath.PathType;
import il.ac.technion.cs.eldery.system.exceptions.AppInstallerException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ApplicationViewController implements Initializable {
    @FXML ListView<String> listView;
    @FXML AnchorPane appView;
    @FXML Button plusButton;
    @FXML Label label;
    @FXML ToggleSwitch toggleSwitch;
    File file;

    private ApplicationsHandler appsHandler;

    @Override public void initialize(URL location, ResourceBundle __) {
        return;
    }

    public void setAppsHandler(ApplicationsHandler appsHandler) {
        this.appsHandler = appsHandler;

        initListView();
        initPlusBtn();
        initLabel();
        initSwitch();
    }

    private void initListView() {
        updateListView();
        listView.setOnMouseClicked(e -> {
            appsHandler.getApplicationManagers().get(listView.getSelectionModel().getSelectedIndex()).reopen(appView);
        });
    }

    private void initPlusBtn() {
        this.plusButton.setOnAction(e -> {
            browseFile();
        });
    }

    private void initLabel() {
        this.label.setText("Browse File");
    }

    private void initSwitch() {
        this.toggleSwitch.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override public void changed(ObservableValue<? extends Boolean> __, Boolean oldValue, Boolean newValue) {
                if (!newValue.booleanValue()) {
                    ApplicationViewController.this.label.setText("Browse File:");
                    ApplicationViewController.this.plusButton.setOnAction(e -> {
                        browseFile();
                    });
                } else {
                    ApplicationViewController.this.label.setText("Choose From Classes:");
                    ApplicationViewController.this.plusButton.setOnAction(e -> {
                        comboBoxWindow();
                    });
                }
            }
        });
    }

    private void updateListView() {
        ObservableList<String> names = FXCollections.observableArrayList();
        for (ApplicationManager m : appsHandler.getApplicationManagers())
            names.add(m.getApplicationName());

        listView.setItems(names);
    }

    protected void browseFile() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        this.file = fileChooser.showOpenDialog(stage);
    }

    protected void comboBoxWindow() {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ComboBoxSelection.fxml"));
        Parent root1 = null;
        try {
            root1 = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.setTitle("Application Installer");
        stage.show();
        ((ComboWindowController) fxmlLoader.getController()).setController(this);
    }
}
