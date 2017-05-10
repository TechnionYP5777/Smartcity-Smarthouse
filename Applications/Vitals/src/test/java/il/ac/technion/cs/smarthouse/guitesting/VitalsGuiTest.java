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

import il.ac.technion.cs.smarthouse.applications.vitals.VitalsApp;
import javafx.scene.layout.VBox;
import org.controlsfx.control.RangeSlider;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/** A Class to test the GUI aspects of the vitals sensor and application.
 * @author Yarden
 * @since 30.4.17 */

public class VitalsGuiTest extends InfastructureMainSystemGuiTest {

    private static Logger log = LoggerFactory.getLogger(VitalsGuiTest.class);

    @Before public void openSensor() {
        openVitalsSensor();
    }

    @Test public void testVitalsSensor() throws Exception {
        click("#appsTab");
        ListView<String> l = find("#listView");
        assert l.getItems().isEmpty();
        installAppOnSystem(VitalsApp.class);
        assertEquals(l.getItems().size(), 1);
        click("Vitals Application");
        Slider pulseSlider = find("#pulseSlider");
        Stage stage1 = (Stage) find("#pulseLabelSensor").getScene().getWindow();
        try {
            FXTestUtils.bringToFront(stage1);
        } catch (Exception e) {
            log.error("Unable to show stage", e);
        }
        click("#pulseSlider");
        moveBy(15, 0);
        click();
        int pulse = (int) Math.round(pulseSlider.getValue());
        assertEquals("Pulse: " + pulse, ((Label) find("#pulseLabelSensor")).getText());
        assertEquals("Pulse: " + pulse, ((Label) find("#pulseLabel")).getText());
        VBox mainVBox = (VBox) find("#mainVBox");
        RangeSlider bpRSlider = (RangeSlider) mainVBox.getChildren().get(4);
        click(bpRSlider);
        moveBy(70, 0);
        click();
        int systolicBP = (int) Math.round(bpRSlider.getHighValue());
        moveBy(-140, 0);
        click();
        int diastolicBP = (int) Math.round(bpRSlider.getLowValue());
        assertEquals("Blood Pressure: " + systolicBP + "/" + diastolicBP, ((Label) find("#bpLabelSensor")).getText());
        assertEquals("Blood Pressure: " + systolicBP + "/" + diastolicBP, ((Label) find("#bpLabel")).getText());
        TextFlow console = (TextFlow) find("#console");
        assertEquals(Integer.valueOf(pulse), Integer.valueOf(((Text) console.getChildren().get(1)).getText()));
        assertEquals(Integer.valueOf(systolicBP), Integer.valueOf(((Text) console.getChildren().get(3)).getText()));
        assertEquals(Integer.valueOf(diastolicBP), Integer.valueOf(((Text) console.getChildren().get(5)).getText()));
    }
}
