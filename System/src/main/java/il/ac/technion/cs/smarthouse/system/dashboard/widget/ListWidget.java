package il.ac.technion.cs.smarthouse.system.dashboard.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.skins.BarChartItem;
import eu.hansolo.tilesfx.skins.LeaderBoardItem;
import il.ac.technion.cs.smarthouse.system.dashboard.InfoCollector;
import il.ac.technion.cs.smarthouse.system.dashboard.WidgetType;
import javafx.scene.paint.Color;

/**
 * @author Elia Traore
 * @since Jun 3, 2017
 */
public class ListWidget extends BasicWidget {
    private static Logger log = LoggerFactory.getLogger(ListWidget.class);
    private static final List<Color> colors = Arrays.asList(Tile.BLUE, Tile.GREEN, Tile.MAGENTA, Tile.ORANGE, Tile.RED,
                    Tile.YELLOW);
    private final Random rand = new Random();

    private final Map<String, String> namedPaths;

    /**
     * [[SuppressWarningsSpartan]]
     */
    @SuppressWarnings("unchecked")
    public ListWidget(final WidgetType t, final Double tileSize, final InfoCollector data) {
        super(t, tileSize, data);
        namedPaths = data.getInfoEntries();
        final List items = new ArrayList();
        for (final String name : data.getInfoEntries().values())
            if (WidgetType.BAR_CHART.equals(type))
                items.add(new BarChartItem(name, 0, colors.get(rand.nextInt(colors.size()))));
            else
                items.add(new LeaderBoardItem(name, 0));

        if (WidgetType.BAR_CHART.equals(type))
            builder.barChartItems(items);
        else
            builder.leaderBoardItems(items);
    }

    @Override
    public String getTitle() {
        return "List Widget";
    }

    @Override
    public void update(final Number value, final String key) {
        final String name = namedPaths.get(key);
        final double val = value.doubleValue();
        if (WidgetType.BAR_CHART.equals(type))
            getTile().getBarChartItems().stream().filter(item -> item.getName().equals(name))
                            .forEach(v -> v.setValue(val));
        if (WidgetType.LEAD_CHART.equals(type))
            getTile().getLeaderBoardItems().stream().filter(item -> item.getName().equals(name)).findFirst()
                            .ifPresent(v -> v.setValue(val));
    }

    public Set<String> getUpdateKeys() {
        return data.getInfoEntries().keySet();
    }

}
