package il.ac.technion.cs.eldery.system.applications;

import java.io.IOException;

import com.google.gson.annotations.Expose;

import il.ac.technion.cs.eldery.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.eldery.system.applications.api.exceptions.OnLoadException;
import il.ac.technion.cs.eldery.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.eldery.system.exceptions.AppInstallerException;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/** A class that stores information about the installed application
 * @author RON
 * @since 09-12-2016 */
public class ApplicationManager {
    private String id;
    private final ApplicationPath<?> appPath;
    @Expose private ApplicationsHandler referenceToApplicationsHandler;
    @Expose private SmartHouseApplication application;

    @Expose private Node rootNode;

    public ApplicationManager(final String id, final ApplicationPath<?> appPath, final ApplicationsHandler referenceToApplicationsHandler) {
        this.id = id;
        this.appPath = appPath;
        this.referenceToApplicationsHandler = referenceToApplicationsHandler;
    }

    // [start] Public - Getters and Setters for the private params
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setReferenceToApplicationsHandler(final ApplicationsHandler referenceToApplicationsHandler) {
        this.referenceToApplicationsHandler = referenceToApplicationsHandler;
    }
    // [end]

    // [start] Public - Services to the ApplicationHandler
    /** Installs the jar file (by dynamically loading it to the system's
     * run-time). Initializes the SmartHouseApplication from the jar. This
     * function should be used once (when the application is installed or when
     * the system is initialized). If the application is already installed,
     * false will be returned and nothing will happen.
     * @return false if the application is already installed, and true otherwise
     * @throws IOException - If the jar file can't be found
     * @throws AppInstallerException - An installation error
     * @throws OnLoadException - An exception in the onLoad method */
    public boolean initialize() throws AppInstallerException, IOException, OnLoadException {
        if (application != null)
            return false;

        application = appPath.installMe();

        application.setApplicationsHandler(referenceToApplicationsHandler);
        application.onLoad();
        rootNode = application.getRootNode();

        return true;
    }

    /** If the application is installed, but currently closed, this will reopen
     * it. */
    @SuppressWarnings("boxing") public void reopen(final Pane parentPane) {
        if (application == null || rootNode == null)
            return;

        AnchorPane.setTopAnchor(rootNode, 0.0);
        AnchorPane.setRightAnchor(rootNode, 0.0);
        AnchorPane.setLeftAnchor(rootNode, 0.0);
        AnchorPane.setBottomAnchor(rootNode, 0.0);

        parentPane.getChildren().setAll(rootNode);
    }

    public String getApplicationName() {
        return application == null ? null : application.getApplicationName();
    }
    // [end]

    // [start] Overrides - hash-code and equals
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
    // [end]

    @Override public String toString() {
        return "ApplicationManager [id=" + id + ", path={" + appPath + "}]";
    }
}
