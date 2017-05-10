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

import il.ac.technion.cs.smarthouse.applications.stove.StoveModuleGui;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javafx.scene.paint.Color;

/** A Class to test the GUI aspects of the stove sensor and application.
 * @author Yarden
 * @since 3.5.17 */

public class StoveGuiTest extends InfastructureMainSystemGuiTest {

    private static Logger log = LoggerFactory.getLogger(StoveGuiTest.class);

    @Before public void openSensor() {
        openStoveSensor();
    }

    @Test public void testStoveSensor() throws Exception {
        click("#appsTab");
        ListView<String> l = find("#listView");
        assert l.getItems().isEmpty();
        installAppOnSystem(StoveModuleGui.class);
        assertEquals(l.getItems().size(), 1);
        click("Stove Application");
        Slider tempSlider = find("#tempSlider");
        Stage stage1 = (Stage) find("#tempLabelSensor").getScene().getWindow();
        try {
            FXTestUtils.bringToFront(stage1);
        } catch (Exception e) {
            log.error("Unable to show stage", e);
        }
        click("#onOffButton");
        click("#tempSlider");
        moveBy(15, 0);
        click();
        int temperature = (int) Math.round(tempSlider.getValue());
        assertEquals("Temperature: " + temperature, ((Label) find("#tempLabelSensor")).getText());
        assertEquals("The stove temperature is: " + temperature, ((Label) find("#tempLabel")).getText());
        assertEquals("The stove is running for:", ((Label) find("#timeLabel")).getText().substring(0, 25));

        TextFlow console = (TextFlow) find("#console");
        assertEquals("true", ((Text) console.getChildren().get(1)).getText());
        assertEquals(Integer.valueOf(temperature), Integer.valueOf(((Text) console.getChildren().get(3)).getText()));

        click("#onOffButton");
        assertEquals("The stove is: Off", ((Label) find("#timeLabel")).getText());
        assertEquals("false", ((Text) console.getChildren().get(1)).getText());
        assertEquals(Integer.valueOf(temperature), Integer.valueOf(((Text) console.getChildren().get(3)).getText()));
        
        click("#onOffButton");
        Stage stage2 = (Stage) find("#stoveConfigButton").getScene().getWindow();
        try {
            FXTestUtils.bringToFront(stage2);
        } catch (Exception e) {
            log.error("Unable to show stage", e);
        }
        click("#stoveConfigButton");
        click("#secs").type("10");
        click("#cels").type("85");
        click("#Apply");
        waitUntil((Label) find("#timeLabel"), (Label label) -> (label.getTextFill() == Color.RED), 60);
        try {
            FXTestUtils.bringToFront(stage1);
        } catch (Exception e) {
            log.error("Unable to show stage", e);
        }
        click("#tempSlider");
        moveBy(55, 0);
        click();
        assertEquals(Color.RED, ((Label) find("#tempLabel")).getTextFill());
        click("#tempSlider");
        assertEquals(Color.BLACK, ((Label) find("#tempLabel")).getTextFill());
        click("#onOffButton");
        click("#onOffButton");
        assertEquals(Color.BLACK, ((Label) find("#timeLabel")).getTextFill());
        click("#onOffButton");
    }
}