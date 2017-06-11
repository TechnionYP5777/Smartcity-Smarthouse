package il.ac.technion.cs.smarthouse.developers_api;

/**
 * GUI layout - widgets region
 * <p>
 * A region for custom widgets
 * @author RON
 * @since 10-06-2017
 */
public final class WidgetsRegionBuilder extends AbstractRegionBuilder {
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
