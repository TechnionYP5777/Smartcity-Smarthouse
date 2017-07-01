package il.ac.technion.cs.smarthouse.developers_api.application_builder.implementations;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.developers_api.application_builder.ConfigurationsRegionBuilder;
import il.ac.technion.cs.smarthouse.developers_api.application_builder.GuiBinderObject;
import il.ac.technion.cs.smarthouse.gui.javafx_elements.AppBooleanButtonField;
import il.ac.technion.cs.smarthouse.gui.javafx_elements.AppButton;
import il.ac.technion.cs.smarthouse.gui.javafx_elements.AppComboBoxField;
import il.ac.technion.cs.smarthouse.gui.javafx_elements.AppSpinnerField;
import il.ac.technion.cs.smarthouse.gui.javafx_elements.AppTextField;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorApi;
import il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorData;
import javafx.scene.Node;

/**
 * Implementation of {@link ConfigurationsRegionBuilder}
 * 
 * @author RON
 * @since 10-06-2017
 */
public final class ConfigurationsRegionBuilderImpl extends AbstractRegionBuilder
                implements ConfigurationsRegionBuilder {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationsRegionBuilderImpl.class);

    public ConfigurationsRegionBuilderImpl() {
        super.setTitle("Configurations");
    }

    private <T> ConfigurationsRegionBuilderImpl aux(String title, GuiBinderObject<T> bindingDataObject,
                    T initialValueDefault, BiFunction<Consumer<T>, T, Node> nodeCreatorFunction) {
        T initialValue = Optional.ofNullable(bindingDataObject.getData()).orElse(initialValueDefault);
        addAppBuilderItem(
                        new AppBuilderItem(title, nodeCreatorFunction.apply(bindingDataObject::setData, initialValue)));
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see il.ac.technion.cs.smarthouse.developers_api.application_builder.
     * implementations.AbstractRegionBuilder#setTitle(java.lang.String)
     */
    @Override
    public ConfigurationsRegionBuilderImpl setTitle(String title) {
        super.setTitle(title);
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see il.ac.technion.cs.smarthouse.developers_api.application_builder.
     * ConfigurationsRegionBuilder#addStringInputField(java.lang.String,
     * il.ac.technion.cs.smarthouse.developers_api.application_builder.
     * GuiBinderObject)
     */
    @Override
    public ConfigurationsRegionBuilderImpl addStringInputField(String title,
                    GuiBinderObject<String> bindingDataObject) {
        return aux(title, bindingDataObject, "", AppTextField::new);
    }

    /*
     * (non-Javadoc)
     * 
     * @see il.ac.technion.cs.smarthouse.developers_api.application_builder.
     * ConfigurationsRegionBuilder#addDoubleInputField(java.lang.String,
     * il.ac.technion.cs.smarthouse.developers_api.application_builder.
     * GuiBinderObject)
     */
    @Override
    public ConfigurationsRegionBuilderImpl addDoubleInputField(String title,
                    GuiBinderObject<Double> bindingDataObject) {
        return aux(title, bindingDataObject, 0.0, AppSpinnerField::createDoubleAppSpinner);
    }

    /*
     * (non-Javadoc)
     * 
     * @see il.ac.technion.cs.smarthouse.developers_api.application_builder.
     * ConfigurationsRegionBuilder#addIntegerInputField(java.lang.String,
     * il.ac.technion.cs.smarthouse.developers_api.application_builder.
     * GuiBinderObject)
     */
    @Override
    public ConfigurationsRegionBuilderImpl addIntegerInputField(String title,
                    GuiBinderObject<Integer> bindingDataObject) {
        return aux(title, bindingDataObject, 0, AppSpinnerField::createIntegerAppSpinner);
    }

    /*
     * (non-Javadoc)
     * 
     * @see il.ac.technion.cs.smarthouse.developers_api.application_builder.
     * ConfigurationsRegionBuilder#addComboboxInputField(java.lang.String,
     * il.ac.technion.cs.smarthouse.developers_api.application_builder.
     * GuiBinderObject, java.lang.Object[])
     */
    @Override
    public <T> ConfigurationsRegionBuilderImpl addComboboxInputField(String title, GuiBinderObject<T> bindingDataObject,
                    @SuppressWarnings("unchecked") T... comboOptions) {
        assert comboOptions.length > 0;
        T initialValue = Optional.ofNullable(bindingDataObject.getData()).orElse(comboOptions[0]);
        addAppBuilderItem(new AppBuilderItem(title,
                        new AppComboBoxField<>(bindingDataObject::setData, initialValue, comboOptions)));
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see il.ac.technion.cs.smarthouse.developers_api.application_builder.
     * ConfigurationsRegionBuilder#addButtonToggleField(java.lang.String,
     * il.ac.technion.cs.smarthouse.developers_api.application_builder.
     * GuiBinderObject)
     */
    @Override
    public ConfigurationsRegionBuilderImpl addButtonToggleField(String title,
                    GuiBinderObject<Boolean> bindingDataObject) {
        return aux(title, bindingDataObject, false, AppBooleanButtonField::new);
    }

    /*
     * (non-Javadoc)
     * 
     * @see il.ac.technion.cs.smarthouse.developers_api.application_builder.
     * ConfigurationsRegionBuilder#addButtonInputField(java.lang.String,
     * java.lang.String,
     * il.ac.technion.cs.smarthouse.developers_api.application_builder.
     * GuiBinderObject)
     */
    @Override
    public <T> ConfigurationsRegionBuilderImpl addButtonInputField(String title, String textOnButton,
                    GuiBinderObject<T> bindingDataObject) {
        addAppBuilderItem(new AppBuilderItem(title, new AppButton(textOnButton, bindingDataObject::notifyListeners)));
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see il.ac.technion.cs.smarthouse.developers_api.application_builder.
     * ConfigurationsRegionBuilder#addSensorAliasSelectionField(java.lang.
     * String,
     * il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorApi)
     */
    @Override
    public <T extends SensorData> ConfigurationsRegionBuilder addSensorAliasSelectionField(final String title,
                    final SensorApi<T> sensorApiObject) {
        return addSensorAliasSelectionField(title, sensorApiObject, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see il.ac.technion.cs.smarthouse.developers_api.application_builder.
     * ConfigurationsRegionBuilder#addSensorAliasSelectionField(java.lang.
     * String,
     * il.ac.technion.cs.smarthouse.system.services.sensors_service.SensorApi,
     * java.util.function.BiConsumer)
     */
    @Override
    public <T extends SensorData> ConfigurationsRegionBuilder addSensorAliasSelectionField(final String title,
                    final SensorApi<T> sensorApiObject, BiConsumer<String, String> aliasesConsumer) {

        final AppComboBoxField<String> cBox = new AppComboBoxField<>(
                        alias -> Optional.ofNullable(alias).ifPresent(newAlias -> {
                            String oldAlias = sensorApiObject.getSensorAlias();
                            if (!newAlias.equals(oldAlias)) {
                                log.info("changed sensor through field! old:" + oldAlias + " new:" + newAlias);
                                sensorApiObject.reselectSensorByAlias(newAlias);
                                Optional.ofNullable(aliasesConsumer).ifPresent(c -> c.accept(oldAlias, newAlias));
                            }
                        }), null, sensorApiObject.getAllAliases().toArray(new String[0]));

        addAppBuilderItem(new AppBuilderItem(title, cBox));

        Consumer<String> c = alias -> {
            final String currValue = cBox.getValue();
            cBox.getItems().clear();
            cBox.getItems().addAll(sensorApiObject.getAllAliases());
            if (!cBox.getItems().isEmpty())
                cBox.setValue(cBox.getItems().contains(currValue) ? currValue : cBox.getItems().get(0));
        };

        sensorApiObject.listenForNewAliases(c);

        c.accept("");

        return this;
    }
}
