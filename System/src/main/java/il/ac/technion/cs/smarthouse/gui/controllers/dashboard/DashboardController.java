package il.ac.technion.cs.smarthouse.gui.controllers.dashboard;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.TileBuilder;
import il.ac.technion.cs.smarthouse.gui.controllers.SystemGuiController;
import il.ac.technion.cs.smarthouse.gui_controller.GuiController;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.SystemMode;
import il.ac.technion.cs.smarthouse.system.dashboard.DashboardCore;
import il.ac.technion.cs.smarthouse.system.dashboard.widget.BasicWidget;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class DashboardController extends SystemGuiController {
	private static Logger log = LoggerFactory.getLogger(DashboardController.class);

	private FileSystem filesystem;
	private DashboardCore core;

	private static final double TILE_SIZE = 200;
	private final Map<String, BasicWidget> currentWidgets = new HashMap<>();
	private Integer id = 0;

	// ---- GUI elements members -----
	@FXML
	public FlowPane pane;
	private Tile addWidgetTile;

	@Override
	protected <T extends GuiController<SystemCore>> void initialize(final SystemCore model1, final T parent1,
			final SystemMode extraData1, final URL location, final ResourceBundle b) {

		filesystem = model1.getFileSystem();
		core = model1.getSystemDashboardCore();
		core.setWidgetPresenter(this::addWidget);
		core.setWidgetRemover(this::removeWidget);

		final Label plus = new Label("+");
		plus.setFont(Font.font("Arial Black", FontWeight.EXTRA_BOLD, 70)); // font
																			// fam:
																			// Alegreya*
																			// are
																			// cute
																			// too
		plus.setTextFill(Color.ANTIQUEWHITE);
		addWidgetTile = TileBuilder.create().prefSize(TILE_SIZE, TILE_SIZE).skinType(SkinType.CUSTOM).graphic(plus)
				// .roundedCorners(false)
				.build();
		addWidgetTile.setOnMouseClicked(e -> openConfigWindow());

		pane.setBackground(new Background(new BackgroundFill(Tile.BACKGROUND.darker(), null, null)));
		pane.setPrefSize(3000, 3000);
		pane.setPadding(new Insets(5));
		pane.getChildren().add(addWidgetTile);
	}

	private void openConfigWindow() {
		try {
			final ConfigController configController = createChildController("dashboard_config_window_ui.fxml");
			configController.setConfigConsumer((type, info) -> {
				final String wid = addWidget(type.createWidget(TILE_SIZE, info));
				core.registerWidget(wid, currentWidgets.get(wid));
			});
			final Stage stage = new Stage();
			stage.setTitle("Widget Configuration Window");
			stage.setScene(new Scene(configController.getRootViewNode()));
			stage.show();
		} catch (final Exception $) {
			log.error("Failed to open widget configuration window, as a result of:\n\t" + $);
			throw new RuntimeException();
		}
	}

	private String getId() {
		return id + "";
	}

	// returns the old id
	private String incId() {
		return id++ + "";
	}

	private String addWidget(final BasicWidget widget) {
		final String wid = getId();
		widget.getTile().setOnMouseClicked(e -> {
			if (e.getButton().equals(MouseButton.SECONDARY)) {
				final MenuItem deleteOption = new MenuItem("Delete");
				deleteOption.setOnAction(e1 -> removeWidget(wid));
				final ContextMenu popup = new ContextMenu();
				popup.getItems().add(deleteOption);
				popup.show(widget.getTile(), e.getScreenX(), e.getScreenY());
			}
		});
		widget.updateAutomaticallyFrom(filesystem);
		widget.setSize(TILE_SIZE);

		currentWidgets.put(wid, widget);
		pane.getChildren().add(pane.getChildren().indexOf(addWidgetTile), widget.getTile());
		core.registerWidget(wid, widget);

		return incId();
	}

	private void removeWidget(final String givenId) {
		Optional.ofNullable(currentWidgets.get(givenId)).ifPresent(widget -> {
			currentWidgets.remove(givenId);
			pane.getChildren().remove(widget.getTile());
			core.deregisterWidget(givenId);
		});
	}

}
