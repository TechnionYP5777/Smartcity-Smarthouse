package il.ac.technion.cs.smarthouse.system.applications.installer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.Expose;

import il.ac.technion.cs.smarthouse.developers_api.SmarthouseApplication;

/**
 * This class will store the path for the application's .jar file OR a list of
 * classes. This will help the debugging process.
 * 
 * @author RON
 * @since 30-12-2016
 */
public class ApplicationPath {
    private static Logger log = LoggerFactory.getLogger(ApplicationPath.class);

    public enum PathType {
        JAR_PATH,
        CLASS_NAME,
        CLASS_NAMES_LIST,
        PACKAGE_NAME,
        CLASS;
    }

    @Expose private final PathType pathType;
    @Expose private final Object path;

    public ApplicationPath(final PathType pathType, final Object path) {
        this.pathType = pathType;
        this.path = path;
    }

    public PathType getPathType() {
        return pathType;
    }

    public Object getPath() {
        return path;
    }

    @SuppressWarnings("unchecked")
    public SmarthouseApplication installMe() throws AppInstallerException, IOException {
        switch (pathType) {
            case CLASS:
                try {
                    return ((Class<? extends SmarthouseApplication>)path).newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error("\n\tPathType " + pathType + " is not an option");
                    return null;
                }
            case JAR_PATH:
                return AppInstallHelper.loadApplication((String) path);
            case CLASS_NAMES_LIST:
                return AppInstallHelper.loadApplication((List<String>) path);
            case CLASS_NAME:
                return AppInstallHelper.loadApplication(Arrays.asList((String) path));
            case PACKAGE_NAME:
                // TODO: we can add support for package names here if we want...
                log.error("\n\tPathType " + pathType + " is not implemented");
                return null;
            default:
                log.error("\n\tPathType " + pathType + " is not an option");
                return null;
        }
    }

    @Override
    public String toString() {
        return "ApplicationPath [type=" + pathType + ", path=" + path + "]";
    }
}
