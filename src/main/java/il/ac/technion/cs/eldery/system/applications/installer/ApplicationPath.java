package il.ac.technion.cs.eldery.system.applications.installer;

import java.io.IOException;
import java.util.List;

import il.ac.technion.cs.eldery.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.eldery.system.exceptions.AppInstallerException;

/** This class will store the path for the application's .jar file OR a list of
 * classes. This will help the debugging process.
 * @author RON
 * @since 30-12-2016 */
public class ApplicationPath<T> {
    public enum PathType {
        JAR_PATH,
        CLASS_NAMES_LIST,
        PACKAGE_NAME;
    }

    private final PathType pathType;
    private final T path;

    public ApplicationPath(final PathType pathType, final T path) {
        this.pathType = pathType;
        this.path = path;
    }

    public PathType getPathType() {
        return pathType;
    }

    public T getPath() {
        return path;
    }

    @SuppressWarnings("unchecked") public SmartHouseApplication installMe() throws AppInstallerException, IOException {
        switch (pathType) {
            case JAR_PATH:
                return AppInstallHelper.loadApplication((String) path);
            case CLASS_NAMES_LIST:
                return AppInstallHelper.loadApplication((List<String>) path);
            case PACKAGE_NAME:
                // TODO: RON - add support for package names...
                return null;
            default:
                return null;
        }
    }

    @Override public String toString() {
        return "ApplicationPath [type=" + pathType + ", path=" + path + "]";
    }
}
