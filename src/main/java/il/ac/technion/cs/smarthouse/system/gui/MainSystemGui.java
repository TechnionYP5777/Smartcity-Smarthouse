package il.ac.technion.cs.smarthouse.system.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainSystemGui extends Application {
    public static void main(final String[] args) {
        launch(args);
    }

    @Override public void start(final Stage s) throws Exception {
        final Parent root = FXMLLoader.load(getClass().getResource("main_system_ui.fxml"));
        final Scene scene = new Scene(root);
        s.setTitle("System Controller");
        s.setScene(scene);
        s.setWidth(800);
        s.setHeight(450);
        s.show();
    }
}
