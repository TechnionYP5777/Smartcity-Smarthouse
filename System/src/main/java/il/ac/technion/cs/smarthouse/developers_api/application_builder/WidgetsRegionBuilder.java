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
    
    /** Sets the core to instantiate widgets from
     * */
    public WidgetsRegionBuilder setDashboardCore(DashboardCore core);
    
    /** Adds a widget to the app. If {@link WidgetsRegionBuilder#setDashboardCore(DashboardCore)}
     * wasn't call before calling this method, <b>nothing will be added! </b>
     * */
    public WidgetsRegionBuilder addWidget(WidgetType type, InfoCollector info);
}
