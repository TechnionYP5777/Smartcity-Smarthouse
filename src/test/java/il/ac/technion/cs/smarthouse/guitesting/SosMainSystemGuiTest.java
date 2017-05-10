package il.ac.technion.cs.smarthouse.guitesting;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.utils.FXTestUtils;

import javafx.scene.control.ListView;
import javafx.stage.Stage;

import static org.loadui.testfx.Assertions.assertNodeExists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.applications.sos.SosAppGui;

public class SosMainSystemGuiTest extends InfastructureMainSystemGuiTest {

    private static Logger log = LoggerFactory.getLogger(SosMainSystemGuiTest.class);

    @Before public void openSensor() {
        openSOSSensor();
    }

    @Test public void testSosSensor() throws Exception {
        click("#appsTab");
        ListView<String> l = find("#listView");
        assert l.getItems().isEmpty();
        installAppOnSystem(SosAppGui.class);
        assertEquals(l.getItems().size(), 1);
        click("SOS Application");
        Stage stage1 = (Stage) find("#sosButton").getScene().getWindow();
        try {
            FXTestUtils.bringToFront(stage1);
        } catch (Exception e) {
            log.error("Unable to show stage", e);
        }
        click("#sosButton");
        assertNodeExists("#killerButton");
    }
}
