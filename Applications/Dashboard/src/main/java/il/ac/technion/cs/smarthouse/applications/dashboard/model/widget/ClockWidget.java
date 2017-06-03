/**
 * 
 */
package il.ac.technion.cs.smarthouse.applications.dashboard.model.widget;

import java.util.Locale;

import il.ac.technion.cs.smarthouse.applications.dashboard.model.WidgetType;

/**
 * @author Elia Traore
 * @since Jun 3, 2017
 */
public class ClockWidget extends BasicWidget {

	public ClockWidget(WidgetType t) {
		super(t);
		builder.locale(Locale.US)
				.dateVisible(true)
				.running(true);
	}
	
	public String getTitle(){
		return "Clock Widget";
	}

}
