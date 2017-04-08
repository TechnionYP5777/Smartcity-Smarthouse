package il.ac.technion.cs.smarthouse.guitesting;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import il.ac.technion.cs.smarthouse.sensors.sos.gui.SosSensorSimulator;
import il.ac.technion.cs.smarthouse.system.gui.MainSystemGui;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class MainSystemGuiTest extends GuiTest {

    private MainSystemGui gui = new MainSystemGui();
    private SosSensorSimulator sosSim;

    @Override protected Parent getRootNode() {
        return this.gui.getRoot();
    }

    @After public void closeSystem() throws Exception {
        Thread.sleep(500);
        if (sosSim != null)
            sosSim.stop();
        gui.kill();
        gui.stop();
        Thread.sleep(1000);
    }

    @Ignore("Read issue #151 for details") @Test public void testInstalation() throws InterruptedException {
        Platform.runLater(() -> {
            try {
                sosSim = new SosSensorSimulator();
                sosSim.start(new Stage());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        Thread.sleep(500);
        click("#appsTab");
        ListView<String> l = find("#listView");
        assert l.getItems().isEmpty();
        click("#plusButton");
        click("#toggleBtn");
        click("#comboBox");
        click("SOS Application");
        click("#installBtn");
        assertEquals(l.getItems().size(), 1);
    }
}
