package il.ac.technion.cs.smarthouse.guitesting;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.loadui.testfx.GuiTest;

import il.ac.technion.cs.smarthouse.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath.PathType;
import il.ac.technion.cs.smarthouse.system.main.SystemPresenter;
import il.ac.technion.cs.smarthouse.system.main.SystemPresenterFactory;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;
import javafx.application.Application;
import javafx.scene.Parent;

public class InfastructureMainSystemGuiTest extends GuiTest {

    private SystemPresenter gui;
    private List<Application> sensors = new ArrayList<>();

    @Override
    protected Parent getRootNode() {
        gui = new SystemPresenterFactory().setUseCloudServer(false)
                        .setRegularFileSystemListeners(false).setOpenOnNewStage(false).build();
        return gui.getSystemView().getRootViewNode();
    }

    @After
    public void closeSystem() {
        sensors.forEach(s -> {
            try {
                s.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    protected void installAppOnSystem(final Class<? extends SmartHouseApplication> appClass) throws Exception {
        gui.getSystemModel().getSystemApplicationsHandler()
                        .addApplication(new ApplicationPath(PathType.CLASS_NAME, appClass.getName()));
        Thread.sleep(500);
    }

    protected void openSensor(Class<? extends Application> sensorSimulator) {
        try {
            JavaFxHelper.startGui(sensorSimulator.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
