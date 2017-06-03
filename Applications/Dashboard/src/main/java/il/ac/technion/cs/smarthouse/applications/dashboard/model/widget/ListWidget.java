/**
 * 
 */
package il.ac.technion.cs.smarthouse.applications.dashboard.model.widget;

import java.util.Arrays;
import java.util.List;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.skins.BarChartItem;
import eu.hansolo.tilesfx.skins.LeaderBoardItem;
import il.ac.technion.cs.smarthouse.applications.dashboard.model.WidgetType;

/**
 * @author Elia Traore
 * @since Jun 3, 2017
 */
public class ListWidget extends BasicWidget {

	public ListWidget(WidgetType t, BarChartItem... items) {
		super(t);
		builder.barChartItems(items);
	}

	public ListWidget(WidgetType t, LeaderBoardItem... items) {
		super(t);
		builder.leaderBoardItems(items);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see il.ac.technion.cs.smarthouse.applications.dashboard.model.widget.
	 * BasicWidget#getTitle()
	 */
	@Override
	public String getTitle() {
		return "List Widget";
	}

	public static List<BarChartItem> getDefaultBarItems() {
		return Arrays.asList(new BarChartItem("Gerrit", 47, Tile.BLUE), new BarChartItem("Sandra", 43, Tile.RED),
				new BarChartItem("Lilli", 12, Tile.GREEN), new BarChartItem("Anton", 8, Tile.ORANGE));
	}

	public static List<LeaderBoardItem> getDefaultBoardItems() {
		return Arrays.asList(new LeaderBoardItem("Gerrit", 47), new LeaderBoardItem("Sandra", 43),
				new LeaderBoardItem("Lilli", 12), new LeaderBoardItem("Anton", 8));
	}

	@Override
	public void updateExisting(Number value, String key) {
		if (WidgetType.BAR_CHART.equals(type))
			getTile().getBarChartItems().stream().filter(item -> item.getName().equals(key))
					.forEach(v -> v.setValue((double) value));
		if (WidgetType.LEAD_CHART.equals(type))
			getTile().getLeaderBoardItems().stream().filter(item -> item.getName().equals(key))
					.forEach(v -> v.setValue((double) value));
	}
}
