package il.ac.technion.cs.smarthouse.applications.dashboard;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.skins.BarChartItem;
import eu.hansolo.tilesfx.skins.LeaderBoardItem;

import il.ac.technion.cs.smarthouse.applications.dashboard.model.WidgetType;
import il.ac.technion.cs.smarthouse.applications.dashboard.model.widget.BasicWidget;
import il.ac.technion.cs.smarthouse.applications.dashboard.model.widget.GraphWidget;
import il.ac.technion.cs.smarthouse.applications.dashboard.model.widget.ListWidget;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;

/**
 * [[SuppressWarningsSpartan]]
 */
public class ConfigurationController implements Initializable {
	private static Logger log = LoggerFactory.getLogger(ConfigurationController.class);

	@FXML
	public TextField path;
	@FXML
	public ComboBox<String> types = new ComboBox<>();
	@FXML
	public Button button;
	// @FXML public ScrollPane sPane;

	@FXML
	public FlowPane pane;

	private Runnable callback;

	private static final Map<String, List<AnimationTimer>> timers = new HashMap<>();
	private static final Map<String, List<BasicWidget>> widgets = initWidgets();
	private static final Color chosenColor = Color.AQUA, enteredTileColor = Color.BISQUE, normalTileColor = Color.WHITE;
	private static WidgetType chosenType;

	private static Map<String, List<BasicWidget>> initWidgets() {
		Map<String, List<BasicWidget>> widgets = new HashMap<>();
		Stream.of(WidgetType.values()).forEach(t -> {
			try {
				BasicWidget widget = null;
				Class implClass = t.getImplementingClass();
				switch (t) {
				case BAR_CHART:
					BarChartItem[] bItems = (BarChartItem[]) ListWidget.getDefaultBarItems().toArray();
					widget = (BasicWidget) implClass.getConstructor(t.getClass(), bItems.getClass()).newInstance(t,
							bItems);
					break;
				case LEAD_CHART:
					LeaderBoardItem[] lItems = (LeaderBoardItem[]) ListWidget.getDefaultBoardItems().toArray();
					widget = (BasicWidget) implClass.getConstructor(t.getClass(), lItems.getClass()).newInstance(t,
							lItems);
					break;
				default:
					widget = (BasicWidget) implClass.getConstructor(t.getClass()).newInstance(t);
					break;
				}
				List<BasicWidget> newlist = widgets.getOrDefault(widget.getTitle(), new ArrayList<>());
				newlist.add(widget);
				widgets.put(widget.getTitle(), newlist);
				addTimer(widget.getTitle(), t, widget);
				setListeners(widget);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				log.error("Failed to instantiate new widget tile. Got error:" + e);
			}
		});
		BasicWidget.setDefaultTileSize(220);// todo:not do it so uglyly
		return widgets;
	}

	private static void setListeners(final BasicWidget widget) {
		Tile t = widget.getTile();
		t.setOnMouseEntered(e -> {
			if (!chosenColor.equals(t.getForegroundColor()))
				t.setForegroundBaseColor(enteredTileColor);
		});
		t.setOnMouseExited(e -> {
			if (!chosenColor.equals(t.getForegroundColor()))
				t.setForegroundBaseColor(normalTileColor);
		});
		t.setOnMouseClicked(e -> {
			chosenType = widget.getType();
			widgets.get(widget.getTitle()).stream()
					.forEach(otherT -> otherT.getTile().setForegroundBaseColor(normalTileColor));
			t.setForegroundBaseColor(chosenColor);
		});
	}

	private static void addTimer(String key, WidgetType type, final BasicWidget widget) {
		List<AnimationTimer> where = timers.getOrDefault(key, new ArrayList<>());
		where.add(new AnimationTimer() {

			private long lastTimerCall;
			final Random RND = new Random();
			final Boolean isGraph = key.equals(new GraphWidget(WidgetType.AREA_GRAPH).getTitle());

			@Override
			public void handle(long now) {
				if (now > lastTimerCall + 500_000_000) {
					if (isGraph)
						GraphWidget.getDefaultSerie().getData().stream()
								.forEach(data -> widget.updateExisting(RND.nextInt(100), data.getXValue()));
					else
						widget.updateExisting(RND.nextDouble() * 100, null);
					lastTimerCall = now;
				}

			}

		});
		timers.put(key, where);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		button.setOnMouseClicked(e -> {
			if (callback != null)
				Platform.runLater(callback);
			timers.values().stream().filter(l -> l != null).flatMap(l -> l.stream()).forEach(t -> t.stop());
			((Stage) button.getScene().getWindow()).close();
			widgets.values().stream().filter(l -> l != null).flatMap(l -> l.stream())
					.forEach(w -> w.getTile().setForegroundBaseColor(normalTileColor));
		});

		pane.setPadding(new Insets(5));
		pane.setBackground(
				new Background(new BackgroundFill(Tile.BACKGROUND.darker(), CornerRadii.EMPTY, Insets.EMPTY)));

		types.setItems(FXCollections.observableArrayList(widgets.keySet()));
		types.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			Optional.ofNullable(timers.get(newValue)).ifPresent(l -> l.stream().forEach(t -> t.start()));
			pane.getChildren()
					.setAll(widgets.get(newValue).stream().map(BasicWidget::getTile).collect(Collectors.toList()));
			Optional.ofNullable(timers.get(oldValue)).ifPresent(l -> l.stream().forEach(t -> t.stop()));
			Optional.ofNullable(widgets.get(oldValue))
					.ifPresent(l -> l.stream().forEach(w -> w.getTile().setForegroundBaseColor(normalTileColor)));
		});

		types.getSelectionModel().select(3);
		;

		// sPane.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		// sPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		// sPane.setContent(pane);
	}

	public WidgetType getChosenType() {
		return chosenType;
	}

	public String getChosenPath() {
		return path.getText();
	}

	public void SetCallback(Runnable r) {
		callback = r;
	}
}