package il.ac.technion.cs.smarthouse.app_builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A linkable data object
 * <p>
 * Use this object to link data fields to the GUI's configuration and status fields
 * @author RON
 * @since 10-06-2017
 */
public class DataObject<T> {
    private T data;
    private List<Consumer<Optional<T>>> dataChangedListeners = new ArrayList<>();
    
    public DataObject() {
    }
    
    public DataObject(T initialValue) {
        this();
        data = initialValue;
    }
    
    public T getData() {
        return data;
    }
    
    public String getDataStr() {
        return data != null ? data.toString() : "";
    }
    
    public DataObject<T> setData(T newData) {
        data = newData;
        dataChangedListeners.forEach(f->f.accept(Optional.ofNullable(data)));
        return this;
    }
    
    public DataObject<T> addOnDataChangedListener(Consumer<Optional<T>> listener) {
        dataChangedListeners.add(listener);
        return this;
    }

}
