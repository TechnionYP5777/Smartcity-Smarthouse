package il.ac.technion.cs.smarthouse.system.applications.installer.examples;

import il.ac.technion.cs.smarthouse.developers_api.SmarthouseApplication;

/**
 * @author RON
 * @since 14-12-2016
 */
public class MyTestClass1 extends SmarthouseApplication {
    private boolean isLoaded;

    public MyTestClass1() {}

    @Override
    public void onLoad() {
        isLoaded = true;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    @Override
    public String getApplicationName() {
        return null;
    }

}
