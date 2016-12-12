package il.ac.technion.cs.eldery.system.applications;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;
import java.util.stream.*;

import il.ac.technion.cs.eldery.system.MainSystem;
import il.ac.technion.cs.eldery.system.applications.SmartHouseApplication;
import il.ac.technion.cs.eldery.system.exceptions.InstallerException;
import il.ac.technion.cs.eldery.utils.Generator;

/** This class handles installing and loading SmartHouseApplications
 * @author RON
 * @since 09-12-2016 */
public class AppInstaller {
    /** installs the application, and generates the ApplicationIdentifier for it
     * @param jarFilePath
     * @return the ApplicationIdentifier for the application */
    public static ApplicationIdentifier installApplication(final String jarFilePath) {
        // TODO: Ron and Roy - do we need to do more stuff here?
        return new ApplicationIdentifier(Generator.GenerateUniqueID() + "", jarFilePath);
    }

    /** Dynamically loads the classes from the jar file to the JVM. Finds the
     * one class that extends SmartHouseApplication, and returns an instance of
     * it.
     * @param i
     * @return an instance of SmartHouseApplication
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InstallerException
     * @throws IOException */
    public static SmartHouseApplication loadApplication(final ApplicationIdentifier i)
            throws InstantiationException, IllegalAccessException, InstallerException, IOException {
        String jarFilePath = i.getJarPath();
        final URL[] urls = { new URL("jar:file:" + jarFilePath + "!/") };
        try (URLClassLoader cl = URLClassLoader.newInstance(urls)) {
            return loadApplication_aux(getClassNamesFromJar(jarFilePath), cl);
        }
    }

    /** Dynamically loads the classes to the JVM. Finds the one class that
     * extends SmartHouseApplication, and returns an instance of it.
     * @param classNames
     * @return an instance of SmartHouseApplication
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InstallerException */
    public static SmartHouseApplication loadApplication(List<String> classNames)
            throws InstantiationException, IllegalAccessException, InstallerException {
        return loadApplication_aux(classNames, ClassLoader.getSystemClassLoader());
    }

    @SuppressWarnings("unused") private static SmartHouseApplication initiateApplication(SmartHouseApplication a, MainSystem s) {
        // TODO: Ron and Roy - ?
        a.setMainSystemInstance(s);
        a.onLoad();
        return a;
    }

    /** loads the classes with the ClassLoader and finds the class that extends
     * SmartHouseApplication, instantiates it, and returns it
     * @param classNames
     * @param l
     * @return an instance of SmartHouseApplication
     * @throws InstallerException
     * @throws InstantiationException
     * @throws IllegalAccessException */
    private static SmartHouseApplication loadApplication_aux(List<String> classNames, ClassLoader l)
            throws InstallerException, InstantiationException, IllegalAccessException {
        // TODO: Ron and Roy - do we really need to throw all of these
        // exceptions? maybe we should handle them here
        final List<Class<?>> applicationClasses = getClassesBySuperclass(loadAllClasses(l, classNames), SmartHouseApplication.class);
        if (applicationClasses.size() != 1)
            throw new InstallerException(InstallerException.MORE_THAN_ONE_IMPL, applicationClasses.size());
        return (SmartHouseApplication) applicationClasses.get(0).newInstance();
    }

    /** finds all the classes in cs that extend filterClass
     * @param cs
     * @param filterClass
     * @return a list of the filtered classes */
    private static List<Class<?>> getClassesBySuperclass(final List<Class<?>> cs, final Class<?> filterClass) {
        return cs.stream().filter((x) -> x.getSuperclass() == filterClass).collect(Collectors.toList());
    }

    /** loads the classes to the JVM using the given ClassLoader. if a class was
     * not found, the ClassNotFoundException's stack trace will be printed to
     * the stderr (the function will still try to load the other classes)
     * @param l
     * @param classNames
     * @return a list of the loaded classes */
    private static List<Class<?>> loadAllClasses(final ClassLoader l, final List<String> classNames) {
        final List<Class<?>> $ = new ArrayList<>();
        for (String className : classNames)
            try {
                $.add(l.loadClass(className));
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        return $;
    }

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
}
