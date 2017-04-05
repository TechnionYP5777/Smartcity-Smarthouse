package il.ac.technion.cs.smarthouse.guitesting;


import static org.junit.Assert.*;

import org.junit.Test;
import org.loadui.testfx.GuiTest;

import il.ac.technion.cs.smarthouse.sensors.sos.gui.SosSensorSimulator;
import il.ac.technion.cs.smarthouse.system.gui.MainSystemGui;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class MainSystemGuiTests extends GuiTest{
        
    MainSystemGui gui = new MainSystemGui();
    
    @Override protected Parent getRootNode() {
        return this.gui.getRoot();
    }
    
    @Test
    public void testInstalation(){
        Platform.runLater(() -> {
            try {
                new SosSensorSimulator().start(new Stage());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        click("#appsTab");
        ListView<String> l = find("#listView");
        assertTrue(l.getItems().isEmpty());
        click("#plusButton");
        click("#toggleBtn");
        click("#comboBox");
        click("SOS Application");
        click("#installBtn");
        assertEquals(l.getItems().size(),1);
    }    
}
