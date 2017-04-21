package il.ac.technion.cs.smarthouse.system.applications;

import java.io.IOException;

import com.google.gson.annotations.Expose;

import il.ac.technion.cs.smarthouse.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.smarthouse.system.exceptions.AppInstallerException;
import il.ac.technion.cs.smarthouse.system.services.ServiceManager;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/** A class that stores information about the installed application
 * @author RON
 * @since 09-12-2016 */
public class ApplicationManager {
    @Expose private String id;
    @Expose private final ApplicationPath appPath;
    private SmartHouseApplication application;

    private Node rootNode;

    public ApplicationManager(final String id, final ApplicationPath appPath) {
        this.id = id;
        this.appPath = appPath;
    }

    // [start] Public - Getters and Setters for the private params
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
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
     * @throws Exception - An exception in the onLoad method */
    public boolean initialize(ServiceManager $) throws Exception {
        if (application != null)
            return false;

        application = appPath.installMe();

        application.setServiceManager($);
        application.onLoad();
        rootNode = application.getRootNode();

        return true;
    }

    /** If the application is installed, but currently closed, this will reopen
     * it. */
    public void reopen(final Pane parentPane) {
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
