package il.ac.technion.cs.smarthouse.system.gui.dashboard;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.hansolo.tilesfx.Tile;
import il.ac.technion.cs.smarthouse.mvp.system.SystemGuiController;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.dashboard.DashboardCore;
import il.ac.technion.cs.smarthouse.system.dashboard.widget.BasicWidget;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class DashboardController extends SystemGuiController {
    private static Logger log = LoggerFactory.getLogger(DashboardController.class);
    
    private static final double TILE_SIZE = 220;
    private FileSystem filesystem;
    private Map<String,BasicWidget> currentWidgets = new HashMap<>();
    private Integer id = 0;
    
    @FXML public FlowPane pane;

    @Override
    protected void initialize(SystemCore model, URL location, ResourceBundle b) {
        filesystem = model.getFileSystem();
        model.getSystemDashboardCore().setWidgetPresenter(this::addWidget);
        model.getSystemDashboardCore().setWidgetRemover(this::removeWidget);
        
        pane.setPrefSize(1200, 800);
        pane.setBackground(new Background(new BackgroundFill(Tile.BACKGROUND.darker(), null, null)));
        pane.setOnMouseClicked(e -> openConfigWindow());
    }

    private void openConfigWindow() {
        try {
            URL location = getClass().getResource("/dashboard_config_window_ui.fxml");
            final FXMLLoader fxmlLoader = new FXMLLoader(location);
            final Parent root1 = (Parent) fxmlLoader.load();
            final Stage stage = new Stage();
            stage.setTitle("Widget Configuration Window");
//            ConfigController controller = fxmlLoader.getController();
//          controller.setListenablePaths(fileSystem.ge);
//          controller.SetCallback(() -> updateTile(controller.getChosenType(), controller.getChosenPath(), position));
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (final Exception $) {
            // TODO: handle error
            System.out.println("Oops...");
            $.printStackTrace();
        }
    }

    private String addWidget(BasicWidget widget){
        widget.getTile().setMaxSize(TILE_SIZE, TILE_SIZE);
        widget.getTile().setMinSize(TILE_SIZE, TILE_SIZE);
        currentWidgets.put(id+"", widget);
        return (id++) + "";
    }
    
    private void removeWidget(String id){
        //todo: works?
        Optional.ofNullable(currentWidgets.get(id)).ifPresent(widget -> pane.getChildren().remove(widget.getTile()));
    }
}
