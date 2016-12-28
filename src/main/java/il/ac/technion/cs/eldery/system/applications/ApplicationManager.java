package il.ac.technion.cs.eldery.system.applications;

import java.io.IOException;

import com.google.gson.annotations.Expose;

import il.ac.technion.cs.eldery.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.eldery.system.exceptions.AppInstallerException;
import javafx.application.Platform;
import javafx.stage.Stage;

/** A class that stores information about the installed application
 * @author RON
 * @since 09-12-2016 */
// TODO: RON and ROY - implement this class.
public class ApplicationManager {
    private String id;
    private String jarPath;
    @Expose private ApplicationsHandler referenceToApplicationsHandler;
    @Expose private SmartHouseApplication application;

    public ApplicationManager(final String id, final String jarPath, final ApplicationsHandler referenceToApplicationsHandler) {
        this.id = id;
        this.jarPath = jarPath;
        this.referenceToApplicationsHandler = referenceToApplicationsHandler;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getJarPath() {
        return jarPath;
    }

    public void setJarPath(final String path) {
        jarPath = path;
    }

    public void setReferenceToApplicationsHandler(final ApplicationsHandler referenceToApplicationsHandler) {
        this.referenceToApplicationsHandler = referenceToApplicationsHandler;
    }
    
    /**
     * @return the path to the application's icon
     */
    public String getIcon() {
        //TODO: 4Ron - might not be string at some point later
        return application.getIcon();
    }

    /**
     * Installs the jar file (by dynamically loading it to the system's run-time).
     * Initializes the SmartHouseApplication from the jar.
     * 
     * This function should be used once (when the application is installed or when the system is initialized).
     * 
     * If the application is already installed, false will be returned and nothing will happen.
     * If the jar file can't be found, or doesn't contain the correct classes, false will be returned and nothing will happen. 
     * 
     * @return true if initialization was successful, false otherwise
     */
    public boolean initialize() {
        if (application != null)
            return false;

        try {
            application = AppInstallHelper.loadApplication(jarPath);
        } catch (AppInstallerException | IOException e1) {
            // TODO Do something better here
            e1.printStackTrace();
        }

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

    /** If the application is installed, but currently open (for display), this will minimize it.
     * */
    public void minimize(){
        //TODO: RON implement
    }
    
    /** If the application is installed, but currently closed, this will reopen it.
     */
    public void reopen() {
        if (application != null)
            application.reopen();
    }

    @Override public int hashCode() {
        return 31 * ((id == null ? 0 : id.hashCode()) + 31) + (jarPath == null ? 0 : jarPath.hashCode());
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
        if (jarPath == null) {
            if (other.jarPath != null)
                return false;
        } else if (!jarPath.equals(other.jarPath))
            return false;
        return true;
    }

    @Override public String toString() {
        return "ApplicationManager [id=" + id + ", path=" + jarPath + "]";
    }
}
