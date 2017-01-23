package il.ac.technion.cs.eldery.system;

import java.io.IOException;
import java.util.List;

import il.ac.technion.cs.eldery.system.applications.ApplicationsHandler;
import il.ac.technion.cs.eldery.system.gui.MainSystemGuiController;
import il.ac.technion.cs.eldery.system.sensors.SensorsHandler;
import il.ac.technion.cs.eldery.system.userInformation.Contact;
import il.ac.technion.cs.eldery.system.userInformation.UserInformation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Hold the databases of the smart house, and allow sensors and applications to
 * store and read information about the changes in the environment */
public class SystemCore extends Application {
    private static final String APP_NAME = "SmartHouse";

    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    protected final SensorsHandler sensorsHandler = new SensorsHandler(databaseHandler);
    protected final ApplicationsHandler applicationsHandler = new ApplicationsHandler(databaseHandler, this);
    protected UserInformation user;
    private boolean userInitialized;

    public static void main(final String[] args) {
        launch(args);
    }

    @Override public void start(final Stage ¢) throws IOException {
        initializeSystemComponents();
        initializeSystemGui(¢);
    }

    protected void initializeSystemGui(final Stage s) throws IOException {
        System.out.println("Initializing system ui...");

        final FXMLLoader loader = new FXMLLoader(getClass().getResource("gui/main_system_ui.fxml"));
        final Scene scene = new Scene(loader.load(), 1000, 800);
        MainSystemGuiController mainGuiController = (MainSystemGuiController) loader.getController();
        mainGuiController.setDatabaseHandler(databaseHandler);
        mainGuiController.setApplicationsHandler(applicationsHandler);
        mainGuiController.setSysCore(this);

        s.setTitle(APP_NAME);
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

    public void alert(final String msg, final EmergencyLevel elvl) {
        if(user == null) return;
        final List<Contact> $ = user.getContacts(elvl);
        switch (elvl) {
            case NOTIFY_ELDERLY:
                // TODO: how do we notify the elderly?
                break;
            case SMS_EMERGENCY_CONTACT:
                $.stream().forEach(c -> Communicate.throughSms(c.getPhoneNumber(), msg));
                break;
            case CALL_EMERGENCY_CONTACT:
            case CONTACT_HOSPITAL:
            case CONTACT_POLICE:
            case CONTACT_FIRE_FIGHTERS:
                $.stream().forEach(c -> Communicate.throughPhone(c.getPhoneNumber()));
                break;
            case EMAIL_EMERGENCY_CONTACT:
                $.stream().forEach(c -> Communicate.throughEmailFromHere(c.getEmailAddress(), msg));
                break;
            default:
                // TODO: what's the desired behavior?
                break;
        }
    }

}
