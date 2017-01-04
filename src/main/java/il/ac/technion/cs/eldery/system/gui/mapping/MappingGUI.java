package il.ac.technion.cs.eldery.system.gui.mapping;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MappingGUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("house mapping.fxml"));
        
        Scene scene = new Scene(root, 1000, 800);
        stage.setTitle("Test");
        stage.setScene(scene);
        stage.show();
    }
}
