package il.ac.technion.cs.smarthouse.developers_api.application_builder;

import il.ac.technion.cs.smarthouse.system.dashboard.DashboardCore;
import il.ac.technion.cs.smarthouse.system.dashboard.InfoCollector;
import il.ac.technion.cs.smarthouse.system.dashboard.WidgetType;

/**
 * GUI layout - widgets region
 * <p>
 * A region for custom widgets
 * @author RON
 * @since 10-06-2017
 */
public interface WidgetsRegionBuilder {    
    public WidgetsRegionBuilder setTitle(String title);
        
    public WidgetsRegionBuilder addWidget(WidgetType type, InfoCollector info);
}
