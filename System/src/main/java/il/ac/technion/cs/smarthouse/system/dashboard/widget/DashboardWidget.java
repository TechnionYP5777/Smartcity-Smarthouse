/**
 * 
 */
package il.ac.technion.cs.smarthouse.applications.dashboard.model.widget;

import eu.hansolo.medusa.Gauge;
import il.ac.technion.cs.smarthouse.applications.dashboard.model.InfoCollector;
import il.ac.technion.cs.smarthouse.applications.dashboard.model.WidgetType;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;

/**
 * @author Elia Traore
 * @since Jun 3, 2017
 */
public class DashboardWidget extends BasicWidget {

	public DashboardWidget(WidgetType t, Double tileSize, InfoCollector data) {
		super(t,tileSize, data);

		if (WidgetType.BASIC_DASHBOARD.equals(type)) 
			builder.unit(data.getUnit());//.threshold(75.0);
		if (WidgetType.NEEDLE_DASHBOARD.equals(type))
			((Gauge) getTile().getGraphic()).setUnit(data.getUnit());
	}

	@Override
	public String getTitle() {
		return "Dashboard Widget";
	}

	public void update(Number value, String key) {
		if (WidgetType.NEEDLE_DASHBOARD.equals(type))
			((Gauge) getTile().getGraphic()).setValue((double)value);
		else
			super.update(value,key);
	}

}
