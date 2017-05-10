package il.ac.technion.cs.smarthouse;

import il.ac.technion.cs.smarthouse.sensors.sos.gui.SosSensorSimulator;
import il.ac.technion.cs.smarthouse.system.gui.main_system.MainSystemGui;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;

public class Entry {

    public static void main(String[] args) throws InterruptedException {
        JavaFxHelper.startGui(new MainSystemGui(), args);
        Thread.sleep(500);
        JavaFxHelper.startGui(new SosSensorSimulator(), args);
    }

}
