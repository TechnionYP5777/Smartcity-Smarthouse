package il.ac.technion.cs.smarthouse.developers_api.application_builder;

/**
 * GUI layout - status region
 * <p>
 * A region for read-only fields
 * 
 * @author RON
 * @since 10-06-2017
 */
public interface StatusRegionBuilder {
    public StatusRegionBuilder setTitle(String title);

    public <T> StatusRegionBuilder addStatusField(String title, GuiBinderObject<T> bindingDataObject);

    public <T extends Comparable<T>> StatusRegionBuilder addStatusField(String title, GuiBinderObject<T> bindingDataObject,
                    ColorRange<T> r);

    public StatusRegionBuilder addTimerStatusField(String title, GuiBinderObject<Boolean> timerToggle,
                    GuiBinderObject<Double> timerDuration);

    public StatusRegionBuilder addTimerStatusField(String title, GuiBinderObject<Boolean> timerToggle,
                    GuiBinderObject<Double> timerDuration, ColorRange<Double> d);
}
