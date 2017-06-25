package il.ac.technion.cs.smarthouse.system.dashboard.widget;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import eu.hansolo.tilesfx.Tile;
import il.ac.technion.cs.smarthouse.system.dashboard.InfoCollector;
import il.ac.technion.cs.smarthouse.system.dashboard.WidgetType;
import javafx.application.Platform;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Stop;

/**
 * @author Elia Traore
 * @since Jun 3, 2017
 */
public class GraphWidget extends BasicWidget {
    // path -> series
    private final Map<String, XYChart.Series<String, Number>> dataSeries = new HashMap<>();
    private int points;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public GraphWidget(final WidgetType t, final Double tileSize, final InfoCollector data) {
        super(t, tileSize, data);

        data.getInfoEntries().keySet().forEach(path -> {
            dataSeries.put(path, new XYChart.Series<>());
            dataSeries.get(path).setName(data.getInfoEntries().get(path));
        });

        builder.series(dataSeries.values().stream().map(s -> (XYChart.Series) s).collect(Collectors.toList()));

        if (!WidgetType.PROGRESS_LINE_GRAPH.equals(type))
            return;
        builder.gradientStops(new Stop(0, Tile.GREEN), new Stop(0.5, Tile.YELLOW), new Stop(1.0, Tile.RED))
                        .strokeWithGradient(true);
        if (data.getUnit() != null)
            builder.unit(data.getUnit());
    }

    @Override
    public String getTitle() {
        return "Graph Widget";
    }

    @Override
    public synchronized void update(final Double value, final String key) {
        if (WidgetType.PROGRESS_LINE_GRAPH.equals(type))
            super.update(value, key);
        if (!dataSeries.containsKey(key))
            return;
        Platform.runLater(() -> {
            final Integer maxDataSize = 30;
            if (points > maxDataSize)
                dataSeries.get(key).getData().remove(0);

            dataSeries.get(key).getData().add(new XYChart.Data<>(points + "", value));
            ++points;
        });
    }

    public Set<String> getUpdateKeys() {
        return dataSeries.keySet();
    }

}
