/**
 * 
 */
package il.ac.technion.cs.smarthouse.applications.dashboard.model.widget;

import il.ac.technion.cs.smarthouse.applications.dashboard.model.WidgetType;

/**
 * @author Elia Traore
 * @since Jun 2, 2017
 */
public class PrecenetageWidget extends BasicWidget {

//	private final String upperTitle, LowerTitle;
	public PrecenetageWidget(WidgetType t, Double constant, String upperTitle){//, String LowerTitle) {
		super(t);
//		this.upperTitle = upperTitle;
//		this.LowerTitle = LowerTitle;
		
		builder.description(upperTitle)
					.referenceValue(constant)
//					.text(LowerTitle)
					.unit("\u0025");
	}
	
	public PrecenetageWidget(WidgetType t){
		this(t, 20.0, "Upper Title");
	}

	public String getTitle(){
		return "Percentage Widget";
	}
	
}
