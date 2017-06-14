package il.ac.technion.cs.smarthouse.developers_api.application_builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A linkable data object
 * <p>
 * Use this object to link data fields to the GUI's configuration and status
 * fields
 * 
 * @author RON
 * @since 10-06-2017
 */
public final class GuiBinderObject<T> {
    private T data;
    private List<Consumer<GuiBinderObject<T>>> dataChangedListeners = new ArrayList<>();

    public GuiBinderObject() {}

    public GuiBinderObject(T initialValue) {
        this();
        data = initialValue;
    }

    public T getData() {
        return data;
    }
    
    public T getData(T orElseValue) {
        return getDataAsOptional().orElse(orElseValue);
    }

    public String getDataAsString() {
        return data != null ? data.toString() : "";
    }

    public Optional<T> getDataAsOptional() {
        return Optional.ofNullable(data);
    }

    public GuiBinderObject<T> setData(T newData) {
        if (data != newData) {
            data = newData;
            notifyListeners();
        }
        return this;
    }

    public void notifyListeners() {
        dataChangedListeners.forEach(f -> f.accept(this));
    }

    public GuiBinderObject<T> addOnDataChangedListener(Consumer<GuiBinderObject<T>> listener) {
        dataChangedListeners.add(listener);
        return this;
    }

    public GuiBinderObject<T> addOnDataChangedListener(Runnable listener) {
        dataChangedListeners.add(t -> listener.run());
        return this;
    }

}
