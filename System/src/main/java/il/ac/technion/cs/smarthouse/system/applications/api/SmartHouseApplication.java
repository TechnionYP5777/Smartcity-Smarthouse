package il.ac.technion.cs.smarthouse.system.applications.api;

import java.net.URL;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.file_system.PathBuilder;
import il.ac.technion.cs.smarthouse.system.gui.main_system.MainSystemGuiController;
import il.ac.technion.cs.smarthouse.system.main.SystemPresenter;
import il.ac.technion.cs.smarthouse.system.main.SystemPresenterFactory;
import il.ac.technion.cs.smarthouse.system.services.Service;
import il.ac.technion.cs.smarthouse.system.services.ServiceManager;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;

/**
 * The API for the apps/modules developers Every app that wants to be installed
 * on the system, MUST extend this class
 * 
 * @author RON
 * @author roysh
 * @author Elia Traore
 * @since 8.12.2016
 */
public abstract class SmartHouseApplication {
    private static Logger log = LoggerFactory.getLogger(SmartHouseApplication.class);

    private SystemCore systemCore;
    private ServiceManager serviceManager;
    private FileSystem fileSystem;
    private String applicationId;
    private Node rootNode;

    public SmartHouseApplication() {}

    @SafeVarargs
    public static void launch(final Class<? extends Application>... sensors) throws Exception {
        final SystemPresenter p = new SystemPresenterFactory()
                        .setUseCloudServer(false)
                        .setRegularFileSystemListeners(false)
                        .addApplicationToInstall(new Throwable().getStackTrace()[1].getClassName())
                        .build();

        for (final Class<? extends Application> s : sensors)
            JavaFxHelper.startGui(s.newInstance());

        p.<MainSystemGuiController>getSystemView().gotoAppsTab();
    }

    // [start] Public - Services to the SystemCore
    public final SmartHouseApplication setDataFromApplicationManager(final SystemCore $, final String applicationId) {
        if (systemCore != null)
            return this;

        systemCore = $;
        serviceManager = systemCore.getSystemServiceManager();
        fileSystem = systemCore.getFileSystem();

        this.applicationId = applicationId;

        return this;
    }

    public final Node getRootNode() {
        return rootNode;
    }
    // [end]

    // [start] Public - Services to application developers
    /**
     * Set the fxml file that will be used
     * 
     * @param location
     *            of the fxml file
     * @return
     */
    public <T extends Initializable> T setContentView(final String fxmlFileName) {
        try {
            final FXMLLoader fxmlLoader = createFXMLLoader(fxmlFileName);
            fxmlLoader.setClassLoader(getClass().getClassLoader());
            rootNode = fxmlLoader.load();
            return fxmlLoader.getController();
        } catch (final Exception e) {
            rootNode = null;
            log.error("Couldn't load the application's fxml. The rootNode is null", e);
        }
        return null;
    }

    public URL getResource(final String resourcePath) {
        return Optional.ofNullable(getClass().getClassLoader().getResource(resourcePath))
                        .orElse(getClass().getResource(resourcePath));
    }

    public FXMLLoader createFXMLLoader(final String fxmlFileName) {
        final URL url = getResource(fxmlFileName);
        final FXMLLoader fxmlLoader = new FXMLLoader(url);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        log.info("Creating FXML for app \"" + getApplicationName() + "\" (" + getClass().getName() + ") from: " + url);
        return fxmlLoader;
    }

    /**
     * Get a service from the system
     * 
     * @param $
     * @return
     */
    public <T extends Service> T getService(final ServiceType $) {
        return serviceManager.getService($);
    }

    /**
     * Saves the app's data to the system's database
     * 
     * @param data
     * @return true if the data was saved to the system, or false otherwise
     */
    public final void saveApplicationData(final Object data, final String... path) {
        assert applicationId != null;
        fileSystem.sendMessage(data,
                        FileSystemEntries.APPLICATIONS_DATA.buildPath(applicationId, PathBuilder.buildPath(path)));
    }

    /**
     * Loads the app's data from the system's database
     * 
     * @return a string with the data
     */
    public final <T> T readApplicationData(final String... path) {
        assert applicationId != null;
        return fileSystem.getData(
                        FileSystemEntries.APPLICATIONS_DATA.buildPath(applicationId, PathBuilder.buildPath(path)));
    }
    // [end]

    // [start] Public abstract - the developer must implement
    /**
     * This will run when the system loads the app. Here all of the sensors
     * subscriptions must occur
     */
    public abstract void onLoad() throws Exception;

    public abstract String getApplicationName();
    // [end]
}
