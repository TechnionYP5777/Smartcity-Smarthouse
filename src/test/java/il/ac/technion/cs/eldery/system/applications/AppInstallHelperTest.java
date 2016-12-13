package il.ac.technion.cs.eldery.system.applications;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;

import il.ac.technion.cs.eldery.system.exceptions.AppInstallerException;

public class AppInstallHelperTest {
    private static String examplesPackageName;
    private List<String> classesNames_app1;

    @BeforeClass public static void setup_findPackageName() {
        examplesPackageName = AppInstallHelperTest.class.getPackage().getName() + ".examples";
    }

    @Before public void setup_classesNames() {
        classesNames_app1 = new ArrayList<>();
        classesNames_app1.add(examplesPackageName + ".MyTestApp");
    }

    @Test public void testLoadApplication() {
        try {
            AppInstallHelper.loadApplication(classesNames_app1);
        } catch (AppInstallerException e) {
            Assert.fail(e.getMessage());
        }
    }
}
