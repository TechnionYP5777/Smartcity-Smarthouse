package il.ac.technion.cs.smarthouse.developers_api.application_builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.scene.paint.Color;

/**
 * Used by the {@link StatusRegionBuilder} to set the color of the text fields
 * 
 * @author RON
 * @since 11-06-2017
 */
public class ColorRange<T extends Comparable<T>> {

    class ColorAndValue {
        private final boolean isRegularValue;
        private final T value;
        private final DataObject<T> dataObject;
        final Color color;

        ColorAndValue(final DataObject<T> dataObject, final Color c) {
            assert dataObject != null;
            this.isRegularValue = false;
            this.value = null;
            this.dataObject = dataObject;
            this.color = c;
        }

        ColorAndValue(final T value, final Color c) {
            this.isRegularValue = true;
            this.value = value;
            this.dataObject = null;
            this.color = c;
        }

        T getValue() {
            if (isRegularValue)
                return value;
            return dataObject.getData();
        }
    }

    Color defaultColor = Color.BLACK;
    final List<ColorAndValue> rangeList = new ArrayList<>();
    final List<ColorAndValue> ifEqualsList = new ArrayList<>();

    public ColorRange() {}

    public ColorRange(final Color defaultColor) {
        if (defaultColor != null)
            this.defaultColor = defaultColor;
    }

    public ColorRange<T> addRange(final T from, final Color c) {
        assert from != null;
        rangeList.add(new ColorAndValue(from, c));
        return this;
    }

    public ColorRange<T> addRange(final DataObject<T> from, final Color c) {
        assert from != null;
        rangeList.add(new ColorAndValue(from, c));
        return this;
    }

    public ColorRange<T> addIfEquals(final T value, final Color c) {
        assert value != null;
        ifEqualsList.add(new ColorAndValue(value, c));
        return this;
    }

    public ColorRange<T> addIfEquals(final DataObject<T> value, final Color c) {
        assert value != null;
        ifEqualsList.add(new ColorAndValue(value, c));
        return this;
    }

    public Color getColorOfValue(T value) {
        Color outColor = defaultColor;

        if (value == null)
            return outColor;

        for (ColorAndValue colorAndValue : ifEqualsList)
            if (value.equals(colorAndValue.getValue()))
                return colorAndValue.color;

        for (ColorAndValue colorAndValue : rangeList)
            if (value.compareTo(colorAndValue.getValue()) >= 0)
                outColor = Optional.ofNullable(colorAndValue.color).orElse(defaultColor);

        return outColor;
    }
}
