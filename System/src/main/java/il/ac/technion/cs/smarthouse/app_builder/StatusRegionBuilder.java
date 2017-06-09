package il.ac.technion.cs.smarthouse.app_builder;

import java.util.Optional;
import java.util.function.Function;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * GUI layout - status region
 * <p>
 * A region for read-only fields
 * @author RON
 * @since 10-06-2017
 */
public class StatusRegionBuilder extends RegionBuilder {
    public StatusRegionBuilder() {
        super.setTitle("Status");
    }

    @Override
    public StatusRegionBuilder setTitle(String title) {
        super.setTitle(title);
        return this;
    }

    public <T> StatusRegionBuilder addStatusField(String title, DataObject<T> bindingDataObject) {
        return addStatusField(title, bindingDataObject, null);
    }

    public <T> StatusRegionBuilder addStatusField(String title, DataObject<T> bindingDataObject,
                    Function<Optional<T>, Color> colorFunction) {
        final Label l = new Label(bindingDataObject.getDataStr());
        l.setFont(Font.font(14));
        
        bindingDataObject.addOnDataChangedListener(d -> {
            l.setText(d.get() + "");
            if (colorFunction != null)
                l.setTextFill(colorFunction.apply(d));
        });
        addAppBuilderItem(new AppBuilderItem(title, l));
        return this;
    }
}
