package il.ac.technion.cs.smarthouse.system.applications.installer;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import il.ac.technion.cs.smarthouse.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.smarthouse.system.applications.installer.AppInstallHelper;
import il.ac.technion.cs.smarthouse.system.applications.installer.examples.MyTestClass1;
import il.ac.technion.cs.smarthouse.system.exceptions.AppInstallerException;

public class AppInstallHelperTest {
    private static String testClassesPathPrefix;

    private final List<String> classesNames_app1 = new ArrayList<>();
    private final List<String> classesNames_app2 = new ArrayList<>();
    private final List<String> classesNames_app3 = new ArrayList<>();
    private final List<String> classesNames_app4 = new ArrayList<>();

    @BeforeClass public static void setup_findPackageName() {
        testClassesPathPrefix = AppInstallHelperTest.class.getPackage().getName() + ".examples.MyTestClass";
    }

    private static String getTestClassName(final int index) {
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
            final SmartHouseApplication a = AppInstallHelper.loadApplication(classesNames_app1);
            assert a instanceof MyTestClass1;

            final MyTestClass1 t = (MyTestClass1) a;
            assert !t.isLoaded();
            t.onLoad();
            assert t.isLoaded();

        } catch (final AppInstallerException ¢) {
            Assert.fail(¢.getMessage());
        }
    }

    @Test public void testLoadBadApp_moreThanOneImplError() {
        try {
            AppInstallHelper.loadApplication(classesNames_app2);
        } catch (final AppInstallerException ¢) {
            Assert.assertEquals(¢.getErrorCode(), AppInstallerException.ErrorCode.MORE_THAN_ONE_IMPL_ERROR);
        }
    }

    @Test public void testLoadBadApp_noImplError() {
        try {
            AppInstallHelper.loadApplication(classesNames_app3);
        } catch (final AppInstallerException ¢) {
            Assert.assertEquals(¢.getErrorCode(), AppInstallerException.ErrorCode.NO_IMPL_ERROR);
        }
    }
}
