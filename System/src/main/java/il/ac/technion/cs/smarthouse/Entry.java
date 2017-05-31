package il.ac.technion.cs.smarthouse;

import il.ac.technion.cs.smarthouse.sensors.sos.gui.SosSensorSimulator;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.gui.main_system.MainSystemGui;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;

public class Entry {

    public static void main(final String[] args) throws InterruptedException {
        MainSystemGui m = new MainSystemGui();
        JavaFxHelper.startGui(m, args);
//        Thread.sleep(500);
//        JavaFxHelper.startGui(new SosSensorSimulator(), args);
        m.getPresenter().getModel().getFileSystem().sendMessage(null, FileSystemEntries.SAVEME.buildPath());
        System.out.println(m.getPresenter().getModel().getFileSystem().toString());
    }

}
