package il.ac.technion.cs.smarthouse.system.gui.applications;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.mvp.SystemPresenter;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath.PathType;
import il.ac.technion.cs.smarthouse.system.exceptions.AppInstallerException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ApplicationsInstallerViewController extends SystemPresenter {
    private static Logger log = LoggerFactory.getLogger(ApplicationsInstallerViewController.class);
    
    @FXML private TextField browseText;
    @FXML private Button browseBtn;
    @FXML private Button installBtn;

    @Override public void init(SystemCore model, URL location, ResourceBundle __) {
        initInstallBtn();
        initBrowseBtn();
    }

    // [start] Private - init FXML elements
    private void initInstallBtn() {
        installBtn.setOnAction(e -> {
            installApp(new ApplicationPath(PathType.JAR_PATH, browseText.getText()));
            this.<ApplicationViewController>getParentPresenter().updateListView();
        });
    }

    private void initBrowseBtn() {
        browseBtn.setOnAction(e -> {
            final Stage stage = new Stage();
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            Optional.ofNullable(fileChooser.showOpenDialog(stage)).ifPresent(a->browseText.setText(a.getAbsolutePath()));
        });
    }
    // [end]

    // [start] install app and alert if needed
    private void installApp(final ApplicationPath p) {
        try {
            getModel().applicationsHandler.addApplication(p);
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

}
