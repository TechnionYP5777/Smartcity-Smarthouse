package il.ac.technion.cs.smarthouse.system.gui.applications;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import il.ac.technion.cs.smarthouse.mvp.system.SystemGuiController;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath.PathType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

@Deprecated
public class ApplicationsInstallerViewController extends SystemGuiController {
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
