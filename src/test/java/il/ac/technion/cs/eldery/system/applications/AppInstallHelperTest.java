package il.ac.technion.cs.eldery.system.applications;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;

import il.ac.technion.cs.eldery.system.exceptions.AppInstallerException;

public class AppInstallHelperTest {
    private static String testClassesPathPrefix;

    private List<String> classesNames_app1 = new ArrayList<>();
    private List<String> classesNames_app2 = new ArrayList<>();
    private List<String> classesNames_app3 = new ArrayList<>();
    private List<String> classesNames_app4 = new ArrayList<>();

    @BeforeClass public static void setup_findPackageName() {
        testClassesPathPrefix = AppInstallHelperTest.class.getPackage().getName() + ".examples.MyTestClass";
    }

    private static String getTestClassName(int index) {
        return testClassesPathPrefix + index;
    }

    @Before public void setup_classesNames() {
        classesNames_app1.add(getTestClassName(1));
        classesNames_app1.add(getTestClassName(3));

        classesNames_app2.add(getTestClassName(1));
        classesNames_app2.add(getTestClassName(2));

        classesNames_app3.add(getTestClassName(3));

        classesNames_app4.add("NotAClass");
    }

    @Test public void testLoadGoodApp() {
        try {
            AppInstallHelper.loadApplication(classesNames_app1);
        } catch (AppInstallerException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test public void testLoadBadApp() {
        try {
            AppInstallHelper.loadApplication(classesNames_app2);
        } catch (AppInstallerException e) {
            Assert.assertEquals(e.getValue(), 2);
        }
    }
}
