package il.ac.technion.cs.smarthouse.utils;

import java.util.function.Consumer;

import javafx.application.Platform;

public enum JavaFxHelper {
    ;

    /** Surrounds the given function with a Platform.runLater
     * @param functionToRun - the Consumer to surround
     * @return the modified consumer */
    public static <T> Consumer<T> surroundConsumerWithFx(final Consumer<T> functionToRun) {
        return x -> {
            if (Platform.isFxApplicationThread())
                functionToRun.accept(x);
            else
                try {
                    Platform.runLater(() -> functionToRun.accept(x));
                } catch (IllegalStateException __) {
                    new Thread(()->functionToRun.accept(x)).start();
                }
        };
    }
}
