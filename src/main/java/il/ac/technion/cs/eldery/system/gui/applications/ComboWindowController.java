package il.ac.technion.cs.eldery.system.gui.applications;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ComboWindowController  implements Initializable {
    @FXML ComboBox<String> comboBox;
    @FXML Button install;
    @FXML Button cancel;
    @FXML HBox ButtonBox;
    @FXML VBox topBox;
    ApplicationViewController applicationViewController;

    @Override public void initialize(URL location, ResourceBundle __) {   
        HBox.setHgrow(comboBox, Priority.ALWAYS);
        HBox.setHgrow(install, Priority.ALWAYS);
        HBox.setHgrow(cancel, Priority.ALWAYS);
        final int btnCount = ButtonBox.getChildren().size();
        comboBox.prefWidthProperty().bind(topBox.widthProperty());
        install.prefWidthProperty().bind(ButtonBox.widthProperty().divide(btnCount));
        cancel.prefWidthProperty().bind(ButtonBox.widthProperty().divide(btnCount));
        comboBox.setPromptText("Choose File");
        comboBox.getItems().add("roy");
        comboBox.getItems().add("ron");
    }
    
    public void setController(ApplicationViewController ¢){
        this.applicationViewController=¢;
    }

}
