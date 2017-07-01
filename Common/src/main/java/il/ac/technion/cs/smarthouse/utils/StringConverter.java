package il.ac.technion.cs.smarthouse.utils;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class for easy String conversions
 * 
 * @author RON
 * @since 30-05-2017
 */
public class StringConverter {
    private static Logger log = LoggerFactory.getLogger(StringConverter.class);

    /**
     * Convert a string into any class
     * 
     * @param targetType
     *            The class
     * @param text
     *            The string
     * @return the new converted Object
     * 
     *         [[SuppressWarningsSpartan]]
     */
    public static Object convert(final Class<?> targetType, final String text) {
        if (targetType.isPrimitive()) {
            if (text == null)
                return targetType.equals(Character.TYPE) ? '\0' : targetType.equals(Boolean.TYPE) ? false : 0;
            if (targetType.equals(Character.TYPE))
                return text.charAt(0);
        }

        final PropertyEditor editor = PropertyEditorManager.findEditor(targetType);
        if (editor == null) {
            log.warn("\n\tConverting an uknown Object to null: [targetType: " + targetType.getCanonicalName() + "]");
            return null;
        }
        editor.setAsText(text);
        return editor.getValue();
    }
}
