package il.ac.technion.cs.smarthouse.guitesting;


import static org.junit.Assert.*;

import org.junit.Test;
import org.loadui.testfx.GuiTest;

import il.ac.technion.cs.smarthouse.system.gui.MainSystemGui;
import javafx.scene.Parent;
import javafx.scene.control.ListView;

public class MainSystemGuiTests extends GuiTest{
    
    MainSystemGui gui = new MainSystemGui();
    
    
    @Override protected Parent getRootNode() {
        return this.gui.getRoot();
    }
    
    @Test
    public void testListEmpty(){
        click("#appsTab");
        ListView<String> l = find("#listView");
        assertTrue(l.getItems().isEmpty());
        click("#plusButton");
        click("#toggleBtn");
        click("#comboBox");
    }
    
}
