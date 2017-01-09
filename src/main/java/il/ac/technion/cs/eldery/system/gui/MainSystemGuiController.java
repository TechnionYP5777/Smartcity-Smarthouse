package il.ac.technion.cs.eldery.system.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;

public class MainSystemGuiController implements Initializable {

    @FXML Tab homeTab;
    @FXML Tab userTab;
    @FXML Tab appsTab;
    @FXML Tab sensorsTab;

    @Override public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        try {
            userTab.setContent((Node) FXMLLoader.load(this.getClass().getResource("user information.fxml")));
            sensorsTab.setContent(
                    (Node) FXMLLoader.load(this.getClass().getResource("/il/ac/technion/cs/eldery/system/gui/mapping/house_mapping.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
