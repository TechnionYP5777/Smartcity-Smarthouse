package il.ac.technion.cs.eldery.system.applications;

import il.ac.technion.cs.eldery.system.MainSystem;
import il.ac.technion.cs.eldery.utils.Generator;

/** A class that stores information about the installed application
 * @author RON
 * @since 09-12-2016 */
// TODO: RON and ROY - implement this class. Should this class store an instance
// of the SmartHouseApplication
public class ApplicationManager {
    String id;
    String jarPath;

    public ApplicationManager(String id, String jarPath) {
        this.id = id;
        this.jarPath = jarPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJarPath() {
        return jarPath;
    }

    public void setJarPath(String path) {
        this.jarPath = path;
    }

    /** installs the application, and generates the ApplicationIdentifier for it
     * @param jarFilePath
     * @return the ApplicationIdentifier for the application */
    public static ApplicationManager installApplication(final String jarFilePath) {
        // TODO: Ron and Roy - do we need to do more stuff here?
        return new ApplicationManager(Generator.GenerateUniqueID() + "", jarFilePath);
    }

    @SuppressWarnings("unused") private static SmartHouseApplication initiateApplication(SmartHouseApplication a, MainSystem s) {
        // TODO: Ron and Roy - ?
        a.setMainSystemInstance(s);
        a.onLoad();
        return a;
    }

    @Override public int hashCode() {
        return 31 * (((id == null) ? 0 : id.hashCode()) + 31) + ((jarPath == null) ? 0 : jarPath.hashCode());
    }

    @Override public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ApplicationManager other = (ApplicationManager) o;
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
