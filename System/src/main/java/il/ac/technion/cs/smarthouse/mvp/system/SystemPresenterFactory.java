package il.ac.technion.cs.smarthouse.mvp.system;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.developers_api.SmarthouseApplication;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.SystemFailureDetector;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath.PathType;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;
import il.ac.technion.cs.smarthouse.utils.Tuple;

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
    private boolean disableFailureDetector;

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

        SystemFailureDetector.enable();
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
