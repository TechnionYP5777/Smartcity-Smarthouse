package il.ac.technion.cs.smarthouse.developers_api.application_builder;

import java.util.Map;
import java.util.function.Function;

import il.ac.technion.cs.smarthouse.system.dashboard.InfoCollector;
import il.ac.technion.cs.smarthouse.system.dashboard.WidgetType;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorApi;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorData;

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
    
    public <T extends SensorData> WidgetsRegionBuilder addWidget(final WidgetType type, final InfoCollector info, 
                                        final SensorApi<T> sensor, final Function<T,Map<String,Object>> sensorProcessor);
}
