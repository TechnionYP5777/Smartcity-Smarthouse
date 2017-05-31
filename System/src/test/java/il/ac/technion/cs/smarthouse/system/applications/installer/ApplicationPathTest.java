package il.ac.technion.cs.smarthouse.system.applications.installer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath.PathType;
import il.ac.technion.cs.smarthouse.system.exceptions.AppInstallerException;

public class ApplicationPathTest {
    private static String APP1_CLASSPATH = ApplicationPathTest.class.getPackage().getName() + ".examples.MyTestClass1";
    
    @Test
    public void mainTest() throws AppInstallerException, IOException {
        ApplicationPath p = new ApplicationPath(PathType.CLASS_NAME, APP1_CLASSPATH);
        
        Assert.assertEquals(p.getPath(), APP1_CLASSPATH);
        Assert.assertEquals(p.getPathType(), PathType.CLASS_NAME);
        
        assert p.installMe() != null;
        
        assert (new ApplicationPath(PathType.CLASS_NAMES_LIST, Arrays.asList(APP1_CLASSPATH))).installMe() != null;
        Assert.assertNull((new ApplicationPath(PathType.PACKAGE_NAME, APP1_CLASSPATH)).installMe());
    }
    
    @Test(expected = FileNotFoundException.class)
    public void jarTypeTest() throws AppInstallerException, IOException {
        (new ApplicationPath(PathType.JAR_PATH, APP1_CLASSPATH)).installMe();
    }
}
