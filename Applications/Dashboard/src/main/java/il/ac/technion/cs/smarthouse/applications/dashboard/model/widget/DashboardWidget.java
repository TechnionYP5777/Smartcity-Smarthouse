/**
 * 
 */
package il.ac.technion.cs.smarthouse.applications.dashboard.model.widget;

import eu.hansolo.medusa.Gauge;
import il.ac.technion.cs.smarthouse.applications.dashboard.model.WidgetType;

/**
 * @author Elia Traore
 * @since Jun 3, 2017
 */
public class DashboardWidget extends BasicWidget {

	/**
	 * @param t
	 */
	public DashboardWidget(WidgetType t, Integer threshold, String unit) {
		super(t);

		if(WidgetType.BASIC_DASHBOARD.equals(type)){
				builder.unit(unit)
        				.threshold(threshold);
		}
	}
	
	public DashboardWidget(WidgetType t){
		this(t, 75, "mb");
	}
	
	public String getTitle(){
		return "Dashboard Widget";
	}


	public void updateExisting(Number value, String key){
		if(WidgetType.NEEDLE_BDASHBORD.equals(type))
			((Gauge)getTile().getGraphic()).setValue((double)value);
		else
			super.updateExisting(value, key);
	}
}
