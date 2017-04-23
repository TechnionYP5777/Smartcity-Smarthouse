package il.ac.technion.cs.smarthouse.system.gui.applications;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.applications.PremadeApplications;
import il.ac.technion.cs.smarthouse.system.applications.ApplicationsCore;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath.PathType;
import il.ac.technion.cs.smarthouse.system.exceptions.AppInstallerException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ApplicationsInstallerViewController implements Initializable {
    private static Logger log = LoggerFactory.getLogger(ApplicationsInstallerViewController.class);
    
    @FXML private Button toggleBtn;
    @FXML private VBox toggleOptionsParent;
    @FXML private HBox toggleOptionBrowse;
    @FXML private TextField browseText;
    @FXML private Button browseBtn;
    @FXML private HBox toggleOptionCombo;
    @FXML private ComboBox<String> comboBox;
    @FXML private Button installBtn;

    private ApplicationViewController applicationViewController;
    private ApplicationsCore applicationsHandler;
    boolean inRealMode;

    @Override public void initialize(final URL location, final ResourceBundle __) {
        initToggleBtn();
        initComboBox();
        initInstallBtn();
        initBrowseBtn();
        gotoRegularMode();
    }

    // [start] Public - set params
    public void setApplicationsHandler(final ApplicationsCore ¢) {
        applicationsHandler = ¢;
    }

    public void setApplicationViewController(final ApplicationViewController ¢) {
        applicationViewController = ¢;
    }
    // [end]

    // [start] Private - init FXML elements
    private void initToggleBtn() {
        toggleBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override public void handle(ActionEvent __) {
                if (ApplicationsInstallerViewController.this.inRealMode)
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
                installApp(new ApplicationPath(PathType.JAR_PATH, browseText.getText()));
            else if (comboBox.getValue() != null)
                installApp(new ApplicationPath(PathType.CLASS_NAME, PremadeApplications.getByName(comboBox.getValue()).getAppClass().getName()));

            applicationViewController.updateListView();
        });
    }

    private void initBrowseBtn() {
        browseBtn.setOnAction(e -> {
            final Stage stage = new Stage();
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            browseText.setText(fileChooser.showOpenDialog(stage).getAbsolutePath());
        });
    }
    // [end]

    // [start] install app and alert if needed
    private void installApp(final ApplicationPath p) {
        try {
            applicationsHandler.addApplication(p);
            if (!inRealMode)
                comboBox.getItems().remove(comboBox.getValue());
        } catch (AppInstallerException $) {
            log.debug("An exception while installing: " + p, $);
            alert("Installer Error: " + $.getMessage());
        } catch (final IOException $) {
            log.debug("An exception while installing: " + p, $);
            alert("IO Error: " + $.getMessage());
        } catch (final Exception $) {
            log.debug("An exception while installing: " + p, $);
            alert($.getClass().getName() + ": " + $.getMessage());
        }
    }

    private static void alert(final String messege) {
        final Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("ALERT");
        alert.setHeaderText("Installation Error");
        alert.setContentText(messege);
        alert.show();
    }
    // [end]

    // [start] set mode functions
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
    // [end]
}
