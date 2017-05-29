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
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;

/**
 * @author Elia Traore
 * @since May 29, 2017
 */
public class Controller implements Initializable{
	private static final Random RND       = new Random();
	
	public List<Tile> defaultList(final FlowPane pane){
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
    		tiles.add(t);
    	}
		return tiles;
	}
	
	public List<Tile> timeList(final FlowPane pane){
		TimeSection timeSection = TimeSectionBuilder.create()
                .start(LocalTime.now().plusSeconds(20))
                .stop(LocalTime.now().plusHours(1))
                //.days(DayOfWeek.MONDAY, DayOfWeek.FRIDAY)
                .color(Tile.GRAY)
                .highlightColor(Tile.RED)
                .build();
		
		
		return Arrays.asList(
					TileBuilder.create()
	                .prefSize(TILE_SIZE, TILE_SIZE)
	                .skinType(SkinType.CLOCK)
	                .title("Clock Tile")
	                .text("Whatever text")
	                .dateVisible(true)
	                .locale(Locale.US)
	                .running(true)
	                .build()
                ,
	                TileBuilder.create()
	                .prefSize(TILE_SIZE, TILE_SIZE)
	                .skinType(SkinType.CUSTOM)
	                .title("Medusa Clock")
	                .graphic(
	                		ClockBuilder.create()
	                        .prefSize(TILE_SIZE, TILE_SIZE)
	                        .skinType(ClockSkinType.SLIM)
	                        .secondColor(Tile.FOREGROUND)
	                        .minuteColor(Tile.BLUE)
	                        .hourColor(Tile.FOREGROUND)
	                        .dateColor(Tile.FOREGROUND)
	                        .running(true)
	                        .build()
	                		)
	                .textVisible(false)
	                .build()
                ,
	                TileBuilder.create()
	                .prefSize(TILE_SIZE, TILE_SIZE)
	                .skinType(SkinType.TIMER_CONTROL)
	                .title("TimerControl Tile")
	                .text("Whatever text")
	                .secondsVisible(true)
	                .dateVisible(true)
	                .timeSections(timeSection)
	                .running(true)
	                .build()
                );
	}
	
	public List<Tile> precList(final FlowPane pane){
		return Arrays.asList(
					TileBuilder.create()
	                .prefSize(TILE_SIZE, TILE_SIZE)
	                .skinType(SkinType.HIGH_LOW)
	                .title("HighLow Tile")
	                .unit("\u0025")
	                .description("Test")
	                .text("Whatever text")
	                .referenceValue(6.7)
	                .value(8.2)
	                .build()
                ,
	                TileBuilder.create()
	                .prefSize(TILE_SIZE, TILE_SIZE)
	                .skinType(SkinType.PERCENTAGE)
	                .title("Percentage Tile")
	                .unit("\u0025")
	                .description("Test")
	                .maxValue(60)
	                .build()
                );
	}
	
	public List<Tile> dashList(final FlowPane pane){
		return Arrays.asList(
					TileBuilder.create()
	                .prefSize(TILE_SIZE, TILE_SIZE)
	                .skinType(SkinType.CUSTOM)
	                .title("Medusa Slim")
	                .text("Temperature")
	                .graphic(createGauge(Gauge.SkinType.SLIM))
	                .build()
                ,
                	TileBuilder.create()
                    .prefSize(TILE_SIZE, TILE_SIZE)
                    .skinType(SkinType.CUSTOM)
                    .title("Medusa Dashboard")
                    .text("Temperature")
                    .graphic(createGauge(Gauge.SkinType.DASHBOARD))
                    .build()
                ,
			        TileBuilder.create()
                    .prefSize(TILE_SIZE, TILE_SIZE)
                    .skinType(SkinType.CUSTOM)
                    .title("Medusa Indicator")
                    .text("")
                    .graphic(createGauge(Gauge.SkinType.INDICATOR))
                    .build()
                );
	}
	
	public List<Tile> simpleSectionList(final FlowPane pane){
		Gauge simpleSectionGauge = createGauge(Gauge.SkinType.SIMPLE_SECTION);
        simpleSectionGauge.setBarColor(Tile.FOREGROUND);
        simpleSectionGauge.setSections(new Section(66, 100, Tile.BLUE));
        
        Gauge slimGauge = createGauge(Gauge.SkinType.SLIM);
        Gauge digitalGauge = createGauge(Gauge.SkinType.DIGITAL);
		Gauge simpleDigitalGauge = createGauge(Gauge.SkinType.SIMPLE_DIGITAL);
        return Arrays.asList(
					TileBuilder.create()
	                .prefSize(TILE_SIZE, TILE_SIZE)
	                .skinType(SkinType.CUSTOM)
	                .title("Medusa SimpleSection")
	                .text("Temperature")
	                .graphic(simpleSectionGauge)
	                .build()
                ,
	                TileBuilder.create()
	                .prefSize(TILE_SIZE, TILE_SIZE)
	                .skinType(SkinType.CUSTOM)
	                .title("Medusa Slim")
	                .text("Temperature")
	                .graphic(slimGauge)
	                .build()
	            ,
		            TileBuilder.create()
	                .skinType(SkinType.CIRCULAR_PROGRESS)
	                .prefSize(TILE_SIZE, TILE_SIZE)
	                .title("CircularProgress Tile")
	                .text("Whatever text")
	                .unit("\u0025")
	                .animated(true)
	                .build()
	            ,
		            TileBuilder.create()
	                .prefSize(TILE_SIZE, TILE_SIZE)
	                .skinType(SkinType.CUSTOM)
	                .title("Medusa Digital")
	                .text("Temperature")
	                .graphic(digitalGauge)
	                .build()
	            ,
		            TileBuilder.create()
	                .prefSize(TILE_SIZE, TILE_SIZE)
	                .skinType(SkinType.CUSTOM)
	                .title("Medusa SimpleDigital")
	                .text("Temperature")
	                .graphic(simpleDigitalGauge)
	                .build()
				);
	}
	
	public List<Tile> graphList(final FlowPane pane){
        XYChart.Series<String, Number> series1 = new XYChart.Series();
        series1.setName("Whatever");
        series1.getData().add(new XYChart.Data("MO", 23));
        series1.getData().add(new XYChart.Data("TU", 21));
        series1.getData().add(new XYChart.Data("WE", 20));
        series1.getData().add(new XYChart.Data("TH", 22));
        series1.getData().add(new XYChart.Data("FR", 24));
        series1.getData().add(new XYChart.Data("SA", 22));
        series1.getData().add(new XYChart.Data("SU", 20));
		
	    XYChart.Series<String, Number> series2 = new XYChart.Series();
        series2.setName("Inside");
        series2.getData().add(new XYChart.Data("MO", 8));
        series2.getData().add(new XYChart.Data("TU", 5));
        series2.getData().add(new XYChart.Data("WE", 0));
        series2.getData().add(new XYChart.Data("TH", 2));
        series2.getData().add(new XYChart.Data("FR", 4));
        series2.getData().add(new XYChart.Data("SA", 3));
        series2.getData().add(new XYChart.Data("SU", 5));

        XYChart.Series<String, Number> series3 = new XYChart.Series();
        series3.setName("Outside");
        series3.getData().add(new XYChart.Data("MO", 8));
        series3.getData().add(new XYChart.Data("TU", 5));
        series3.getData().add(new XYChart.Data("WE", 0));
        series3.getData().add(new XYChart.Data("TH", 2));
        series3.getData().add(new XYChart.Data("FR", 4));
        series3.getData().add(new XYChart.Data("SA", 3));
        series3.getData().add(new XYChart.Data("SU", 5));
        
//        series1.getData().forEach(data -> data.setYValue(RND.nextInt(100)));
	    return Arrays.asList(
					TileBuilder.create()
	                .prefSize(TILE_SIZE, TILE_SIZE)
	                .skinType(SkinType.SPARK_LINE)
	                .title("SparkLine Tile")
	                .unit("mb")
	                .gradientStops(new Stop(0, Tile.GREEN),
	                               new Stop(0.5, Tile.YELLOW),
	                               new Stop(1.0, Tile.RED))
	                .strokeWithGradient(true)
	                .build()
                ,
	                TileBuilder.create()
	                .prefSize(TILE_SIZE, TILE_SIZE)
	                .skinType(SkinType.AREA_CHART)
	                .title("AreaChart Tile")
	                .series(series1)
	                .build()
                ,
	                TileBuilder.create()
	                .prefSize(TILE_SIZE, TILE_SIZE)
	                .skinType(SkinType.LINE_CHART)
	                .title("LineChart Tile")
	                .series(series2, series3)
	                .build()
						);
	}
	
	public List<Tile> interactiveList(final FlowPane pane){
		return Arrays.asList(
					TileBuilder.create()
	                .prefSize(TILE_SIZE, TILE_SIZE)
	                .skinType(SkinType.PLUS_MINUS)
	                .maxValue(30)
	                .minValue(0)
	                .title("PlusMinus Tile")
	                .text("Whatever text")
	                .description("Test")
	                .unit("\u00B0C")
	                .build()
                ,
	                TileBuilder.create()
	                .prefSize(TILE_SIZE, TILE_SIZE)
	                .skinType(SkinType.SLIDER)
	                .title("Slider Tile")
	                .text("Whatever text")
	                .description("Test")
	                .unit("\u00B0C")
	                .barBackgroundColor(Tile.FOREGROUND)
	                .build()
                ,
	                TileBuilder.create()
	                .prefSize(TILE_SIZE, TILE_SIZE)
	                .skinType(SkinType.SWITCH)
	                .title("Switch Tile")
	                .text("Whatever text")
	                //.description("Test")
	                .build()
	            ,
		            TileBuilder.create()
	                .prefSize(TILE_SIZE, TILE_SIZE)
	                .skinType(SkinType.TEXT)
	                .title("Text Tile")
	                .text("Whatever text")
	                .description("I'm a note!")
	                .textVisible(true)
	                .build()
				);
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
	
	
	public static double TILE_SIZE = 220;
	public static int TILES_IN_ROW = 3;
	public static int ROWS_NUM = 3;
	public static int index = 0;
	@FXML public FlowPane pane;

	@Override public void initialize(final URL location, final ResourceBundle __) {
        pane.setPrefSize(1245,  780);
    	List<Tile> tiles = defaultList(pane);
    	pane.getChildren().addAll(tiles);
        pane.setBackground(new Background(new BackgroundFill(Tile.BACKGROUND.darker(), CornerRadii.EMPTY, Insets.EMPTY)));

    	
    	/*
    	List<Tile> tiles2 = timeList(pane);
    	List<Tile> tiles3= precList(pane);
    	List<Tile> tiles4 = dashList(pane);
    	List<Tile> tiles5 = simpleSectionList(pane);
    	List<Tile> tiles6 = graphList(pane);
    	List<Tile> tiles7 = interactiveList(pane);
    	List<List<Tile>> lists = Arrays.asList(tiles2, tiles3, tiles4, tiles5, tiles6, tiles7);
    	lists.stream()
    			.flatMap(l->l.stream())
    			.forEach(t ->{
    				t.setOnMouseEntered(e -> {t.setText("get off me");t.setForegroundBaseColor(Color.AQUAMARINE);});
    	    		t.setOnMouseExited(e -> {t.setText("and stay there!"); t.setForegroundBaseColor(Color.WHITE);});
    			}
    			);
    	pane.setOnMouseClicked(e -> pane.getChildren().setAll(lists.get(index++ % lists.size())));
    	pane.getChildren().addAll(tiles2);
    	*/

//    	pane.setPadding(new Insets(5));
//        pane.setPrefHeight((TILE_SIZE+8)*ROWS_NUM);
//        pane.setPrefWidth((TILE_SIZE+8)*TILES_IN_ROW);

	}
}
