/**
 * 
 */
package il.ac.technion.cs.smarthouse.applications.dashboard.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import eu.hansolo.medusa.ClockBuilder;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.Clock.ClockSkinType;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import il.ac.technion.cs.smarthouse.applications.dashboard.model.widget.ClockWidget;
import il.ac.technion.cs.smarthouse.applications.dashboard.model.widget.DashboardWidget;
import il.ac.technion.cs.smarthouse.applications.dashboard.model.widget.GraphWidget;
import il.ac.technion.cs.smarthouse.applications.dashboard.model.widget.ListWidget;
import il.ac.technion.cs.smarthouse.applications.dashboard.model.widget.PrecenetageWidget;
import eu.hansolo.tilesfx.Tile.SkinType;

/**
 * @author Elia Traore
 * @since May 30, 2017
 */
public enum WidgetType {

	// SIMPLE_CLOCK(ClockWidget.class, SkinType.CLOCK),
	MEDUSA_CLOCK(ClockWidget.class, SkinType.CUSTOM), ANALOG_CLOCK(ClockWidget.class, SkinType.TIMER_CONTROL),

	FLUCTUATIONS(PrecenetageWidget.class, SkinType.HIGH_LOW),

	BASIC_DASHBOARD(DashboardWidget.class, SkinType.GAUGE), NEEDLE_BDASHBORD(DashboardWidget.class, SkinType.CUSTOM),

	BAR_CHART(ListWidget.class, SkinType.BAR_CHART), LEAD_CHART(ListWidget.class, SkinType.LEADER_BOARD),

	// PROGRESS_LINE_GRAPH(GraphWidget.class, SkinType.SPARK_LINE),
	AREA_GRAPH(GraphWidget.class, SkinType.AREA_CHART), LINES_GRAPH(GraphWidget.class, SkinType.LINE_CHART);

	private final SkinType skinType;
	private final Class implementingClass;
	// private String title;

	private WidgetType(final Class implClass, final SkinType skin) {
		this.implementingClass = implClass;
		skinType = skin;
		// try {
		// title = ((Class<BasicWidget>)
		// implClass).getConstructor(WidgetType.class).newInstance(this).getTitle();
		// } catch (InstantiationException | IllegalAccessException |
		// IllegalArgumentException | InvocationTargetException
		// | NoSuchMethodException | SecurityException e) {
		// e.printStackTrace();
		// }
	}

	public static WidgetType fromString(final String name) {
		final List<WidgetType> $ = Arrays.asList(WidgetType.values()).stream()
				.filter(value -> value.name().equalsIgnoreCase(name)).collect(Collectors.toList());
		$.add(null);
		return $.get(0);
	}

	public TileBuilder getBuilder(double TILE_SIZE) {
		TileBuilder builder = TileBuilder.create();

		builder.prefSize(TILE_SIZE, TILE_SIZE).text(this.name().replaceAll("_", "  "))// todo:
																						// needed?
				.skinType(this.skinType);

		switch (this) {
		case MEDUSA_CLOCK:
			builder.graphic(ClockBuilder.create().prefSize(TILE_SIZE, TILE_SIZE).skinType(ClockSkinType.SLIM)
					.secondColor(Tile.FOREGROUND).minuteColor(Tile.BLUE).hourColor(Tile.FOREGROUND)
					.dateColor(Tile.FOREGROUND).running(true).build());
			break;
		case ANALOG_CLOCK:
			builder.secondsVisible(true);
			break;
		case BAR_CHART:
			builder.decimals(0);
			break;
		case NEEDLE_BDASHBORD:
			builder.graphic(createGauge(Gauge.SkinType.INDICATOR, TILE_SIZE));
			break;
		default:
			;
		}

		return builder;
	}

	public Class getImplementingClass() {
		return implementingClass;
	}

	// public String getTitle(){
	// return title;
	// }

	private Gauge createGauge(final Gauge.SkinType TYPE, Double TILE_SIZE) {
		return GaugeBuilder.create().skinType(TYPE).prefSize(TILE_SIZE, TILE_SIZE).animated(true)
				// .title("")
				.unit("\u00B0C").valueColor(Tile.FOREGROUND).titleColor(Tile.FOREGROUND).unitColor(Tile.FOREGROUND)
				.barColor(Tile.BLUE).needleColor(Tile.FOREGROUND).barColor(Tile.BLUE)
				.barBackgroundColor(Tile.BACKGROUND.darker()).tickLabelColor(Tile.FOREGROUND)
				.majorTickMarkColor(Tile.FOREGROUND).minorTickMarkColor(Tile.FOREGROUND)
				.mediumTickMarkColor(Tile.FOREGROUND).build();
	}

}
