package il.ac.technion.cs.smarthouse.developers_api.application_builder.implementations;

import java.util.Map;
import java.util.function.Function;

import il.ac.technion.cs.smarthouse.developers_api.application_builder.WidgetsRegionBuilder;
import il.ac.technion.cs.smarthouse.system.dashboard.InfoCollector;
import il.ac.technion.cs.smarthouse.system.dashboard.WidgetType;
import il.ac.technion.cs.smarthouse.system.dashboard.widget.BasicWidget;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorApi;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorData;
import il.ac.technion.cs.smarthouse.system.dashboard.DashboardCore;
import il.ac.technion.cs.smarthouse.system.dashboard.DashboardCore.Widget;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * Implementation of {@link WidgetsRegionBuilder}
 * 
 * @author RON
 * @since 10-06-2017
 */
public final class WidgetsRegionBuilderImpl extends AbstractRegionBuilder implements WidgetsRegionBuilder {
	private HBox widgetsHbox;
	private Double tileSize = 150.0;
	private DashboardCore core;

	public WidgetsRegionBuilderImpl() {
		super.setTitle("Widgets");
	}

	public WidgetsRegionBuilderImpl setTitle(String title) {
		super.setTitle(title);
		return this;
	}

	private void initWidgetPane() {
		widgetsHbox = new HBox();
		widgetsHbox.setSpacing(5);
		widgetsHbox.setPadding(new Insets(5));

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(widgetsHbox);
		scrollPane.setFitToWidth(true);
		Double size = 1.2 * tileSize;

		scrollPane.setMinHeight(size);
		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);

		addAppBuilderItem(new AppBuilderItem(null, scrollPane));
	}

	public WidgetsRegionBuilder setDashboardCore(DashboardCore c) {
		this.core = c;
		return this;
	}

	private Boolean canAdd() {
		if (core == null)
			return false;

		if (widgetsHbox == null)
			initWidgetPane();
		return true;
	}

	@Override
	public WidgetsRegionBuilder addWidget(WidgetType t, InfoCollector c) {
		if (canAdd())
			widgetsHbox.getChildren().add(core.createWidget(t, c, tileSize).get());

		return this;
	}

	@Override
	public <T extends SensorData> WidgetsRegionBuilder addWidget(final WidgetType t, final InfoCollector c,
			final SensorApi<T> a, final Function<T, Map<String, Object>> sensorProcessor) {
		if (!canAdd())
			return this;
		BasicWidget bw = t.createWidget(tileSize, c);
		a.subscribe(data -> sensorProcessor.apply(data).forEach((path, val) -> bw.update(val, path)));
		widgetsHbox.getChildren().add(core.createWidget(bw).get());
		return this;
	}

}
