package il.ac.technion.cs.smarthouse.mvp.system;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.developers_api.SmarthouseApplication;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath.PathType;
import il.ac.technion.cs.smarthouse.utils.Communicate;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;
import il.ac.technion.cs.smarthouse.utils.Tuple;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A factory for {@link SystemPresenter}
 * 
 * TODO: at the moment it is very ugly... will be fixed later
 * 
 * @author RON
 * @since 07-06-2017
 */
public class SystemPresenterFactory {
    static final Logger log = LoggerFactory.getLogger(SystemPresenterFactory.class);

    private boolean model_useCloudServer = true;
    private boolean model_useSensorsServer = true;
    private List<Tuple<BiConsumer<String, Object>, String[]>> model_fileSystemListeners = new ArrayList<>();
    private boolean model_initRegularFileSystemListeners = true;
    private List<String> model_applicationsToInstall = new ArrayList<>();
    private boolean view_enableView = true;
    private List<Runnable> view_onCloseListeners = new ArrayList<>();
    private boolean view_openOnNewStage = true;
    boolean disableFailureDetector = false;

    public SystemPresenterFactory addFileSystemSubscriber(final BiConsumer<String, Object> eventHandler,
                    final String... path) {
        model_fileSystemListeners.add(new Tuple<>(eventHandler, path));
        return this;
    }

    public SystemPresenterFactory addOnGuiCloseListener(final Runnable onGuiCloseListner) {
        view_onCloseListeners.add(onGuiCloseListner);
        return this;
    }

    public SystemPresenterFactory setUseCloudServer(final boolean useServer) {
        model_useCloudServer = useServer;
        return this;
    }

    public SystemPresenterFactory setUseSensorsServer(final boolean useServer) {
        model_useCloudServer = useServer;
        return this;
    }

    public SystemPresenterFactory setRegularFileSystemListeners(final boolean set) {
        model_initRegularFileSystemListeners = set;
        return this;
    }

    public SystemPresenterFactory setEnableGui(final boolean enableGui) {
        view_enableView = enableGui;
        return this;
    }

    public SystemPresenterFactory addApplicationToInstall(
                    final Class<? extends SmarthouseApplication> applicationClass) {
        model_applicationsToInstall.add(applicationClass.getName());
        return this;
    }

    public SystemPresenterFactory addApplicationToInstall(final String applicationClassName) {
        model_applicationsToInstall.add(applicationClassName);
        return this;
    }

    public SystemPresenterFactory setOpenOnNewStage(final boolean openOnNewStage) {
        view_openOnNewStage = openOnNewStage;
        return this;
    }

    public SystemPresenterFactory disableFailureDetector(final boolean disable) {
        disableFailureDetector = disable;
        return this;
    }

    private void setFailureDetector() {
        if (disableFailureDetector)
            return;

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            final String errTxt = "Uncaught exception from thread [" + t.getName() + "]\n\nException:\n" + e.toString();
            log.error(errTxt, e);
            if (JavaFxHelper.isJavaFxThreadStarted())
                JavaFxHelper.surroundConsumerWithFx(p -> {
                    ButtonType reportType = new ButtonType("Send Report", ButtonBar.ButtonData.LEFT);
                    Alert a = new Alert(AlertType.ERROR, errTxt, reportType, ButtonType.CLOSE);
                    a.setTitle("Uncaught exception");
                    a.showAndWait().ifPresent(response -> {
                        if (response == reportType) {
                            Stage dialogStage = new Stage();
                            dialogStage.initModality(Modality.WINDOW_MODAL);
                            TextArea userInput = new TextArea();
                            Text text = new Text(
                                            "Please describe what happened when the error occurred (optional):");
                            Button reportButton = new Button("Send");
                            reportButton.setOnMouseClicked(event -> {
                                String input = userInput.getText();
                                StringWriter sw = new StringWriter();
                                PrintWriter pw = new PrintWriter(sw);
                                e.printStackTrace(pw);
                                Communicate.throughEmailFromHere("smarthouse5777@gmail.com",
                                                "We got new report...\n\n" + errTxt + "\n\n"
                                                                + (input == null || "".equals(input)
                                                                                ? "The user did not add a description of the error."
                                                                                : "User Description:\n" + input)  + "\n\nStack Trace:\n" +
                                                                                sw.toString());
                                ((Stage) reportButton.getScene().getWindow()).close();
                            });
                            Pane pane1 = new Pane();
                            Pane pane2 = new Pane();
                            HBox hbox1 = new HBox(text, pane1);
                            hbox1.setPadding(new Insets(0, 0, 5, 0));
                            HBox hbox2 = new HBox(pane2, reportButton);
                            hbox2.setPadding(new Insets(5, 0, 0, 0));
                            HBox.setHgrow(pane1, Priority.ALWAYS);
                            HBox.setHgrow(pane2, Priority.ALWAYS);
                            VBox vbox = new VBox(hbox1, userInput, hbox2);
                            VBox.setVgrow(userInput, Priority.ALWAYS);
                            vbox.setPadding(new Insets(15));

                            dialogStage.setScene(new Scene(vbox));
                            dialogStage.showAndWait();
                        }
                        System.exit(-1);
                    });
                }).accept(null);
        });
    }

    public SystemPresenter build() {
        setFailureDetector();

        SystemPresenter p = new SystemPresenter();
        p.model = new SystemCore();

        model_fileSystemListeners.forEach(t -> p.model.getFileSystem().subscribe(t.left, t.right));

        if (view_enableView) {

            p.viewOnCloseListeners.addAll(view_onCloseListeners);
            p.viewOnCloseListeners.add(() -> p.model.shutdown());
            p.viewOnCloseListeners.add(() -> System.exit(0));

            if (view_openOnNewStage)
                JavaFxHelper.startGui(p.new MainSystemGui());
            else
                p.viewController = SystemGuiController.createRootController(SystemPresenter.APP_ROOT_FXML, p.model);
        }

        model_applicationsToInstall.forEach(clsName -> {
            try {
                p.model.getSystemApplicationsHandler()
                                .addApplication(new ApplicationPath(PathType.CLASS_NAME, clsName));
            } catch (Exception e) {
                log.error("Can't install the application " + clsName + " on the system", e);
            }
        });

        p.model.initializeSystemComponents(model_useSensorsServer, model_useCloudServer,
                        model_initRegularFileSystemListeners);

        p.viewController.waitUntilInitFinishes();

        return p;
    }
}
