package il.ac.technion.cs.eldery.system.sensors_applications_integration_tests;

import java.io.IOException;

import il.ac.technion.cs.eldery.applications.vitals.VitalsApp;
import il.ac.technion.cs.eldery.sensors.vitals.gui.VitalsSensorSimulator;
import il.ac.technion.cs.eldery.system.EmergencyLevel;
import il.ac.technion.cs.eldery.system.userInformation.Contact;
import javafx.stage.Stage;

/** @author Yarden
 * @since 19.1.17 */
public class SystemCore_VitalsTest extends IntegrationAbstructTest {
    public static void main(final String[] args) {
        launch(args);
    }

    @Override public void start(final Stage primaryStage) {
        try {
            super.start(primaryStage, VitalsSensorSimulator.class, VitalsApp.class);
            initializeUser("user", "123456789", "050-1234567", "Technion");
            user.addContact(new Contact("contact", "234567890", "054-1234567", "smarthouse5777@gmail.com"), EmergencyLevel.EMAIL_EMERGENCY_CONTACT);
        } catch (final IOException ¢) {
            ¢.printStackTrace();
        }
    }
}
