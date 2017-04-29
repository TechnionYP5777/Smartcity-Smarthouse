package il.ac.technion.cs.smarthouse.guitesting;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import javafx.scene.control.ListView;
import static org.loadui.testfx.Assertions.assertNodeExists;

public class SosMainSystemGuiTest extends InfastructureMainSystemGuiTest{
    
    @Before public void openSensor(){
        openSOSSensor();
    }
    
    @Test public void testSosSensor() {
        click("#appsTab");
        ListView<String> l = find("#listView");
        assert l.getItems().isEmpty();
        click("#plusButton");
        click("#toggleBtn");
        click("#comboBox");
        click("SOS Application");
        click("#installBtn");
        assertEquals(l.getItems().size(), 1);
        click("SOS Application");
        click("#sosButton");
        assertNodeExists("#killerButton");
        return;
    }
}
