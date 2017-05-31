package il.ac.technion.cs.smarthouse.applications.dashboard;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.Tile.SkinType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class ConfigurationController implements Initializable {
    
    @FXML public TextField path;
    @FXML public ComboBox<String> types = new ComboBox<>();
    @FXML public Button button;
    @FXML public ScrollPane scrollPane;
    
    private FlowPane innerPane;
    
    private TileType chosenType;
    private Runnable callback;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button.setOnMouseClicked(e-> { 
        	chosenType = TileType.fromstring(types.getValue());
        	if(callback != null) callback.run();
        	((Stage) button.getScene().getWindow()).close();
        });
        
        scrollPane.setHbarPolicy(ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setFitToHeight(true);
        
        types.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
        switch(newValue) {
            case "Clock":
                Tile clockTile = TileBuilder.create()
                .prefSize(150, 150)
                .skinType(SkinType.CLOCK)
                .title("Clock Tile")
                .text("Whatever text")
                .dateVisible(true)
                .locale(Locale.US)
                .running(true)
                .build();
                innerPane = new FlowPane(Orientation.HORIZONTAL, 1, 1, clockTile);
                innerPane.setPadding(new Insets(5));
                innerPane.setPrefSize(150, 150);
                innerPane.setBackground(
                                new Background(new BackgroundFill(Tile.BACKGROUND.darker(), CornerRadii.EMPTY, Insets.EMPTY)));
                scrollPane.setContent(innerPane);
        }

        });
        
    }
    
    public void setComboBox() {
        ObservableList<String> options = 
                        FXCollections.observableArrayList(
                            "Clock",
                            "Numeric",
                            "Pie Chart"
                        );
        types.setItems(options);
    }
    
    public TileType getChosenType(){
    	return chosenType;
    }
    
    public String getChosenPath(){
    	return path.getText();
    }
    
    public void SetCallback(Runnable r){
    	callback = r;
    }

}