package il.ac.technion.cs.eldery.system.applications;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;
import java.util.stream.*;

import il.ac.technion.cs.eldery.system.applications.SmartHouseApplication;
import il.ac.technion.cs.eldery.system.exceptions.InstallerException;

/** @author RON
 * @since 09-12-16 */
public class AppInstaller {
    /** Dynamically loads the classes in the jar file to the JVM. Finds the one
     * class that extends BaseApplication, and returns an instance of it.
     * @param jarFilePath
     * @return BaseApplication instance from the jar file
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InstallerException - if there is more than one class in the jar
     *         that extends BaseApplication
     * @throws InstantiationException
     * @throws IllegalAccessException */
    public static SmartHouseApplication install(final String jarFilePath)
            throws IOException, ClassNotFoundException, InstallerException, InstantiationException, IllegalAccessException {
        final URL[] urls = { new URL("jar:file:" + jarFilePath + "!/") };
        try (JarFile jarFile = new JarFile(jarFilePath); URLClassLoader cl = URLClassLoader.newInstance(urls)) {
            final Enumeration<JarEntry> e = jarFile.entries();
            final List<Class<?>> baseAppClasses = getClassBySuperclass(loadAllClasses(cl, e), SmartHouseApplication.class);
            if (baseAppClasses.size() != 1)
                throw new InstallerException(InstallerException.MORE_THAN_ONE_IMPL, baseAppClasses.size());
            return (SmartHouseApplication) baseAppClasses.get(0).newInstance();
        }
    }

    private static List<Class<?>> getClassBySuperclass(final List<Class<?>> cs, final Class<?> filterClass) {
        return cs.stream().filter((x) -> x.getSuperclass() == filterClass).collect(Collectors.toList());
    }

    private static List<Class<?>> loadAllClasses(final ClassLoader l, final Enumeration<JarEntry> e) throws ClassNotFoundException {
        final List<Class<?>> $ = new ArrayList<>();
        while (e.hasMoreElements()) {
            final JarEntry je = e.nextElement();
            if (je.isDirectory() || !je.getName().endsWith(".class"))
                continue;
            final String className = getClassName(je);
            $.add(l.loadClass(className));
        }
        return $;
    }

    private static String getClassName(final JarEntry ¢) {
        return ¢.getName().substring(0, ¢.getName().length() - 6).replace('/', '.');
    }
}
