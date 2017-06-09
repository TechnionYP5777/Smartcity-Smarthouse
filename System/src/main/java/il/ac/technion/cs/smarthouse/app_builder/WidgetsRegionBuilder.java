package il.ac.technion.cs.smarthouse.app_builder;

/**
 * GUI layout - widgets region
 * <p>
 * A region for custom widgets
 * @author RON
 * @since 10-06-2017
 */
public class WidgetsRegionBuilder extends RegionBuilder {
    public WidgetsRegionBuilder() {
        super.setTitle("Widgets");
    }
    
    @Override
    public WidgetsRegionBuilder setTitle(String title) {
        super.setTitle(title);
        return this;
    }
    
    //TODO: Ron + Elia
}
