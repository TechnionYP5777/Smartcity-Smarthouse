package il.ac.technion.cs.smarthouse.system.gui.applications;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.mvp.system.SystemGuiController;
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

public class ApplicationsInstallerViewController extends SystemGuiController {
    private static Logger log = LoggerFactory.getLogger(ApplicationsInstallerViewController.class);

    @FXML private TextField browseText;
    @FXML private Button browseBtn;
    @FXML private Button installBtn;

    @Override
    public void initialize(final SystemCore model, final URL location, final ResourceBundle __) {
        initInstallBtn();
        initBrowseBtn();
    }

    // [start] Private - init FXML elements
    private void initInstallBtn() {
        installBtn.setOnAction(e -> {
            installApp(new ApplicationPath(PathType.JAR_PATH, browseText.getText()));
            this.<ApplicationViewController>getParentController().updateListView();
        });
    }

    private void initBrowseBtn() {
        browseBtn.setOnAction(e -> {
            final Stage stage = new Stage();
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            Optional.ofNullable(fileChooser.showOpenDialog(stage))
                            .ifPresent(a -> browseText.setText(a.getAbsolutePath()));
        });
    }
    // [end]
    
    private void installApp(final ApplicationPath p) {
        getModel().getSystemApplicationsHandler().addApplication(p);
    }

}
