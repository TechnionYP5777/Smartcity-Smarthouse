package il.ac.technion.cs.smarthouse.utils;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public enum JavaFxHelper {
    ;
    
    private static Logger log = LoggerFactory.getLogger(JavaFxHelper.class);

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
                    new Thread(() -> functionToRun.accept(x)).start();
                }
        };
    }
    
    public static void placeNodeInPane(Node n, Pane parent) {
        AnchorPane.setTopAnchor(n, 0.0);
        AnchorPane.setRightAnchor(n, 0.0);
        AnchorPane.setLeftAnchor(n, 0.0);
        AnchorPane.setBottomAnchor(n, 0.0);

        parent.getChildren().setAll(n);
    }
    
    /**
     * Checks if the JavaFx thread was initiated
     * @return true if the JavaFx thread was initiated, or false otherwise
     */
    public static boolean isJavaFxThreadStarted() {
        try {
            Platform.runLater(() -> {});
        } catch (IllegalStateException __) {
            return false;
        }
        return true;
    }
    
    /**
     * starts a {@link javafx.application.Application}
     * @param a the application
     * @param args
     */
    public static <T extends Application> T startGui(T a, String... args) {
        DummyApplication.app2launch = a;
        
        try {
            Platform.runLater(() -> {
                try {
                    new DummyApplication().start(new Stage());
                } catch (Exception e) {
                    log.error("couldn't start the DummyApplication with: " + (a == null ? null : a.getClass().getName()), e);
                }
            });
        } catch (IllegalStateException __) {
            new Thread(() -> Application.launch(DummyApplication.class, args)).start();
        }
        
        return a;
    }
    
    public static class DummyApplication extends Application {
        static Application app2launch;
        
        @Override public void start(Stage primaryStage) throws Exception {
            if (app2launch != null)
                app2launch.start(primaryStage);
        }
    }
}
