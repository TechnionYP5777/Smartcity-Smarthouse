package il.ac.technion.cs.smarthouse.system.applications.installer;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.smarthouse.system.exceptions.AppInstallerException;

/** This class contains static functions that handle loading
 * SmartHouseApplications using ClassLoaders
 * @author RON
 * @since 09-12-2016 */
public class AppInstallHelper {

    private static Logger log = LoggerFactory.getLogger(AppInstallHelper.class);

    // [start] Public - loadApplication functions
    /** Dynamically loads the classes from the jar file to the JVM. Finds the
     * one class that extends SmartHouseApplication, and returns an instance of
     * it.
     * @param jarFilePath - a path to the .jar file
     * @return an instance of SmartHouseApplication
     * @throws AppInstallerException
     * @throws IOException */
    public static SmartHouseApplication loadApplication(final String jarFilePath) throws AppInstallerException, IOException {
        final URL[] urls = { new URL("jar:file:" + jarFilePath + "!/") };
        try (URLClassLoader $ = URLClassLoader.newInstance(urls)) {
            return loadApplication_aux(getClassNamesFromJar(jarFilePath), $);
        }
    }

    /** Dynamically loads the classes to the JVM. Finds the one class that
     * extends SmartHouseApplication, and returns an instance of it.
     * @param classesNames
     * @return an instance of SmartHouseApplication
     * @throws AppInstallerException */
    public static SmartHouseApplication loadApplication(final List<String> classesNames) throws AppInstallerException {
        return loadApplication_aux(classesNames, ClassLoader.getSystemClassLoader());
    }
    // [end]

    // [start] Private - loadApplication aux
    /** loads the classes with the ClassLoader and finds the class that extends
     * SmartHouseApplication, instantiates it, and returns it
     * @param classNames
     * @param l - the ClassLoader
     * @return an instance of SmartHouseApplication
     * @throws AppInstallerException */
    private static SmartHouseApplication loadApplication_aux(final List<String> classNames, final ClassLoader l) throws AppInstallerException {
        final List<Class<?>> $ = getClassesBySuperclass(loadAllClasses(l, classNames), SmartHouseApplication.class);
        if ($.isEmpty())
            throw new AppInstallerException(AppInstallerException.ErrorCode.NO_IMPL_ERROR);
        if ($.size() > 1)
            throw new AppInstallerException(AppInstallerException.ErrorCode.MORE_THAN_ONE_IMPL_ERROR, "number of classes that extend is " + $.size());
        try {
            return (SmartHouseApplication) $.get(0).newInstance();
        } catch (final InstantiationException ¢) {
            log.error("Instantiation error occured", ¢);
            throw new AppInstallerException(AppInstallerException.ErrorCode.INSTANTIATION_ERROR, ¢.getMessage());
        } catch (final IllegalAccessException ¢) {
            log.error("Illegal access occured", ¢);
            throw new AppInstallerException(AppInstallerException.ErrorCode.ILLEGAL_ACCESS_ERROR, ¢.getMessage());
        }
    }
    // [end]

    // [start] Private - load from classes names list
    /** finds all the classes in cs that extend filterClass
     * @param cs
     * @param filterClass
     * @return a list of the filtered classes */
    private static List<Class<?>> getClassesBySuperclass(final List<Class<?>> cs, final Class<?> filterClass) {
        return cs.stream().filter(x -> x.getSuperclass() == filterClass).collect(Collectors.toList());
    }

    /** loads the classes to the JVM using the given ClassLoader. if a class was
     * not found, the ClassNotFoundException's stack trace will be printed to
     * the stderr (the function will still try to load the other classes)
     * @param l
     * @param classNames
     * @return a list of the loaded classes */
    private static List<Class<?>> loadAllClasses(final ClassLoader l, final List<String> classNames) {
        final List<Class<?>> $ = new ArrayList<>();
        for (final String className : classNames)
            try {
                $.add(l.loadClass(className));
            } catch (final ClassNotFoundException e1) {
                log.error("Class was not found", e1);
            }
        return $;
    }
    // [end]

    // [start] Private - jar to classes names list
    /** opens the jar file, and returns the names of its classes
     * @param jarFilePath
     * @return a list of class names from the jar file
     * @throws IOException - if the jar file can't be read */
    private static List<String> getClassNamesFromJar(final String jarFilePath) throws IOException {
        final List<String> $ = new ArrayList<>();
        try (JarFile jarFile = new JarFile(jarFilePath)) {
            for (final Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements();) {
                final JarEntry je = e.nextElement();
                if (je.isDirectory() || !je.getName().endsWith(".class"))
                    continue;
                $.add(getClassNameFromJarEntry(je));
            }
        }
        return $;
    }

    /** a JarEntry's name looks like: "com/classes/App.class". This function
     * converts the name to a proper class name for the ClassLoader:
     * "com.classes.App"
     * @param $
     * @return a proper class name */
    private static String getClassNameFromJarEntry(final JarEntry $) {
        return $.getName().substring(0, $.getName().length() - 6).replace('/', '.');
    }
    // [end]
}
