package il.ac.technion.cs.smarthouse.system.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainSystemGui extends Application {
    
    private static final String APP_NAME = "Smarthouse";
    
    private FXMLLoader loader;
    
    public static void main(final String[] args) {
        launch(args);
    }

    @Override public void start(final Stage s) throws Exception {
        
        System.out.println("Initializing system ui...");
        
        final Scene scene = new Scene(getRoot(), 1000, 800);
        s.setTitle(APP_NAME);
        s.getIcons().add(new Image(getClass().getResourceAsStream("/icons/smarthouse-icon.png")));
        s.setScene(scene);
        s.show();

        s.setOnCloseRequest(e -> kill());
    }
    
    public void kill() {
        System.out.println("System closing...");
        ((MainSystemGuiController)loader.getController()).sysCore.sensorsHandler.closeSockets();
    }
    
    public Parent getRoot() {
        try {
            loader = new FXMLLoader(getClass().getResource("main_system_ui.fxml"));
            return loader.load();
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }
}
