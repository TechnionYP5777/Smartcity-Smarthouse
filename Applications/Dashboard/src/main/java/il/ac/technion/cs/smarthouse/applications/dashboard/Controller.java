/**
 * 
 */
package il.ac.technion.cs.smarthouse.applications.dashboard;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.Tile.SkinType;
import il.ac.technion.cs.smarthouse.applications.dashboard.model.InfoCollector;
import il.ac.technion.cs.smarthouse.applications.dashboard.model.WidgetType;
import il.ac.technion.cs.smarthouse.applications.dashboard.model.widget.BasicWidget;
import il.ac.technion.cs.smarthouse.system.services.file_system_service.FileSystemService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.application.Application;


/**
 * @author Elia Traore
 * @since May 29, 2017
 */
public class Controller implements Initializable { 
	private static Logger log = LoggerFactory.getLogger(Controller.class);
	private FileSystemService fileSystem;
	
	public static double TILE_SIZE = 220;
	public static int TILES_IN_ROW = 3;
	public static int ROWS_NUM = 3;
	public static int index = 0;
	
	@FXML public FlowPane pane;

	private void setTileEventHandlers(final Tile t, final Integer position) {
		t.setOnMouseClicked(e -> openConfiguration(position));
	}

	private void updateTile(WidgetType type, final InfoCollector info, Integer position) {
		log.info("creating tile for: type=[" + type + "], paths=[" + info.getInfoEntries() + "], pos=[" + position + "]");

		Optional.ofNullable(type.createWidget(TILE_SIZE, info))
				.ifPresent(widget -> {
					Tile tile = widget.getTile();
					setTileEventHandlers(tile, position);
					pane.getChildren().set(position, tile);
					widget.updateAutomaticallyFrom(fileSystem);
				});

	}

	private void openConfiguration(Integer position) {
		try {
			URL location = getClass().getResource("/config_window_ui.fxml");
			final FXMLLoader fxmlLoader = new FXMLLoader(location);
			final Parent root1 = (Parent) fxmlLoader.load();
			final Stage stage = new Stage();
			stage.setTitle("Configuration Window");
//			ConfigController controller = fxmlLoader.getController();
//			controller.setListenablePaths(fileSystem.ge);
//			controller.SetCallback(() -> updateTile(controller.getChosenType(), controller.getChosenPath(), position));
			stage.setScene(new Scene(root1));
			stage.show();
		} catch (final Exception $) {
			// TODO: handle error
			System.out.println("Oops...");
			$.printStackTrace();
		}

	}

	@Override
	public void initialize(final URL location, final ResourceBundle __) {
		pane.setPrefSize(1200, 800);

		List<Tile> tiles = new ArrayList<>();
		for (int i = 0; i < 1; ++i) {
			final Integer loc = i;
			final Tile t = TileBuilder.create().prefSize(TILE_SIZE, TILE_SIZE).skinType(SkinType.TEXT)
					.description("Choose a widget:\nClick me!\t\t").descriptionAlignment(Pos.CENTER).textVisible(true)
					.build();
			setTileEventHandlers(t, loc);
			tiles.add(t);
		}

		pane.getChildren().addAll(tiles);
		pane.setBackground(new Background(new BackgroundFill(Tile.BACKGROUND.darker(), null, null)));

		// pane.setPadding(new Insets(5));
		// pane.setPrefHeight((TILE_SIZE+8)*ROWS_NUM);
		// pane.setPrefWidth((TILE_SIZE+8)*TILES_IN_ROW);

	}

	public void setFileSystem(FileSystemService fs) {
		this.fileSystem = fs;
	}

}
