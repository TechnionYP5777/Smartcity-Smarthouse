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
     * @return the new converted Object
     * [[SuppressWarningsSpartan]]
     */
    public static Object convert(Class<?> targetType, String text) {
        if (text == null && targetType.isPrimitive())
            return targetType.equals(Boolean.TYPE) ? false : 0;
        PropertyEditor editor = PropertyEditorManager.findEditor(targetType);
        editor.setAsText(text);
        return editor.getValue();
    }
}
