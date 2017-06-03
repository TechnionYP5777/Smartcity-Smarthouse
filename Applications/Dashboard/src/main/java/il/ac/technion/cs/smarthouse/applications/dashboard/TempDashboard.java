package il.ac.technion.cs.smarthouse.applications.dashboard;

import java.util.Locale;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.Tile.SkinType;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class TempDashboard extends Application {
    @FXML TextField path;
    private static final double TILE_SIZE = 150;
    private Tile              textTile;
    private FlowPane pane;
    
    private static int lastLoc;
    

    public static void main(String[] args) {
        launch(args);

    }
    
    @Override
    public void init() {
        textTile = TileBuilder.create()
                        .prefSize(TILE_SIZE, TILE_SIZE)
                        .skinType(SkinType.TEXT)
                        .title("Text Tile")
                        .text("Whatever text")
                        .description("Click Meeeee :>")
                        .textVisible(true)
                        .build();
        textTile.setOnMouseClicked(e -> {
        lastLoc = 0;
        openConfiguration();});
    }
    
    public void openConfiguration() {
        System.out.println("Clicked!");
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dashboard_configuration_ui.fxml"));
            final Parent root1 = (Parent) fxmlLoader.load();
            final Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
            ConfigurationController controller = fxmlLoader.getController();
//            controller.setComboBox();
//            controller.setParentController(this);
//            ((ConfigController) fxmlLoader.getController()).subscribe(StoveAppController.this);
                } catch (final Exception $) {
                    // TODO: handle error
                    System.out.println("Oops...");
                    $.printStackTrace();
                }
        
    }
    
    public void updateTile(String type) {
        switch(type) {
            case "Clock":
                Tile clockTile = TileBuilder.create()
                .prefSize(150, 150)
                .skinType(SkinType.CLOCK)
                .title("Clock Tile")
                .text("Whatever text")
                .dateVisible(true)
                .locale(Locale.US)
                .running(true)
                .build();
                clockTile.setOnMouseClicked(e-> {openConfiguration();});
                pane.getChildren().set(lastLoc, clockTile);
            case "Numeric":
                break;
            case "Pie Chart":
                break;
            default:
                break;
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        pane = new FlowPane(Orientation.HORIZONTAL, 1, 1, textTile);
        pane.setPadding(new Insets(5));
        pane.setPrefSize(150, 150);
        pane.setBackground(
                        new Background(new BackgroundFill(Tile.BACKGROUND.darker(), CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(pane);

        stage.setTitle("Dashboard Configuration in Action");
        stage.setScene(scene);
        stage.show();
    }

    private Gauge createGauge(final Gauge.SkinType TYPE) {
        return GaugeBuilder.create().skinType(TYPE).prefSize(TILE_SIZE, TILE_SIZE).animated(true)
                        // .title("")
                        .unit("\u00B0C")
                        .valueColor(Tile.FOREGROUND).titleColor(Tile.FOREGROUND).unitColor(Tile.FOREGROUND)
                        .barColor(Tile.BLUE).needleColor(Tile.FOREGROUND).barColor(Tile.BLUE)
                        .barBackgroundColor(Tile.BACKGROUND.darker()).tickLabelColor(Tile.FOREGROUND)
                        .majorTickMarkColor(Tile.FOREGROUND).minorTickMarkColor(Tile.FOREGROUND)
                        .mediumTickMarkColor(Tile.FOREGROUND).build();
    }

    @Override
    public void stop() {
        System.exit(0);
    }

}
