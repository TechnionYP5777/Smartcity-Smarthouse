package il.ac.technion.cs.eldery.system.gui.applications;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.layout.HBox;

import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import org.controlsfx.control.ToggleSwitch;

import il.ac.technion.cs.eldery.applications.PremadeApplications;
import il.ac.technion.cs.eldery.system.applications.ApplicationsHandler;
import il.ac.technion.cs.eldery.system.applications.api.exceptions.OnLoadException;
import il.ac.technion.cs.eldery.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.eldery.system.applications.installer.ApplicationPath.PathType;
import il.ac.technion.cs.eldery.system.exceptions.AppInstallerException;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ApplicationsInstallerViewController implements Initializable {
    @FXML private ToggleSwitch toggleBtn;
    @FXML private VBox toggleOptionsParent;
    @FXML private HBox toggleOptionBrowse;
    @FXML private TextField browseText;
    @FXML private Button browseBtn;
    @FXML private HBox toggleOptionCombo;
    @FXML private ComboBox<String> comboBox;
    @FXML private Button installBtn;

    private ApplicationViewController applicationViewController;
    private ApplicationsHandler applicationsHandler;
    private boolean inRealMode;

    @Override public void initialize(URL location, ResourceBundle __) {
        initToggleBtn();
        initComboBox();
        initInstallBtn();
        initBrowseBtn();

        gotoRegularMode();

        return;
    }

    public void setApplicationsHandler(ApplicationsHandler h) {
        applicationsHandler = h;
    }
    
    public void setApplicationViewController(ApplicationViewController c) {
        this.applicationViewController = c;
    }

    private void initToggleBtn() {
        toggleBtn.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override public void changed(ObservableValue<? extends Boolean> __, Boolean oldValue, Boolean newValue) {
                if (newValue.booleanValue())
                    gotoTestMode();
                else
                    gotoRegularMode();
            }
        });
    }

    private void initComboBox() {
        comboBox.setPromptText("Choose File");
        comboBox.getItems().addAll(Arrays.stream(PremadeApplications.values()).map(x -> x.getAppName()).toArray(String[]::new));

    }

    private void initInstallBtn() {
        installBtn.setOnAction(e -> {
            if (applicationsHandler == null)
                return;

            if (inRealMode)
                installApp(new ApplicationPath<>(PathType.JAR_PATH, browseText.getText()));
            else if (comboBox.getValue() != null)
                installApp(new ApplicationPath<>(PathType.CLASS_NAME, PremadeApplications.getByName(comboBox.getValue()).getAppClass().getName()));
            
            applicationViewController.updateListView();
        });
    }
    
    private void initBrowseBtn() {
        browseBtn.setOnAction(e -> {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            browseText.setText(fileChooser.showOpenDialog(stage).getAbsolutePath());
        });
    }

    private void installApp(ApplicationPath<?> p) {
        try {
            applicationsHandler.addApplication(p);
        } catch (AppInstallerException e) {
            alert(e.getMessage());
        } catch (IOException e) {
            alert("IO Error: " + e.getMessage());
        } catch (OnLoadException e) {
            alert(e.getMessage());
        }
    }

    private static void alert(String messege) {
        final Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("ALERT");
        alert.setHeaderText("Installation Error");
        alert.setContentText(messege);
        alert.show();
    }

    void gotoTestMode() {
        inRealMode = false;
        toggleBtn.setText("Test Mode");

        toggleOptionBrowse.setVisible(false);
        toggleOptionCombo.setVisible(true);

        // toggleOptionsParent.getChildren().setAll(toggleOptionCombo);
    }

    void gotoRegularMode() {
        inRealMode = true;
        toggleBtn.setText("Real Mode");

        toggleOptionBrowse.setVisible(true);
        toggleOptionCombo.setVisible(false);

        // toggleOptionsParent.getChildren().setAll(toggleOptionBrowse);
    }

}
