package il.ac.technion.cs.eldery.system.applications;

import java.io.IOException;

import com.google.gson.annotations.Expose;

import il.ac.technion.cs.eldery.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.eldery.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.eldery.system.exceptions.AppInstallerException;
import javafx.application.Platform;
import javafx.stage.Stage;

/** A class that stores information about the installed application
 * @author RON
 * @since 09-12-2016 */
public class ApplicationManager {
    private String id;
    private final ApplicationPath<?> appPath;
    @Expose private ApplicationsHandler referenceToApplicationsHandler;
    @Expose private SmartHouseApplication application;

    public ApplicationManager(final String id, final ApplicationPath<?> appPath, final ApplicationsHandler referenceToApplicationsHandler) {
        this.id = id;
        this.appPath = appPath;
        this.referenceToApplicationsHandler = referenceToApplicationsHandler;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setReferenceToApplicationsHandler(final ApplicationsHandler referenceToApplicationsHandler) {
        this.referenceToApplicationsHandler = referenceToApplicationsHandler;
    }

    /** @return the path to the application's icon */
    public String getIcon() {
        // TODO: 4Ron - might not be string at some point later
        return application.getIcon();
    }

    /** Installs the jar file (by dynamically loading it to the system's
     * run-time). Initializes the SmartHouseApplication from the jar. This
     * function should be used once (when the application is installed or when
     * the system is initialized). If the application is already installed,
     * false will be returned and nothing will happen.
     * @return false if the application is already installed, and true otherwise
     * @throws IOException - If the jar file can't be found
     * @throws AppInstallerException - An installation error */
    public boolean initialize() throws AppInstallerException, IOException {
        if (application != null)
            return false;

        application = appPath.installMe();

        application.setApplicationsHandler(referenceToApplicationsHandler);
        application.onLoad();

        Platform.runLater(() -> {
            try {
                application.start(new Stage());
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });

        return true;
    }

    /** If the application is installed, but currently open (for display), this
     * will minimize it. */
    public void minimize() {
        application.minimize();
    }

    /** If the application is installed, but currently closed, this will reopen
     * it. */
    public void reopen() {
        if (application != null)
            application.reopen();
    }

    @Override public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override public boolean equals(final Object o) {
        if (o == this)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final ApplicationManager other = (ApplicationManager) o;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override public String toString() {
        return "ApplicationManager [id=" + id + ", path={" + appPath + "}]";
    }
}
