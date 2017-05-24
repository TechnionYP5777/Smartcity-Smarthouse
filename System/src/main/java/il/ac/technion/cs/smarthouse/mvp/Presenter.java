package il.ac.technion.cs.smarthouse.mvp;

import il.ac.technion.cs.smarthouse.utils.BoolLatch;
import javafx.fxml.Initializable;

public abstract class Presenter<T> implements Initializable {
    private T myModel;
    private final BoolLatch startedLatch = new BoolLatch();

    public T setModel(final T model) {
        if (myModel != null)
            return myModel;
        myModel = model;
        bind(model);
        return myModel;
    }

    protected T getModel() {
        return myModel;
    }

    public void waitUntilLoaded() {
        startedLatch.blockUntilTrue();
    }

    protected void notifyOnLoaded() {
        startedLatch.setTrueAndRelease();
    }

    protected abstract void bind(T model1);
}
