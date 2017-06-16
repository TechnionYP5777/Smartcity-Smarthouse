package il.ac.technion.cs.smarthouse.developers_api;

import il.ac.technion.cs.smarthouse.developers_api.application_builder.AppBuilder;
import il.ac.technion.cs.smarthouse.developers_api.application_builder.implementations.AppBuilderImpl;
import il.ac.technion.cs.smarthouse.mvp.system.SystemMode;
import il.ac.technion.cs.smarthouse.mvp.system.SystemPresenter;
import il.ac.technion.cs.smarthouse.mvp.system.SystemPresenterFactory;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.file_system.PathBuilder;
import il.ac.technion.cs.smarthouse.system.services.Service;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;
import javafx.application.Application;
import javafx.scene.Parent;

/**
 * The API for the application developers.
 * <p>
 * Every application that wants to be installed
 * on the system, MUST extend this class.
 * 
 * @author RON
 * @author roysh
 * @author Elia Traore
 * @since 8.12.2016
 */
public abstract class SmarthouseApplication {
    private SystemCore systemCore;
    private String applicationId;
    private AppBuilderImpl appBuilder = new AppBuilderImpl(getClass().getClassLoader());

    public SmarthouseApplication() {}

    @SafeVarargs
    public static void launch(final Class<? extends Application>... sensors) throws Exception {
        final SystemPresenter p = new SystemPresenterFactory()
                        .setUseCloudServer(false)
                        .setRegularFileSystemListeners(false)
                        .addApplicationToInstall(new Throwable().getStackTrace()[1].getClassName())
                        .initMode(SystemMode.DEVELOPER_MODE)
                        .enableModePopup(false)
                        .build();

        for (final Class<? extends Application> s : sensors)
            JavaFxHelper.startGui(s.newInstance());

        p.getSystemView().gotoAppsTab();
    }

    // [start] Public - Services to the SystemCore
    final SmarthouseApplication setDataFromApplicationManager(final SystemCore $, final String applicationId) {
        if (systemCore != null)
            return this;

        systemCore = $;

        this.applicationId = applicationId;

        return this;
    }
    
    final Parent buildLayout() {
        return appBuilder.build();
    }
    // [end]

    // [start] Public - Services to application developers
    public AppBuilder getAppBuilder() {
        return appBuilder;
    }

    /**
     * Get a service from the system
     * 
     * @param $
     * @return
     */
    public <T extends Service> T getService(final ServiceType $) {
        return systemCore.getSystemServiceManager().getService($);
    }

    /**
     * Saves the app's data to the system's database
     * 
     * @param data
     * @return true if the data was saved to the system, or false otherwise
     */
    public final void saveApplicationData(final Object data, final String... path) {
        assert applicationId != null;
        systemCore.getFileSystem().sendMessage(data,
                        FileSystemEntries.APPLICATIONS_DATA.buildPath(applicationId, PathBuilder.buildPath(path)));
    }

    /**
     * Loads the app's data from the system's database
     * 
     * @return a string with the data
     */
    public final <T> T readApplicationData(final String... path) {
        assert applicationId != null;
        return systemCore.getFileSystem().getData(
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
