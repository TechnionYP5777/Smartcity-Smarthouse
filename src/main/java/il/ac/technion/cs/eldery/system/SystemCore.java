package il.ac.technion.cs.eldery.system;

import java.io.IOException;
import java.util.List;

import il.ac.technion.cs.eldery.system.applications.ApplicationsHandler;
import il.ac.technion.cs.eldery.system.gui.mapping.MappingController;
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
    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    protected final SensorsHandler sensorsHandler = new SensorsHandler(databaseHandler);
    protected final ApplicationsHandler applicationsHandler = new ApplicationsHandler(databaseHandler, this);
    private UserInformation user;
    private boolean userInitialized;

    public static void main(final String[] args) {
        launch(args);
    }

    @Override public void start(final Stage s) throws IOException {
        System.out.println("Initializing system...");
        new Thread(sensorsHandler).start();

        final FXMLLoader loader = new FXMLLoader(getClass().getResource("gui/main_system_ui.fxml"));

        final Scene scene = new Scene(loader.load(), 1000, 800);
        s.setTitle("Test");
        s.setScene(scene);
        s.show();
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
        // todo: add appId field
        final List<Contact> $ = user.getContacts(elvl);
        switch (elvl) {
            case NOTIFY_ELDERLY:
                // todo: how do we notify the eldery?
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
                // todo: whats the desired behaviour?
                break;
        }
    }

}
