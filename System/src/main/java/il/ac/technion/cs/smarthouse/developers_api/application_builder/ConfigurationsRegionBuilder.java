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
    
    public ConfigurationsRegionBuilder addStringInputField(String title, DataObject<String> bindingDataObject);

    public ConfigurationsRegionBuilder addDoubleInputField(String title, DataObject<Double> bindingDataObject);

    public ConfigurationsRegionBuilder addIntegerInputField(String title, DataObject<Integer> bindingDataObject);

    public <T> ConfigurationsRegionBuilder addComboboxInputField(String title, DataObject<T> bindingDataObject,
                    @SuppressWarnings("unchecked") T... comboOptions);

    public ConfigurationsRegionBuilder addButtonToggleField(String title, DataObject<Boolean> bindingDataObject);
    
    public <T> ConfigurationsRegionBuilder addButtonInputField(String title, String textOnButton, DataObject<T> bindingDataObject);
}
