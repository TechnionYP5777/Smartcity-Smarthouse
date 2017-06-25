package il.ac.technion.cs.smarthouse.simulator.view;

import java.net.URL;
import java.util.ResourceBundle;

import il.ac.technion.cs.smarthouse.simulator.model.SensorField;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SensorConfigListController implements Initializable{

    @FXML private TableView<SensorField> sensorTable;
    @FXML private TableColumn<SensorField, String> nameColumn;
    @FXML private TableColumn<SensorField, Boolean> configColumn;
    @FXML private TableColumn<SensorField, Boolean> messageColumn;
    @FXML private TableColumn<SensorField, Boolean> deleteColumn;
	@FXML private Button addButton;
    @Override
	public void initialize(URL location, ResourceBundle b) {
		nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        configColumn.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue() != null));
        configColumn.setCellFactory(p -> {
            final ButtonCell $ = new ButtonCell();
            $.setAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
            
            $.setImage(new ImageView(new Image(getClass().getResourceAsStream("/Message.png"))));
            
            $.setAlignment(Pos.CENTER);
            return $;
        });
        messageColumn.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue() != null));
        messageColumn.setCellFactory(p -> {
            final ButtonCell $ = new ButtonCell();
            $.setAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
            
            $.setImage(new ImageView(new Image(getClass().getResourceAsStream("/Delete.png"))));
            
            $.setAlignment(Pos.CENTER);
            return $;
        });
        deleteColumn.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue() != null));
        deleteColumn.setCellFactory(p -> {
            final ButtonCell $ = new ButtonCell();
            $.setAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
            
            $.setImage(new ImageView(new Image(getClass().getResourceAsStream("/Settings.png"))));
            
            $.setAlignment(Pos.CENTER);
            return $;
        });
	}
    
    private class ButtonCell extends TableCell<SensorField, Boolean> {
        final Button cellButton; 

        ButtonCell() {
        	this.cellButton = new Button();
            // Action when the button is pressed
        }
        
        public void setAction(EventHandler<ActionEvent> e){
        	this.cellButton.setOnAction(e);
        }
        
        public void setImage(ImageView v){
        	this.cellButton.setGraphic(v);
        }

        // Display button if the row is not empty
        @Override
        protected void updateItem(final Boolean t, final boolean empty) {
            super.updateItem(t, empty);
            setGraphic(empty ? null : cellButton);
        }
    }
    
    
}
