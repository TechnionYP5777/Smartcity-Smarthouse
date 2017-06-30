package il.ac.technion.cs.smarthouse.developers_api;

import java.util.Optional;

import il.ac.technion.cs.smarthouse.DeveloperSimulator.DeveloperSimulatorGui;
import il.ac.technion.cs.smarthouse.developers_api.application_builder.AppBuilder;
import il.ac.technion.cs.smarthouse.developers_api.application_builder.implementations.AppBuilderImpl;
import il.ac.technion.cs.smarthouse.developers_api.application_builder.implementations.WidgetsRegionBuilderImpl;
import il.ac.technion.cs.smarthouse.sensors.simulator.SensorsSimulator;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.SystemMode;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath.PathType;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.file_system.PathBuilder;
import il.ac.technion.cs.smarthouse.system.services.Service;
import il.ac.technion.cs.smarthouse.system.services.ServiceType;
import il.ac.technion.cs.smarthouse.system_presenter.SystemPresenter;
import il.ac.technion.cs.smarthouse.system_presenter.SystemPresenterFactory;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;
import javafx.scene.Parent;

/**
 * The API for the application developers.
 * <p>
 * Every application that wants to be installed on the system, MUST extend this
 * class.
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

    /**
     * a static launch function for testing. You can use it the main function in
     * order to run the house with the application installed on it.
     * 
     * @param simluator
     * @param showSimulatorGui
     * @throws Exception
     */
    public static void launch(final SensorsSimulator simluator, final Boolean showSimulatorGui) throws Exception {
        final SystemPresenter p = new SystemPresenterFactory().setUseCloudServer(false)
                        .setRegularFileSystemListeners(false)
                        .addApplicationToInstall(new ApplicationPath(PathType.CLASS_NAME,
                                        new Throwable().getStackTrace()[1].getClassName()))
                        .initMode(SystemMode.DEVELOPER_MODE).enableModePopup(false).build();

        Optional.ofNullable(simluator).ifPresent(s -> new Thread() {

            @Override
            public void interrupt() {
                s.stopSendingMsgsInAllSensors();
                super.interrupt();
            }

            @Override
            public void run() {
                s.startSendingMsgsInAllSensors();
                if (showSimulatorGui)
                    JavaFxHelper.startGui(new DeveloperSimulatorGui().setSimulator(s));
                super.run();
            }

        }.start());

        p.getSystemView().gotoAppsTab();
    }

    // [start] Package - Services to the ApplicationMetaData
    /**
     * Only for system use! {@link ApplicationMetaData} should use this function
     * to pass parameters to the {@link SmarthouseApplication}
     * 
     * @param $
     *            a reference to the {@link SystemCore}
     * @param applicationId
     *            the ID of this application
     * @return this object
     */
    final SmarthouseApplication setDataFromApplicationMetaData(final SystemCore $, final String applicationId) {
        if (systemCore != null)
            return this;

        systemCore = $;

        this.applicationId = applicationId;

        ((WidgetsRegionBuilderImpl) getAppBuilder().getWidgetsRegionBuilder())
                        .setDashboardCore(systemCore.getSystemDashboardCore());

        return this;
    }

    /**
     * Only for system use! {@link ApplicationMetaData} should use this function
     * to build the application's GUI
     * 
     * @return the GUI handler
     */
    final Parent buildLayout() {
        return appBuilder.build();
    }
    // [end]

    // [start] Public - Services to application developers
    /**
     * Get the {@link AppBuilder} object in order to set your application's GUI
     * 
     * @return the application's {@link AppBuilder} object
     */
    public AppBuilder getAppBuilder() {
        return appBuilder;
    }

    /**
     * Get a service from the system
     * 
     * @param $
     * @return a system service
     */
    public <T extends Service> T getService(final ServiceType $) {
        return systemCore.getSystemServiceManager().getService($);
    }

    /**
     * Save application data to the system's fileSystem.
     * 
     * @param data
     *            the data to save
     * @param path
     *            the path to save it on
     */
    public final void saveApplicationData(final Object data, final String... path) {
        assert applicationId != null;
        systemCore.getFileSystem().sendMessage(data,
                        FileSystemEntries.APPLICATIONS_DATA.buildPath(applicationId, PathBuilder.buildPath(path)));
    }

    /**
     * Loads application data from the system's fileSystem.
     * 
     * @param path
     *            the path the data is saved on.
     * @return the data that was stored on the path, or null if there was no
     *         data on that path.
     */
    public final <T> T readApplicationData(final String... path) {
        assert applicationId != null;
        return systemCore.getFileSystem().getData(
                        FileSystemEntries.APPLICATIONS_DATA.buildPath(applicationId, PathBuilder.buildPath(path)));
    }
    // [end]

    // [start] Public abstract - the developer must implement
    /**
     * This will run when the system loads the application. Here you can call
     * various system services and define the look of the application's GUI
     * (with {@link #getAppBuilder()}
     */
    public abstract void onLoad() throws Exception;

    /**
     * Implement this function! It defines the name of your application.
     * 
     * @return the application's name
     */
    public abstract String getApplicationName();
    // [end]
}
