package il.ac.technion.cs.smarthouse.developers_api.application_builder;

/**
 * GUI layout - configurations region
 * <p>
 * A region for editable fields
 * @author RON
 * @since 10-06-2017
 */
public interface ConfigurationsRegionBuilder {
    public ConfigurationsRegionBuilder setTitle(String title);
    
    public ConfigurationsRegionBuilder addStringInputField(String title, GuiBinderObject<String> bindingDataObject);

    public ConfigurationsRegionBuilder addDoubleInputField(String title, GuiBinderObject<Double> bindingDataObject);

    public ConfigurationsRegionBuilder addIntegerInputField(String title, GuiBinderObject<Integer> bindingDataObject);

    public <T> ConfigurationsRegionBuilder addComboboxInputField(String title, GuiBinderObject<T> bindingDataObject,
                    @SuppressWarnings("unchecked") T... comboOptions);

    public ConfigurationsRegionBuilder addButtonToggleField(String title, GuiBinderObject<Boolean> bindingDataObject);
    
    public <T> ConfigurationsRegionBuilder addButtonInputField(String title, String textOnButton, GuiBinderObject<T> bindingDataObject);
}
