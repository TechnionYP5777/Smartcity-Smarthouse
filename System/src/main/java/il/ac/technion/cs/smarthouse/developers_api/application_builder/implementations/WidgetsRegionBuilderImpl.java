package il.ac.technion.cs.smarthouse.developers_api.application_builder.implementations;

import il.ac.technion.cs.smarthouse.developers_api.application_builder.WidgetsRegionBuilder;
import il.ac.technion.cs.smarthouse.system.dashboard.InfoCollector;
import il.ac.technion.cs.smarthouse.system.dashboard.WidgetType;
import il.ac.technion.cs.smarthouse.system.dashboard.DashboardCore;
import il.ac.technion.cs.smarthouse.system.dashboard.DashboardCore.Widget;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;

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

	@Override
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
		Double size = tileSize + 30;
		scrollPane.setMaxHeight(size);
		scrollPane.setMinHeight(size);
		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);

		addAppBuilderItem(new AppBuilderItem(null, scrollPane));
	}

	public WidgetsRegionBuilder setDashboardCore(DashboardCore c) {
		this.core = c;
		return this;
	}

	@Override
	public WidgetsRegionBuilder addWidget(WidgetType t, InfoCollector c) {
		if (core == null)
			return this;

		if (widgetsHbox == null)
			initWidgetPane();

		Widget w = core.createWidget(t, c, tileSize);
		widgetsHbox.getChildren().add(w.get());
		return this;
	}

}
