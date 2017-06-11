package il.ac.technion.cs.smarthouse.developers_api;

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
public final class DataObject<T> {
    private T data;
    private List<Consumer<DataObject<T>>> dataChangedListeners = new ArrayList<>();
    
    public DataObject() {
    }
    
    public DataObject(T initialValue) {
        this();
        data = initialValue;
    }
    
    public T getData() {
        return data;
    }
    
    public String getDataAsString() {
        return data != null ? data.toString() : "";
    }
    
    public Optional<T> getDataAsOptional() {
        return Optional.ofNullable(data);
    }
    
    public DataObject<T> setData(T newData) {
        if (data != newData) {
            data = newData;
            notifyListeners();
        }
        return this;
    }
    
    public void notifyListeners() {
        dataChangedListeners.forEach(f->f.accept(this));
    }
    
    public DataObject<T> addOnDataChangedListener(Consumer<DataObject<T>> listener) {
        dataChangedListeners.add(listener);
        return this;
    }

}
