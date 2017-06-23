package il.ac.technion.cs.smarthouse.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
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
    private static final List<Stage> openStages = new ArrayList<>();

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
     * initializes the javaFx toolkit
     */
    public static void initJavaFxThread() {
        // PlatformImpl.startup(()->{});
        new JFXPanel();
    }

    /**
     * Closes all stages that were opened by
     * {@link #startGui(Application, boolean)}
     */
    public static void closeAllOpenedStages() {
        openStages.forEach(s -> s.close());
    }

    /**
     * starts a {@link javafx.application.Application}
     * 
     * @param jfxApplication
     *            the application
     * @param blockUntilStarted
     *            if true the function blocks until the application is started
     *            (until {@link javafx.application.Application#start(Stage)}
     *            finishes), otherwise the function returns immediately
     * @return
     */
    public static <T extends Application> T startGui(final T jfxApplication, final boolean blockUntilStarted) {
        assert jfxApplication != null;

        // init javafx thread if the thread wasn't initiated yet:
        if (!isJavaFxThreadStarted())
            initJavaFxThread();

        // make a lock that will wait for jfxApplication to start:
        final BoolLatch guiStarted = new BoolLatch();

        try {
            Platform.runLater(() -> {
                try {
                    Stage s = new Stage();
                    jfxApplication.start(s);
                    openStages.add(s);
                    guiStarted.setTrueAndRelease();
                } catch (final Exception e) {
                    log.error("couldn't start " + jfxApplication.getClass().getName(), e);
                }
            });
        } catch (final IllegalStateException ¢) {
            log.error("Platform.runLater failed! This shouldn't happen!", ¢);
        }

        if (blockUntilStarted)
            guiStarted.blockUntilTrue();

        return jfxApplication;
    }

    /**
     * same as {@link JavaFxHelper#startGui(Application, boolean)} with
     * blockUntilStarted = true
     * 
     * @param jfxApplication
     * @return
     */
    public static <T extends Application> T startGui(final T jfxApplication) {
        return startGui(jfxApplication, true);
    }
}
