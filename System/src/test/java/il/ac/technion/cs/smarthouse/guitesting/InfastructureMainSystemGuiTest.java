package il.ac.technion.cs.smarthouse.guitesting;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.loadui.testfx.GuiTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.developers_api.SmarthouseApplication;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath.PathType;
import il.ac.technion.cs.smarthouse.system_presenter.SystemPresenter;
import il.ac.technion.cs.smarthouse.system_presenter.SystemPresenterFactory;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;
import javafx.application.Application;
import javafx.scene.Parent;

/**
 * @author roysh
 * @since 29-04-2017
 */
public class InfastructureMainSystemGuiTest extends GuiTest {

    private static Logger log = LoggerFactory.getLogger(InfastructureMainSystemGuiTest.class);

    private SystemPresenter gui;
    private List<Application> sensors = new ArrayList<>();

    @Override
    protected Parent getRootNode() {
        gui = new SystemPresenterFactory().setUseCloudServer(false).setRegularFileSystemListeners(false)
                        .setOpenOnNewStage(false).build();
        return gui.getSystemView().getRootViewNode();
    }

    @After
    public void closeSystem() {
        sensors.forEach(s -> {
            try {
                s.stop();
            } catch (Exception e) {
                log.error("Closing the system errored", e);
            }
        });
    }

    protected void installAppOnSystem(final Class<? extends SmarthouseApplication> appClass) throws Exception {
        gui.getSystemModel().getSystemApplicationsHandler()
                        .addApplication(new ApplicationPath(PathType.CLASS_NAME, appClass.getName()));
        Thread.sleep(500);
    }

    protected void openSensor(Class<? extends Application> sensorSimulator) {
        try {
            JavaFxHelper.startGui(sensorSimulator.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("Opening a sensor failed", e);
        }
    }
}
