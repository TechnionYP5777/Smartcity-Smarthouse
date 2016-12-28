package il.ac.technion.cs.eldery.system.applications.examples;

import il.ac.technion.cs.eldery.system.applications.api.SmartHouseApplication;
import javafx.stage.Stage;

public class MyTestClass1 extends SmartHouseApplication {
    private boolean isLoaded;
    private boolean isStarted;

    public MyTestClass1() {}

    @Override public void onLoad() {
        isLoaded = true;
    }

    @Override public void start(final Stage primaryStage) throws Exception {
        isStarted = true;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public boolean isStarted() {
        return isStarted;
    }

}
