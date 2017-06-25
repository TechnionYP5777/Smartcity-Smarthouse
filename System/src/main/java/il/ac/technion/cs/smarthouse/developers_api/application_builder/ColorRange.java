package il.ac.technion.cs.smarthouse.developers_api.application_builder;

import java.util.ArrayList;
import java.util.List;

import il.ac.technion.cs.smarthouse.developers_api.application_builder.GuiBinderObject;
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
        private final GuiBinderObject<T> dataObject;
        final Color color;

        ColorAndValue(final GuiBinderObject<T> dataObject, final Color color) {
            assert dataObject != null;
            this.isRegularValue = false;
            this.value = null;
            this.dataObject = dataObject;
            this.color = color;
        }

        ColorAndValue(final T value, final Color color) {
            this.isRegularValue = true;
            this.value = value;
            this.dataObject = null;
            this.color = color;
        }

        T getValue() {
			return isRegularValue ? value : dataObject.getData();
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

    public ColorRange<T> addRange(final GuiBinderObject<T> from, final Color c) {
        assert from != null;
        rangeList.add(new ColorAndValue(from, c));
        return this;
    }

    public ColorRange<T> addIfEquals(final T value, final Color c) {
        assert value != null;
        ifEqualsList.add(new ColorAndValue(value, c));
        return this;
    }

    public ColorRange<T> addIfEquals(final GuiBinderObject<T> value, final Color c) {
        assert value != null;
        ifEqualsList.add(new ColorAndValue(value, c));
        return this;
    }

    public Color getColorOfValue(T value) {
		if (value == null)
			return defaultColor;
		for (ColorAndValue colorAndValue : ifEqualsList)
			if (value.equals(colorAndValue.getValue()))
				return colorAndValue.color;
		ColorAndValue choosenCv = null;
		for (ColorAndValue colorAndValue : rangeList)
			if (value.compareTo(colorAndValue.getValue()) >= 0
					&& (choosenCv == null || choosenCv.getValue().compareTo(colorAndValue.getValue()) < 0))
				choosenCv = colorAndValue;
		return choosenCv == null ? defaultColor : choosenCv.color;
	}
}
