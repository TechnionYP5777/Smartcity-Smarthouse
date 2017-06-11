package il.ac.technion.cs.smarthouse.developers_api.application_builder;

/**
 * Allows the developer to build the GUI's layout
 * 
 * @author RON
 * @since 10-06-2017
 */
public interface AppBuilder {
    public ConfigurationsRegionBuilder getConfigurationsRegionBuilder();

    public StatusRegionBuilder getStatusRegionBuilder();

    public WidgetsRegionBuilder getWidgetsRegionBuilder();

    public CustomRegionBuilder getCustomRegionBuilder();
}
