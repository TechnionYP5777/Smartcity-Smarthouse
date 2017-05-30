package il.ac.technion.cs.smarthouse.utils;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

/**
 * A class for easy String conversions
 * @author RON
 * @since 30-05-2017
 */
public class StringConverter {
    /**
     * Convert a string into any class
     * @param targetType
     * @param text
     * @return
     */
    public static Object convert(Class<?> targetType, String text) {
        if (text == null && targetType.isPrimitive())
            return 0;//TODO: what about other types? RON?
        PropertyEditor editor = PropertyEditorManager.findEditor(targetType);
        editor.setAsText(text);
        return editor.getValue();
    }
}
