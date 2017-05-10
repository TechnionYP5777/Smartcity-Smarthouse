package il.ac.technion.cs.smarthouse.system.gui.main_system;

import il.ac.technion.cs.smarthouse.mvp.SystemPresenter;
import il.ac.technion.cs.smarthouse.mvp.SystemPresenter.PresenterInfo;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainSystemGui extends Application {
    private static final String APP_NAME = "Smarthouse";

    private PresenterInfo presenterInfo;

    public static void main(final String[] args) {
        launch(args);
    }

    @Override public void start(final Stage s) throws Exception {
        System.out.println("Initializing system ui...");

        final Scene scene = new Scene(getRoot(), 1000, 800);
        s.setTitle(APP_NAME);
        s.getIcons().add(new Image(getClass().getResourceAsStream("/icons/smarthouse-icon.png")));
        s.setScene(scene);
        s.show();

        s.setOnCloseRequest(e -> kill());
    }

    public void kill() {
        System.out.println("System closing...");
        presenterInfo.getPresenter().cleanModel();
    }

    public synchronized Parent getRoot() {
        try {
            presenterInfo = SystemPresenter.createRootPresenter("main_system_ui.fxml");
            notifyAll();
            return (Parent) presenterInfo.getRootViewNode();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
    
    public synchronized MainSystemGuiController getPresenter() throws InterruptedException {
        while (presenterInfo == null)
            wait();
        return presenterInfo.getPresenter();
    }
}
