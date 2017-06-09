package il.ac.technion.cs.smarthouse.app_builder;

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
