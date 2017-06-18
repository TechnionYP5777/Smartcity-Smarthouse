package il.ac.technion.cs.smarthouse.developers_api;

import java.io.IOException;

import com.google.gson.annotations.Expose;

import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.applications.installer.AppInstallerException;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * A class that stores information about the installed application
 * 
 * @author RON
 * @since 09-12-2016
 */
public final class ApplicationMetaData {
    @Expose private String id;
    @Expose private final ApplicationPath appPath;
    private SmarthouseApplication application;

    private Node rootNode;

    public ApplicationMetaData(final String id, final ApplicationPath appPath) {
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
    /**
     * Installs the jar file (by dynamically loading it to the system's
     * run-time). Initializes the SmartHouseApplication from the jar. This
     * function should be used once (when the application is installed or when
     * the system is initialized). If the application is already installed,
     * false will be returned and nothing will happen.
     * 
     * @return false if the application is already installed, and true otherwise
     * @throws IOException
     *             - If the jar file can't be found
     * @throws AppInstallerException
     *             - An installation error
     * @throws Exception
     *             - An exception in the onLoad method
     */
    public boolean initialize(final SystemCore $) throws Exception {
        if (application != null || $ == null)
            return false;

        application = appPath.installMe();

        application.setDataFromApplicationManager($, id);
        application.onLoad();
        rootNode = application.buildLayout();

        return true;
    }

    /**
     * If the application is installed, but currently closed, this will reopen
     * it.
     */
    public void reopen(final Pane parentPane) {
        if (parentPane != null && application != null && rootNode != null)
            JavaFxHelper.placeNodeInPane(rootNode, parentPane);
    }

    public String getApplicationName() {
        return application == null ? "Application_" + id : application.getApplicationName();
    }
    // [end]

    // [start] Overrides - hash-code and equals
    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final ApplicationMetaData other = (ApplicationMetaData) o;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    // [end]

    @Override
    public String toString() {
        return "ApplicationManager [id=" + id + ", path={" + appPath + "}]";
    }
}
