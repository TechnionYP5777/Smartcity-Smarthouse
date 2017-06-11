package il.ac.technion.cs.smarthouse.developers_api.application_builder.implementations;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import il.ac.technion.cs.smarthouse.developers_api.application_builder.ConfigurationsRegionBuilder;
import il.ac.technion.cs.smarthouse.developers_api.application_builder.DataObject;
import il.ac.technion.cs.smarthouse.javafx_elements.AppBooleanButtonField;
import il.ac.technion.cs.smarthouse.javafx_elements.AppButton;
import il.ac.technion.cs.smarthouse.javafx_elements.AppComboBoxField;
import il.ac.technion.cs.smarthouse.javafx_elements.AppSpinnerField;
import il.ac.technion.cs.smarthouse.javafx_elements.AppTextField;
import javafx.scene.Node;

/**
 * Implementation of {@link ConfigurationsRegionBuilder}
 * 
 * @author RON
 * @since 10-06-2017
 */
public final class ConfigurationsRegionBuilderImpl extends AbstractRegionBuilder implements ConfigurationsRegionBuilder {
    public ConfigurationsRegionBuilderImpl() {
        super.setTitle("Configurations");
    }
    
    private <T> ConfigurationsRegionBuilderImpl aux(String title, DataObject<T> bindingDataObject, T initialValueDefault, BiFunction<Consumer<T>, T, Node> nodeCreatorFunction) {
        T initialValue = Optional.ofNullable(bindingDataObject.getData()).orElse(initialValueDefault);
        addAppBuilderItem(new AppBuilderItem(title, nodeCreatorFunction.apply(bindingDataObject::setData, initialValue)));
        return this;
    }
    
    @Override
    public ConfigurationsRegionBuilderImpl setTitle(String title) {
        super.setTitle(title);
        return this;
    }
    
    @Override
    public ConfigurationsRegionBuilderImpl addStringInputField(String title, DataObject<String> bindingDataObject) {
//        String initialValue = Optional.ofNullable(bindingDataObject.getData()).orElse("");
//        addAppBuilderItem(new AppBuilderItem(title, new AppTextField(bindingDataObject::setData, initialValue)));
        return aux(title, bindingDataObject, "", AppTextField::new);
    }

    @Override
    public ConfigurationsRegionBuilderImpl addDoubleInputField(String title, DataObject<Double> bindingDataObject) {
//        double initialValue = Optional.ofNullable(bindingDataObject.getData()).orElse(0.0);
//        addAppBuilderItem(new AppBuilderItem(title, AppSpinnerField.createDoubleAppSpinner(bindingDataObject::setData, initialValue)));
        return aux(title, bindingDataObject, 0.0, AppSpinnerField::createDoubleAppSpinner);
    }

    @Override
    public ConfigurationsRegionBuilderImpl addIntegerInputField(String title, DataObject<Integer> bindingDataObject) {
//        int initialValue = Optional.ofNullable(bindingDataObject.getData()).orElse(0);
//        addAppBuilderItem(new AppBuilderItem(title, AppSpinnerField.createIntegerAppSpinner(bindingDataObject::setData, initialValue)));
        return aux(title, bindingDataObject, 0, AppSpinnerField::createIntegerAppSpinner);
    }

    @Override
    public <T> ConfigurationsRegionBuilderImpl addComboboxInputField(String title, DataObject<T> bindingDataObject,
                    @SuppressWarnings("unchecked") T... comboOptions) {
        assert comboOptions.length > 0;
        T initialValue = Optional.ofNullable(bindingDataObject.getData()).orElse(comboOptions[0]);
        addAppBuilderItem(new AppBuilderItem(title, new AppComboBoxField<>(bindingDataObject::setData, initialValue, comboOptions)));
        return this;
    }

    @Override
    public ConfigurationsRegionBuilderImpl addButtonToggleField(String title, DataObject<Boolean> bindingDataObject) {
//        boolean initialValue = Optional.ofNullable(bindingDataObject.getData()).orElse(false);
//        addAppBuilderItem(new AppBuilderItem(title, new AppBooleanButtonField(bindingDataObject::setData, initialValue)));
        return aux(title, bindingDataObject, false, AppBooleanButtonField::new);
    }
    
    @Override
    public <T> ConfigurationsRegionBuilderImpl addButtonInputField(String title, String textOnButton, DataObject<T> bindingDataObject) {
        addAppBuilderItem(new AppBuilderItem(title, new AppButton(textOnButton, bindingDataObject::notifyListeners)));
        return this;
    }
}
