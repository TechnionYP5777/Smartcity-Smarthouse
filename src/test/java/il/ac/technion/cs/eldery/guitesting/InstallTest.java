package il.ac.technion.cs.eldery.guitesting;

import org.junit.BeforeClass;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.utils.FXTestUtils;

import il.ac.technion.cs.eldery.system.SystemCore;
import javafx.scene.Parent;

public class InstallTest{
    private static GuiTest controller;
    @BeforeClass
    public static void setUpClass() {
        FXTestUtils.launchApp(SystemCore.class);
        controller = new GuiTest() {
            @Override
            protected Parent getRootNode() {
                return SystemCore.getStage().getScene().getRoot();
            }
        };
    }
    
    @SuppressWarnings("static-method")
    @Test
    public void verifyStuff(){
        controller.click();
    }

}
