package il.ac.technion.cs.eldery.system.applications.installer.examples;

import il.ac.technion.cs.eldery.system.applications.api.SmartHouseApplication;

public class MyTestClass1 extends SmartHouseApplication {
    private boolean isLoaded;

    public MyTestClass1() {}

    @Override public void onLoad() {
        isLoaded = true;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    @Override public String getApplicationName() {
        return null;
    }

}
