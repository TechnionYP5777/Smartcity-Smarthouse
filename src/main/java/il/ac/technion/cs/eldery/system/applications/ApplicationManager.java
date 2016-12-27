package il.ac.technion.cs.eldery.system.applications;

import java.io.IOException;

import com.google.gson.annotations.Expose;

import il.ac.technion.cs.eldery.system.SystemCore;
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
    @Expose private SystemCore referenceToSystemCore;
    @Expose private SmartHouseApplication application;

    public ApplicationManager(final String id, final String jarPath) {
        this.id = id;
        this.jarPath = jarPath;
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

    public void setReferenceToSystemCore(SystemCore referenceToSystemCore) {
        this.referenceToSystemCore = referenceToSystemCore;
    }

    public boolean initialize() throws AppInstallerException, IOException {
        if (application != null)
            return false;

        application = AppInstallHelper.loadApplication(jarPath);
        application.setSystemCoreInstance(referenceToSystemCore);
        application.onLoad();

        Platform.runLater(() -> {
            try {
                application.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return true;
    }

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
        return "ApplicationIdentifier [id=" + id + ", path=" + jarPath + "]";
    }
}
