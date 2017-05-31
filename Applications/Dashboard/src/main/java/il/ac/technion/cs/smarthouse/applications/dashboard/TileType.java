/**
 * 
 */
package il.ac.technion.cs.smarthouse.applications.dashboard;

import java.util.Locale;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.Tile.SkinType;
import javafx.scene.paint.Stop;

/**
 * @author Elia Traore
 * @since May 30, 2017
 */
public enum TileType {
	CLOCK, MEDUSA_CLOCK, PERCENTAGE, DASHBOARD, SECTION, SPARK_LINE, AREA_CHART, LINE_CHART, TEXT, HIGH_LOW,
	BAR_LEADERBOARD, LEADERBOARD, PIE_CHART;
	
	public static TileType fromstring(String from){
	    switch(from) {
	        case "Clock": return TileType.CLOCK;
	        case "Number": return TileType.HIGH_LOW;
	        case "Pie Chart": return TileType.PIE_CHART;
	        case "Line Chart": return TileType.SPARK_LINE;
	        case "Text": return TileType.TEXT;
	        case "Numeric Range": return TileType.DASHBOARD;
	        case "Leaderboard": return BAR_LEADERBOARD;
	    }
		return TileType.CLOCK;
	}
	
	public static Tile fromType(TileType from, double tileSize){
        Tile tile = TileBuilder.create()
                        .prefSize(tileSize, tileSize)
                        .skinType(SkinType.CLOCK)
                        .title("Clock Tile")
                        .text("Whatever text")
                        .dateVisible(true)
                        .locale(Locale.US)
                        .running(true)
                        .build();
        switch(from) {
            case CLOCK: return tile;
            case HIGH_LOW: return TileBuilder.create()
                            .prefSize(tileSize, tileSize)
                            .skinType(SkinType.HIGH_LOW)
                            .title("HighLow Tile")
                            .unit("\u0025")
                            .description("Test")
                            .text("Whatever text")
                            .referenceValue(6.7)
                            .value(8.2)
                            .build();
//            case PIE_CHART: return TileBuilder.create()
//                            .skinType(SkinType.DONUT_CHART)
//                            .prefSize(TILE_SIZE, TILE_SIZE)
//                            .title("DonutChart Tile")
//                            .text("Whatever text")
//                            .textVisible(false)
//                            .radialChartData(chartData1, chartData2, chartData3, chartData4)
//                            .build();
            case SPARK_LINE: return TileBuilder.create()
                            .prefSize(tileSize, tileSize)
                            .skinType(SkinType.SPARK_LINE)
                            .title("SparkLine Tile")
                            .unit("mb")
                            .gradientStops(new Stop(0, Tile.GREEN),
                                           new Stop(0.5, Tile.YELLOW),
                                           new Stop(1.0, Tile.RED))
                            .strokeWithGradient(true)
                            .build();
            case TEXT: return TileBuilder.create()
                            .prefSize(tileSize, tileSize)
                            .skinType(SkinType.TEXT)
                            .title("Text Tile")
                            .text("Whatever text")
                            .description("May the force be with you\n...always")
                            .textVisible(true)
                            .build();
            case DASHBOARD: return TileBuilder.create()
                            .prefSize(tileSize, tileSize)
                            .skinType(SkinType.GAUGE)
                            .title("Gauge Tile")
                            .unit("V")
                            .threshold(75)
                            .build();
//            case BAR_LEADERBOARD: return TileBuilder.create()
//                            .prefSize(150, 150)
//                            .skinType(SkinType.BAR_CHART)
//                            .title("BarChart Tile")
//                            .text("Whatever text")
//                            .barChartItems(barChartItem1, barChartItem2, barChartItem3, barChartItem4)
//                            .decimals(0)
//                            .build();
            default: return tile;
        }
    }
}
