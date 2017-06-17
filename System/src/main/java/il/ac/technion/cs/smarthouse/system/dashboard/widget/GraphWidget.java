package il.ac.technion.cs.smarthouse.system.dashboard.widget;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import eu.hansolo.tilesfx.Tile;
import il.ac.technion.cs.smarthouse.system.dashboard.InfoCollector;
import il.ac.technion.cs.smarthouse.system.dashboard.WidgetType;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.paint.Stop;

/**
 * @author Elia Traore
 * @since Jun 3, 2017
 */
public class GraphWidget extends BasicWidget {
    private final Map<String, XYChart.Series<String, Number>> dataSeries = new HashMap<>();// path
                                                                                           // ->
                                                                                           // series

    @SuppressWarnings("unchecked")
    public GraphWidget(final WidgetType t, final Double tileSize, final InfoCollector data) {
        super(t, tileSize, data);

        data.getInfoEntries().keySet().forEach(path -> {
            dataSeries.put(path, new XYChart.Series<>());
            dataSeries.get(path).setName(data.getInfoEntries().get(path));
        });

        builder.series(dataSeries.values().stream().map(s -> (XYChart.Series) s).collect(Collectors.toList()));

        if (WidgetType.PROGRESS_LINE_GRAPH.equals(type))
            builder.gradientStops(new Stop(0, Tile.GREEN), new Stop(0.5, Tile.YELLOW), new Stop(1.0, Tile.RED))
                            .strokeWithGradient(true).unit(data.getUnit());
    }

    @Override
    public String getTitle() {
        return "Graph Widget";
    }

    @Override
    public void update(final Number value, final String key) {
        if (WidgetType.PROGRESS_LINE_GRAPH.equals(type))
            super.update(value, key);
        if (!dataSeries.containsKey(key))
            return;
        final Integer maxDataSize = 7;
        final ObservableList<Data<String, Number>> keySerie = dataSeries.get(key).getData();
        if (keySerie.size() > maxDataSize)
            for (Integer i = 0; i < keySerie.size() - 1; ++i)
                keySerie.get(i).setYValue(keySerie.get(i + 1).getYValue());
        // keySerie.remove(keySesrie.size()-1);

        keySerie.add(new XYChart.Data<>(keySerie.size() + "", value));
    }

    public Set<String> getUpdateKeys() {
        return dataSeries.keySet();
    }

}
