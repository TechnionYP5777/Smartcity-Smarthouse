package il.ac.technion.cs.eldery.applications.installer;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import il.ac.technion.cs.eldery.applications.BaseApplication;
import il.ac.technion.cs.eldery.applications.installer.exceptions.InstallerException;

/**
 * @author RON
 * @since 09-12-16
 */
public class AppInstaller {
  /**
   * Dynamically loads the classes in the jar file to the JVM.
   * Finds the one class that extends BaseApplication, and returns an instance of it.
   * 
   * @param jarFilePath
   * @return BaseApplication instance from the jar file
   * @throws IOException
   * @throws ClassNotFoundException
   * @throws InstallerException - if there is more than one class in the jar that extends BaseApplication
   * @throws InstantiationException
   * @throws IllegalAccessException
   */
  public static BaseApplication install(String jarFilePath) throws IOException, ClassNotFoundException, InstallerException, InstantiationException, IllegalAccessException {
    URL[] urls = { new URL("jar:file:" + jarFilePath + "!/") };
    
    try(JarFile jarFile = new JarFile(jarFilePath); URLClassLoader cl = URLClassLoader.newInstance(urls)) {
      Enumeration<JarEntry> e = jarFile.entries();
      
      List<Class<?>> baseAppClasses = getClassBySuperclass(loadAllClasses(cl, e), BaseApplication.class);
      
      if (baseAppClasses.size() != 1)
        throw new InstallerException(InstallerException.MORE_THAN_ONE_IMPL, baseAppClasses.size());
      
      return (BaseApplication) baseAppClasses.get(0).newInstance();
    }
  }
  
  private static List<Class<?>> getClassBySuperclass(List<Class<?>> cs, Class<?> filterClass) {
    return cs.stream().filter((x) -> x.getSuperclass() == filterClass).collect(Collectors.toList());
  }

  private static List<Class<?>> loadAllClasses(ClassLoader l, Enumeration<JarEntry> e) throws ClassNotFoundException {
    List<Class<?>> $ = new ArrayList<>();
    while (e.hasMoreElements()) {
      JarEntry je = e.nextElement();
      if (je.isDirectory() || !je.getName().endsWith(".class"))
        continue;
      String className = getClassName(je);
      $.add(l.loadClass(className));
    }
    return $;
  }

  private static String getClassName(JarEntry ¢) {
    return ¢.getName().substring(0, ¢.getName().length() - 6).replace('/', '.');
  }
}
