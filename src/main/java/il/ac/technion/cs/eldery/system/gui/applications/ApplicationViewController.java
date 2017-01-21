package il.ac.technion.cs.eldery.system.gui.applications;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import il.ac.technion.cs.eldery.system.applications.ApplicationsHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class ApplicationViewController implements Initializable {
    @FXML ListView<String> listView;
    @FXML AnchorPane appView;
    @FXML Button plusButton;
    
    private ApplicationsHandler appsHandler;
    
    ObservableList<String> names;
    @Override public void initialize(URL location, ResourceBundle __) {
        this.names = FXCollections.observableArrayList(
                "Stove", "SOS");
        listView.setItems(names);
        listView.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                ObservableList<String> selected = listView.getSelectionModel().getSelectedItems();
            }   
            
        });
        this.plusButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
            }
        });
    }
    
    public void addListItem(String name){
        names.add(name);
        listView.setItems(this.names);
    }
    
    public void setAppsHandler(ApplicationsHandler appsHandler) {
        this.appsHandler = appsHandler;
    }
}
