/**
 * 
 */
package il.ac.technion.cs.smarthouse.applications.dashboard.model.widget;

import java.util.Arrays;
import java.util.stream.Stream;

import eu.hansolo.tilesfx.Tile;
import il.ac.technion.cs.smarthouse.applications.dashboard.model.WidgetType;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Stop;

/**
 * @author Elia Traore
 * @since Jun 3, 2017
 */
public class GraphWidget extends BasicWidget {
	XYChart.Series<String, Number>[] data;

	public GraphWidget(WidgetType t, XYChart.Series<String, Number>  ... data) {
		super(t);
		builder.series(data);
		this.data = data;
		
//		if(WidgetType.PROGRESS_LINE_GRAPH.equals(type)){
//			/*todo: specific for spark line - allow dynamic marking of color margins, 
//			 * or move this code to the tiletype#getbuilder method*/
//			builder.gradientStops(new Stop(0, Tile.GREEN),
//		                    new Stop(0.5, Tile.YELLOW),
//		                    new Stop(1.0, Tile.RED))
//		     		.strokeWithGradient(true);
////					.unit("mb")
//		}
	}
	
	public static XYChart.Series<String, Number> getDefaultSerie(){
		XYChart.Series<String, Number> series3 = new XYChart.Series();
	    series3.setName("Outside");
	    series3.getData().add(new XYChart.Data("MO", 8));
	    series3.getData().add(new XYChart.Data("TU", 5));
	    series3.getData().add(new XYChart.Data("WE", 0));
	    series3.getData().add(new XYChart.Data("TH", 2));
	    series3.getData().add(new XYChart.Data("FR", 4));
	    series3.getData().add(new XYChart.Data("SA", 3));
	    series3.getData().add(new XYChart.Data("SU", 5));
	    return series3;
	}
	
	public GraphWidget(WidgetType t){
		this(t, getDefaultSerie());
	}

	public String getTitle(){
		return "Graph Widget";
	}

	public void updateExisting(Number value, String key){
		Stream.of(data)
				.forEach(serie -> serie.getData()
										.stream()
										.filter( data -> data.getXValue().equals(key))
										.forEach(data -> data.setYValue(value)));
	}
	
	public void addEntry(Number value, String key, Integer index){
		data[index].getData().add(new XYChart.Data(key,value));
	}
}
