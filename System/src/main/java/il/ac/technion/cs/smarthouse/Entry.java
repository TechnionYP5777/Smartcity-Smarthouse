package il.ac.technion.cs.smarthouse;

import org.apache.log4j.Logger;

import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.gui.main_system.MainSystemGui;

public class Entry {

    public static void main(final String[] args) throws InterruptedException {
        Logger.shutdown();
        MainSystemGui m = new MainSystemGui();
        m.launchGui(args);
//        Thread.sleep(500);
//        JavaFxHelper.startGui(new SosSensorSimulator(), args);
        m.getPresenter().getModel().getFileSystem().sendMessage(59, FileSystemEntries.TESTS.buildPath("my.first.try"));
        m.getPresenter().getModel().getFileSystem().sendMessage(null, FileSystemEntries.SAVEME.buildPath());
        //System.out.println(((FileSystemImpl)m.getPresenter().getModel().getFileSystem()).toString("system","internals"));
        
        System.out.println(m.getPresenter().getModel().getFileSystem().toString());
        
        
        Thread.sleep(5000);// server should be updated first
        
        
        
        m.kill();
        m = new MainSystemGui();
        m.launchGui(args);
        System.out.println(m.getPresenter().getModel().getFileSystem().toString());
        
        Thread.sleep(5000);// server should be updated first
        
        System.out.println(m.getPresenter().getModel().getFileSystem().toString());
        
        m.kill();
    }

}
