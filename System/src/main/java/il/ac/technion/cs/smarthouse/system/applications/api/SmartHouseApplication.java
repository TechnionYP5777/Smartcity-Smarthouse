package il.ac.technion.cs.smarthouse.system.applications.api;

import java.net.URL;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath.PathType;
import il.ac.technion.cs.smarthouse.system.gui.main_system.MainSystemGui;
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

	private ServiceManager serviceManager;
	private Node rootNode;

	public SmartHouseApplication() {
	}

	@SafeVarargs
	public static void launch(final Class<? extends Application>... sensors) throws Exception {
		final MainSystemGui m = new MainSystemGui();
		m.addOnKillListener(() -> System.exit(0));
		JavaFxHelper.startGui(m);

		m.getPresenter().waitUntilLoaded();

		for (final Class<? extends Application> s : sensors)
			JavaFxHelper.startGui(s.newInstance());

		Thread.sleep(1500);

		m.getPresenter().getModel().applicationsHandler.addApplication(
				new ApplicationPath(PathType.CLASS_NAME, new Throwable().getStackTrace()[1].getClassName()));

		m.getPresenter().gotoAppsTab();
	}

	// [start] Public - Services to the SystemCore
	public final SmartHouseApplication setServiceManager(final ServiceManager $) {
		serviceManager = $;
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
		log.info("Creating FXML for app: " + getApplicationName() + " (" + getClass().getName() + ") from: " + url);
		return fxmlLoader;
	}

	/**
	 * Get a service from the system
	 * 
	 * @param $
	 * @return
	 */
	public Service getService(final ServiceType $) {
		return serviceManager.getService($);
	}

	/**
	 * Saves the app's data to the system's database
	 * 
	 * @param data
	 * @return true if the data was saved to the system, or false otherwise
	 */
	public final boolean saveToDatabase(final String data) {
		log.warn("This function is not implemented yet");
		return data != null;
	}

	/**
	 * Loads the app's data from the system's database
	 * 
	 * @return a string with the data
	 */
	public final String loadFromDatabase() {
		log.warn("This function is not implemented yet");
		return null;
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
