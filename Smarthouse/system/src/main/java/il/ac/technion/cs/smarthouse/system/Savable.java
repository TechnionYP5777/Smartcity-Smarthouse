package il.ac.technion.cs.smarthouse.system;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;

/**
 * An interface that allows to serialize and deserialize the class, using json.
 * <p>
 * The interface uses Gson, and it is defined to exclude fields without {@link Expose} Annotation
 * @author RON
 * @since 21-04-2017
 */
public interface Savable {
    final GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
    
    /**
     * Serialize the object to Json string
     * @return Json String
     */
    default String toJsonString() {
        return gsonBuilder.create().toJson(this);
    }
    
    /**
     * Populates the object with data from the jsonString.<p>
     * Only fields without {@link Expose} Annotation will be populated.<p>
     * <code>Populate</code> will be called recursively for fields that also implement {@link Savable}<br>
     * (not including collections).<p>
     * Override this function in order to define what to do after populating.<br> 
     * You should still call <code>Savable.super.populate(jsonString);</code>
     * @param jsonString
     * @throws Exception
     */
    default void populate(final String jsonString) throws Exception {
        for (Entry<String, JsonElement> e : new JsonParser().parse(jsonString).getAsJsonObject().entrySet()) {
            Field f = getClass().getDeclaredField(e.getKey());
            f.setAccessible(true);
            
            Object o = gsonBuilder.create().fromJson(e.getValue(), f.getGenericType());
            if (Savable.class.isAssignableFrom(f.getType()))
                ((Savable) o).populate(e.getValue() + "");
            f.set(this, o);
        }
    }
    
    default void populateFromFile(final InputStream s) throws Exception {
        populate(IOUtils.toString(s));
    }
    
    default void toJsonFile(final OutputStream s) throws IOException {
        s.write(Byte.valueOf(gsonBuilder.create().toJson(this)));
    }
}
