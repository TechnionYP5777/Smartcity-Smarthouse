package il.ac.technion.cs.smarthouse.system.dashboard.widget;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import il.ac.technion.cs.smarthouse.system.dashboard.InfoCollector;
import il.ac.technion.cs.smarthouse.system.dashboard.WidgetType;
import javafx.application.Platform;
import javafx.scene.chart.XYChart;

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
        Optional.ofNullable(data.getUnit()).ifPresent(d -> builder.unit(d));

    }

    @Override
    public String getTitle() {
        return "Graph Widget";
    }

    @Override
    public synchronized void update(final Double value, final String key) {
        if (WidgetType.PROGRESS_LINE_GRAPH.equals(type))
            super.update(value, key);
        
        if (dataSeries.containsKey(key))
            Platform.runLater(() -> {
                if (points > 30)
                    dataSeries.get(key).getData().remove(0);
                dataSeries.get(key).getData().add(new XYChart.Data<>(points + "", value));
                ++points;
            });
    }

    public Set<String> getUpdateKeys() {
        return dataSeries.keySet();
    }

}
