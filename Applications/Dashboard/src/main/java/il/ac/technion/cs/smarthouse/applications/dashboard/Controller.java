/**
 * 
 */
package il.ac.technion.cs.smarthouse.applications.dashboard;

import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

import eu.hansolo.medusa.ClockBuilder;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.Section;
import eu.hansolo.medusa.Clock.ClockSkinType;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.TimeSection;
import eu.hansolo.tilesfx.TimeSectionBuilder;
import eu.hansolo.tilesfx.Tile.SkinType;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

/**
 * @author Elia Traore
 * @since May 29, 2017
 */
public class Controller implements Initializable{

	public static double TILE_SIZE = 220;
	public static int TILES_IN_ROW = 3;
	public static int ROWS_NUM = 3;
	public static int index = 0;
	@FXML public FlowPane pane;

	private void setTlieEventHandlers(final Tile t, final Integer position){
		t.setOnMouseClicked(e -> openConfiguration(position));
	}
	
	private void updateTile(TileType type, String path, Integer position) {
		//todo: initalize according to type and path
	    Tile tile = TileType.fromType(type, TILE_SIZE);
	    setTlieEventHandlers(tile, position);
		pane.getChildren().set(position, 
				tile
            );
	}
	
	private void openConfiguration(Integer position) {
        try {
        	URL location = getClass().getClassLoader().getResource("dashboard_configuration_ui.fxml");
//        	System.out.println(location);todo: ELIA why is this the location?
            final FXMLLoader fxmlLoader = new FXMLLoader(location);
            final Parent root1 = (Parent) fxmlLoader.load();
            final Stage stage = new Stage();
            ConfigurationController controller = fxmlLoader.getController();
            controller.setComboBox();
            controller.SetCallback(()->updateTile(controller.getChosenType(), controller.getChosenPath(), position));
//            stage.setOnHidden(e -> updateTile(controller.getChosenType(), controller.getChosenPath(), position));
//            stage.setOnCloseRequest(e -> updateTile(controller.getChosenType(), position));
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (final Exception $) {
            // TODO: handle error
            System.out.println("Oops...");
            $.printStackTrace();
        }
        
    }

	
	private List<Tile> defaultList(){
		List<Tile> tiles = new ArrayList<>();
		for(int i=0;  i < ROWS_NUM*TILES_IN_ROW; ++i){
    		final Integer loc = i;
    		final Tile t = TileBuilder.create()
					.prefSize(TILE_SIZE, TILE_SIZE)
					.skinType(SkinType.TEXT)
					.title("Text Tile")
					.text("Whatever text")
					.description("Im text tile #" + i)
					.textVisible(true)
					.build();
    		setTlieEventHandlers(t, loc);
    		
    		/*
    		t.setOnMouseClicked(e -> 
    			pane.getChildren().set(loc, 
					    				TileBuilder.create()
					    		            .prefSize(TILE_SIZE, TILE_SIZE)
					    		            .skinType(SkinType.CLOCK)
					    		            .title("Clock Tile")
					    		            .text("Whatever text")
					    		            .dateVisible(true)
					    		            .locale(Locale.US)
					    		            .running(true)
					    		            .build()
			    		            )
    		);
    		t.setOnMouseEntered(e -> {t.setText("get off me");t.setForegroundBaseColor(Color.AQUAMARINE);});
    		t.setOnMouseExited(e -> {t.setText("and stay there!"); t.setForegroundBaseColor(Color.WHITE);});
    		*/
    		tiles.add(t);
    	}
		return tiles;
	}
		
	private Gauge createGauge(final Gauge.SkinType TYPE) {
        return GaugeBuilder.create()
                           .skinType(TYPE)
                           .prefSize(TILE_SIZE, TILE_SIZE)
                           .animated(true)
                           //.title("")
                           .unit("\u00B0C")
                           .valueColor(Tile.FOREGROUND)
                           .titleColor(Tile.FOREGROUND)
                           .unitColor(Tile.FOREGROUND)
                           .barColor(Tile.BLUE)
                           .needleColor(Tile.FOREGROUND)
                           .barColor(Tile.BLUE)
                           .barBackgroundColor(Tile.BACKGROUND.darker())
                           .tickLabelColor(Tile.FOREGROUND)
                           .majorTickMarkColor(Tile.FOREGROUND)
                           .minorTickMarkColor(Tile.FOREGROUND)
                           .mediumTickMarkColor(Tile.FOREGROUND)
                           .build();
    }
	
	
	@Override public void initialize(final URL location, final ResourceBundle __) {
        pane.setPrefSize(1200,  800);
    	List<Tile> tiles = defaultList();
    	pane.getChildren().addAll(tiles);
        pane.setBackground(new Background(new BackgroundFill(Tile.BACKGROUND.darker(), null,null)));


//    	pane.setPadding(new Insets(5));
//        pane.setPrefHeight((TILE_SIZE+8)*ROWS_NUM);
//        pane.setPrefWidth((TILE_SIZE+8)*TILES_IN_ROW);

	}
}
