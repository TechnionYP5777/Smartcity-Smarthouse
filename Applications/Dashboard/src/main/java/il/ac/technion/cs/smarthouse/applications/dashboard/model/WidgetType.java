/**
 * 
 */
package il.ac.technion.cs.smarthouse.applications.dashboard.model;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.hansolo.medusa.ClockBuilder;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.Clock.ClockSkinType;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import il.ac.technion.cs.smarthouse.applications.dashboard.model.widget.BasicWidget;
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
	SIMPLE_CLOCK(ClockWidget.class, SkinType.CLOCK),
	MEDUSA_CLOCK(ClockWidget.class, SkinType.CUSTOM), 
	ANALOG_CLOCK(ClockWidget.class, SkinType.TIMER_CONTROL),

	FLUCTUATIONS(PrecenetageWidget.class, SkinType.HIGH_LOW),

	BASIC_DASHBOARD(DashboardWidget.class, SkinType.GAUGE), 
	NEEDLE_DASHBOARD(DashboardWidget.class, SkinType.CUSTOM),

	BAR_CHART(ListWidget.class, SkinType.BAR_CHART), 
	LEAD_CHART(ListWidget.class, SkinType.LEADER_BOARD),

	PROGRESS_LINE_GRAPH(GraphWidget.class, SkinType.SPARK_LINE),
	AREA_GRAPH(GraphWidget.class, SkinType.AREA_CHART), 
	LINES_GRAPH(GraphWidget.class, SkinType.LINE_CHART);
	
	private static Logger log = LoggerFactory.getLogger(WidgetType.class);

	private final SkinType skinType;
	private final Class<? extends BasicWidget> implementingClass;

	private WidgetType(final Class<? extends BasicWidget> implClass, final SkinType skin) {
		this.implementingClass = implClass;
		skinType = skin;
	}

	public static WidgetType fromString(final String name) {
		final List<WidgetType> $ = Arrays.asList(WidgetType.values()).stream()
				.filter(value -> value.name().equalsIgnoreCase(name)).collect(Collectors.toList());
		$.add(null);
		return $.get(0);
	}

	@SuppressWarnings("rawtypes")
	public TileBuilder createTileBuilder(double TILE_SIZE) {
		TileBuilder builder = TileBuilder.create();

		builder.prefSize(TILE_SIZE, TILE_SIZE)
				.maxSize(TILE_SIZE, TILE_SIZE)
				.minSize(TILE_SIZE, TILE_SIZE)
				.text(this.name().replaceAll("_", "  "))// todo:needed?
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
		case NEEDLE_DASHBOARD:
			builder.graphic(createGauge(Gauge.SkinType.INDICATOR, TILE_SIZE));
			break;
		default:
			;
		}

		return builder;
	}
	
	public BasicWidget createWidget(Double tileSize, InfoCollector data) {
		try {
			return implementingClass.getConstructor(this.getClass(), tileSize.getClass(), data.getClass())
							.newInstance(this, tileSize,data);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			log.error("Failed to instantiate new widget tile from:"+this+". Got error:" + e);
			return null;
		}
	}

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
