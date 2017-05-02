package il.ac.technion.cs.smarthouse.guitesting;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.utils.FXTestUtils;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** A Class to test the GUI aspects of the vitals sensor and application.
 * @author Yarden
 * @since 30.4.17 */

public class VitalsGuiTest extends InfastructureMainSystemGuiTest {

    private static Logger log = LoggerFactory.getLogger(VitalsGuiTest.class);

    @Before public void openSensor() {
        openVitalsSensor();
    }

    @Test public void testVitalsSensor() {
        click("#appsTab");
        ListView<String> l = find("#listView");
        assert l.getItems().isEmpty();
        click("#plusButton");
        click("#toggleBtn");
        click("#comboBox");
        click("Vitals Application");
        click("#installBtn");
        assertEquals(l.getItems().size(), 1);
        click("Vitals Application");
        Slider pulseSlider = find("#pulseSlider");
        // target("Vitals Sensor Simulator").
        Stage stage1 = (Stage) find("#pulseLabelSensor").getScene().getWindow();
        try {
            FXTestUtils.bringToFront(stage1);
        } catch (Exception e) {
            log.error("Unable to show stage", e);
        }
        click("#pulseLabelSensor");
        click("#pulseSlider");
        moveBy(25, 0);
        click();
        Label pulseLabel = find("#pulseLabel");
        assertEquals("Pulse: " + (int) Math.round(pulseSlider.getValue()), pulseLabel.getText());
        // click("#sosButton");
        // assertNodeExists("#killerButton");
    }
}
