package il.ac.technion.cs.eldery.system.gui.applications;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.layout.HBox;

import javafx.scene.control.TextField;

import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.ToggleSwitch;

import il.ac.technion.cs.eldery.system.applications.ApplicationsHandler;
import javafx.scene.layout.VBox;

public class ApplicationsInstallerViewController implements Initializable {
    @FXML private ToggleSwitch toggleBtn;
    @FXML private VBox toggleOptionsParent;
    @FXML private HBox toggleOptionBrowse;
    @FXML private TextField browseText;
    @FXML private Button browseBtn;
    @FXML private HBox toggleOptionCombo;
    @FXML private ComboBox comboBox;
    @FXML private Button installBtn;

    ApplicationsHandler applicationsHandler;

    @Override public void initialize(URL location, ResourceBundle __) {
        return;
    }

    public void init(ApplicationsHandler h) {
        applicationsHandler = h;
        
        initToggleBtn();

        gotoRegularMode();

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

    void gotoTestMode() {
        toggleBtn.setText("Test Mode");
        toggleOptionsParent.getChildren().setAll(toggleOptionCombo);
    }

    void gotoRegularMode() {
        toggleBtn.setText("Real Mode");
        toggleOptionsParent.getChildren().setAll(toggleOptionBrowse);
    }

}
