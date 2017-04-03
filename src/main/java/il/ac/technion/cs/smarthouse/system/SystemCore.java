package il.ac.technion.cs.smarthouse.system;

import java.io.IOException;

import il.ac.technion.cs.smarthouse.system.applications.ApplicationsCore;
import il.ac.technion.cs.smarthouse.system.gui.MainSystemGuiController;
import il.ac.technion.cs.smarthouse.system.sensors.SensorsHandler;
import il.ac.technion.cs.smarthouse.system.services.ServiceManager;
import il.ac.technion.cs.smarthouse.system.user_information.UserInformation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/** Hold the databases of the smart house, and allow sensors and applications to
 * store and read information about the changes in the environment */
public class SystemCore extends Application {
    private static final String APP_NAME = "SmartHouse";
    
    public final ServiceManager serviceManager = new ServiceManager(this);
    public final DatabaseHandler databaseHandler = new DatabaseHandler();
    public final SensorsHandler sensorsHandler = new SensorsHandler(databaseHandler);
    protected final ApplicationsCore applicationsHandler = new ApplicationsCore(this);
    protected UserInformation user;
    private boolean userInitialized;
    private static Stage stage;

    public static void main(final String[] args) {
        launch(args);
    }

    @Override public void start(final Stage ¢) throws IOException {
        stage = ¢;
        initializeSystemComponents();
        initializeSystemGui(¢);
    }

    protected void initializeSystemGui(final Stage s) throws IOException {
        System.out.println("Initializing system ui...");

        final FXMLLoader loader = new FXMLLoader(getClass().getResource("gui/main_system_ui.fxml"));
        final Scene scene = new Scene(loader.load(), 1000, 800);
        final MainSystemGuiController mainGuiController = (MainSystemGuiController) loader.getController();
        mainGuiController.setDatabaseHandler(databaseHandler);
        mainGuiController.setApplicationsHandler(applicationsHandler);
        mainGuiController.setSysCore(this);

        s.setTitle(APP_NAME);
        s.getIcons().add(new Image(getClass().getResourceAsStream("gui/house-icon.png")));
        s.setScene(scene);
        s.show();

        s.setOnCloseRequest(e -> {
            System.out.println("System closing...");
            // TODO: close other threads from here, or just do this:
            System.exit(0);
        });
    }

    protected void initializeSystemComponents() {
        System.out.println("Initializing system components...");
        new Thread(sensorsHandler).start();
    }

    public UserInformation getUser() {
        return user;
    }

    public void initializeUser(final String name, final String id, final String phoneNumber, final String homeAddress) {
        user = new UserInformation(name, id, phoneNumber, homeAddress);
        userInitialized = true;
    }

    public boolean isUserInitialized() {
        return userInitialized;
    }
    
    public static Stage getStage(){
        return stage;
    }

}
