package il.ac.technion.cs.smarthouse.utils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * A helper class for javaFx
 * 
 * @author RON
 * @since 11-05-2017
 */
public enum JavaFxHelper {
    ;

    private static Logger log = LoggerFactory.getLogger(JavaFxHelper.class);

    /**
     * Surrounds the given function with a Platform.runLater
     * 
     * @param functionToRun
     *            - the Consumer to surround
     * @return the modified consumer
     */
    public static <T> Consumer<T> surroundConsumerWithFx(final Consumer<T> functionToRun) {
        return x -> {
            if (Platform.isFxApplicationThread())
                functionToRun.accept(x);
            else
                try {
                    Platform.runLater(() -> functionToRun.accept(x));
                } catch (final IllegalStateException __) {
                    new Thread(() -> functionToRun.accept(x)).start();
                }
        };
    }

    public static void placeNodeInPane(final Node n, final Pane parent) {
        AnchorPane.setTopAnchor(n, 0.0);
        AnchorPane.setRightAnchor(n, 0.0);
        AnchorPane.setLeftAnchor(n, 0.0);
        AnchorPane.setBottomAnchor(n, 0.0);

        parent.getChildren().setAll(n);
    }

    /**
     * Checks if the JavaFx thread was initiated
     * 
     * @return true if the JavaFx thread was initiated, or false otherwise
     */
    public static boolean isJavaFxThreadStarted() {
        try {
            Platform.runLater(() -> {});
        } catch (final IllegalStateException __) {
            return false;
        }
        return true;
    }

    /**
     * starts a {@link javafx.application.Application}
     * 
     * @param a
     *            the application
     * @param args
     */
    public static synchronized <T extends Application> T startGui(final T a, final String... args) {
        if (!isJavaFxThreadStarted())
            new Thread(() -> Application.launch(DummyApplication.class, args)).start();

        DummyApplication.started.blockUntilTrue();

        synchronized (DummyApplication.apps2launch) {
            DummyApplication.apps2launch.add(a);
        }

        try {
            Platform.runLater(() -> {
                synchronized (DummyApplication.apps2launch) {
                    while (!DummyApplication.apps2launch.isEmpty()) {
                        DummyApplication.app2launch = DummyApplication.apps2launch.pop();
                        try {
                            new DummyApplication().start(new Stage());
                        } catch (final Exception e) {
                            log.error("couldn't start the DummyApplication with: "
                                            + (DummyApplication.app2launch == null ? null
                                                            : DummyApplication.app2launch.getClass().getName()),
                                            e);
                        }
                        DummyApplication.app2launch = null;
                    }
                }
            });
        } catch (final IllegalStateException e) {
            e.printStackTrace();
        }
        return a;
    }

    /**
     * An important part of the implementation of
     * {@link JavaFxHelper#startGui(Application, String...)}
     * <p>
     * Please don't touch this
     * 
     * @author RON
     * @since 11-05-2017
     */
    public static class DummyApplication extends Application {
        static Deque<Application> apps2launch = new ArrayDeque<>();
        static Application app2launch;
        static BoolLatch started = new BoolLatch();

        @Override
        public void start(final Stage primaryStage) throws Exception {
            started.setTrueAndRelease();
            if (app2launch != null)
                app2launch.start(primaryStage);
            app2launch = null;
        }
    }
}
