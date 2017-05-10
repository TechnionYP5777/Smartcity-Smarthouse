package il.ac.technion.cs.smarthouse.guitesting;


import org.junit.After;
import org.loadui.testfx.GuiTest;

import il.ac.technion.cs.smarthouse.sensors.sos.gui.SosSensorSimulator;
import il.ac.technion.cs.smarthouse.sensors.stove.gui.StoveSensorSimulator;
import il.ac.technion.cs.smarthouse.sensors.vitals.gui.VitalsSensorSimulator;
import il.ac.technion.cs.smarthouse.system.applications.api.SmartHouseApplication;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath;
import il.ac.technion.cs.smarthouse.system.applications.installer.ApplicationPath.PathType;
import il.ac.technion.cs.smarthouse.system.gui.main_system.MainSystemGui;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class InfastructureMainSystemGuiTest extends GuiTest {

    private MainSystemGui gui = new MainSystemGui();
    private SosSensorSimulator sosSim;
    private VitalsSensorSimulator vitalsSim;
    private StoveSensorSimulator stoveSim;

    @Override protected Parent getRootNode() {
        return this.gui.getRoot();
    }

    @After public void closeSystem() throws Exception {
        if (vitalsSim != null)
            vitalsSim.stop();
        if (sosSim != null)
            sosSim.stop();
        if (stoveSim != null)
            stoveSim.stop();
        gui.stop();
        gui.kill();
    }
    
    protected void installAppOnSystem(Class<? extends SmartHouseApplication> appClass) throws Exception {
        gui.getPresenter().getModel().applicationsHandler.addApplication(new ApplicationPath(PathType.CLASS_NAME, appClass.getName()));
        Thread.sleep(500);
    }
    
    protected void openVitalsSensor(){
        Platform.runLater(() -> {
            try {
                vitalsSim = new VitalsSensorSimulator();
                vitalsSim.start(new Stage());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }
    
    protected void openStoveSensor(){
        Platform.runLater(() -> {
            try {
                stoveSim = new StoveSensorSimulator();
                stoveSim.start(new Stage());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }
    
    protected void openSOSSensor(){
        Platform.runLater(() -> {
            try {
                sosSim = new SosSensorSimulator();
                sosSim.start(new Stage());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }
}
