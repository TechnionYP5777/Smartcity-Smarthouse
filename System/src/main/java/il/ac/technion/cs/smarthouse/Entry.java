package il.ac.technion.cs.smarthouse;

import java.util.Optional;

import org.apache.log4j.Logger;

import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.gui.main_system.MainSystemGui;
import il.ac.technion.cs.smarthouse.system.user_information.UserInformation;

public class Entry {

    @SuppressWarnings("static-access")
    public static void main(final String[] args) throws InterruptedException {
        Logger.shutdown();
        MainSystemGui m = new MainSystemGui();
        m.launchGui(args);
//        Thread.sleep(500);
//        JavaFxHelper.startGui(new SosSensorSimulator(), args);
       // m.getPresenter().getModel().getFileSystem().sendMessage(59, FileSystemEntries.TESTS.buildPath("my.first.try"));
        
        System.out.println("1: " + Optional.ofNullable(m.getPresenter().getModel().getUser()).orElse(new UserInformation("<NO_USER>", "", "", "")).getName());
        m.getPresenter().getModel().initializeUser("Wonder-Woman", "123", "026790844", "HERE");
        System.out.println("2: " + Optional.ofNullable(m.getPresenter().getModel().getUser()).orElse(new UserInformation("<NO_USER>", "", "", "")).getName());
        m.getPresenter().getModel().getFileSystem().sendMessage(null, FileSystemEntries.SAVEME.buildPath());
        System.out.println("3: " + Optional.ofNullable(m.getPresenter().getModel().getUser()).orElse(new UserInformation("<NO_USER>", "", "", "")).getName());
        
        System.out.println(">> Now it should save to the server");
        System.out.println(">> SLEEP for 5000");
        
        Thread.sleep(5000);// server should be updated first
        
        m.kill();
        System.out.println(">> Killed old house\n\n");
        System.out.println(">> starting new house");
        m = new MainSystemGui();
        m.launchGui(args);
        
        System.out.println("4: " + Optional.ofNullable(m.getPresenter().getModel().getUser()).orElse(new UserInformation("<NO_USER>", "", "", "")).getName());
        
        System.out.println(">> SLEEP for 5000");
        Thread.sleep(5000);// server should be updated first
        
        System.out.println("5: " + Optional.ofNullable(m.getPresenter().getModel().getUser()).orElse(new UserInformation("<NO_USER>", "", "", "")).getName());
        
        m.kill();
    }

}
