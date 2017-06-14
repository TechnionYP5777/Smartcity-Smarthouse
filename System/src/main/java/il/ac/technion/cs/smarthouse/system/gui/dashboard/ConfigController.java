/**
 * 
 */
package il.ac.technion.cs.smarthouse.system.gui.dashboard;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
import il.ac.technion.cs.smarthouse.system.dashboard.InfoCollector;
import il.ac.technion.cs.smarthouse.system.dashboard.WidgetType;
import il.ac.technion.cs.smarthouse.system.dashboard.widget.BasicWidget;
import il.ac.technion.cs.smarthouse.system.dashboard.widget.GraphWidget;
import il.ac.technion.cs.smarthouse.system.dashboard.widget.ListWidget;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * @author Elia Traore
 * @since Jun 11, 2017
 */
public class ConfigController implements Initializable {
	private class ButtonCell extends TableCell<NamedPath, String> {
        final Button cellButton = new Button("X");
        final ObservableList<NamedPath> data = ConfigController.this.tableData;
        
        ButtonCell(){
        	Font oldFont = cellButton.getFont();
        	cellButton.setFont(Font.font(oldFont.getFamily(), FontWeight.BOLD, oldFont.getSize()));
            cellButton.setOnAction(e->{
            	NamedPath path = (NamedPath) ButtonCell.this.getTableView().getItems().get(ButtonCell.this.getIndex());
            	data.remove(path);
            });
        }

        @Override protected void updateItem(String item, boolean empty) {
        	super.updateItem(item, empty);
        	this.setGraphic(empty? null : cellButton); //Display button if the row is not empty
        }
	}

	public class NamedPath {//todo: wrap in properties
		private String name, path;

		public NamedPath(String name, String path) {
			this.name = name;
			this.path = path;
		}

		public String getName() {
			return name;
		}

		public String getPath() {
			return path;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setPath(String path) {
			this.path = path;
		}
		
	}

	//------------------ members representing the actual config window ---------------------------
	@FXML public ScrollPane scrollPane;
	@FXML public ComboBox<String> typesComboBox;
	@FXML public HBox widgetsHbox;
	
	@FXML public TableView<NamedPath> table;
	@FXML public TableColumn<NamedPath, String> nameCol, pathCol, cancelCol;
	private final ObservableList<NamedPath> tableData = FXCollections.observableArrayList();
	
	@FXML public TextField nameField;
	@FXML public ComboBox<String> sysPathsComboBox;
	@FXML public TextField unitField;
	@FXML public Button addPathBtn;
	
	@FXML public Button okBtn, cancelBtn;
	
	//------------------ other members -----------------------------------------------------------
	private static Logger log = LoggerFactory.getLogger(ConfigController.class);
	private static final Color chosenColor = Color.AQUA, enteredTileColor = Color.BISQUE, normalTileColor = Color.WHITE;
	//don't change order of lines - timer definition needs to come before initWidgets() call
	private final Map<String, List<AnimationTimer>> timers = new HashMap<>();
	private final Map<String, List<BasicWidget>> widgets = initWidgets();
	
	private ConfigConsumer consumer;
	private WidgetType chosenType;
	private final String unitfDefaultText = "(optional)";
	private final String namefDefaultText = "<name>";
	
	//------------------ private methods ---------------------------------------------------------
	private Map<String, List<BasicWidget>> initWidgets() {
		Map<String, List<BasicWidget>> widgets = new HashMap<>();
		InfoCollector info = new InfoCollector()
									.addInfoEntry("path.to.foo","foo")
									.addInfoEntry("path.to.bar", "bar")
									.setUnit("m/s");
		Double tileSize = 150.0;
		Stream.of(WidgetType.values()).forEach(type ->{
			Optional<BasicWidget> nullableWidget = Optional.ofNullable(type.createWidget(tileSize, info));
			nullableWidget.ifPresent(widget ->{
				List<BasicWidget> newlist = widgets.getOrDefault(widget.getTitle(), new ArrayList<>());
				newlist.add(widget);
				widgets.put(widget.getTitle(), newlist);
				addWidgetTimer(widget);
				setWidgetColorListeners(widget);
			});
		});
		return widgets;
	}
	
	private void addWidgetTimer(final BasicWidget widget) {
		String key = widget.getTitle();
		List<AnimationTimer> where = timers.getOrDefault(key, new ArrayList<>());
		where.add(new AnimationTimer() {

			private long lastTimerCall;
			final Random RND = new Random();
			final Boolean isGraph = key.equals(new GraphWidget(WidgetType.AREA_GRAPH, widget.getTileSize(), widget.getInitalInfo()).getTitle());
			final Boolean isList = key.equals(new ListWidget(WidgetType.BAR_CHART, widget.getTileSize(), widget.getInitalInfo()).getTitle());
			@Override
			public void handle(long now) {
				if (now > lastTimerCall + 500_000_000) {
					if (isGraph){
						GraphWidget realW = (GraphWidget)widget;
						realW.getUpdateKeys().forEach(key -> realW.update(RND.nextInt(100), key));
					}else if(isList){
						ListWidget realW = (ListWidget)widget;
						realW.getUpdateKeys().forEach(key -> realW.update(RND.nextInt(100), key));
					}else
						widget.update(RND.nextDouble() * 100, null);
					lastTimerCall = now;
				}

			}

		});
		timers.put(key, where);
	}
	
	private void setWidgetColorListeners(final BasicWidget widget) {
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
	
	private static void setDefaultText(final TextField f, final String defaultText){
		f.focusedProperty().addListener((obsr, oldV, newV)->{
			if(Arrays.asList(defaultText, "").contains(f.getText()))
				f.setText(newV.equals(true)? "" : defaultText);
		});
	}

	private void shutdownFrom(Button b){
		timers.values().stream().filter(l -> l != null).flatMap(l -> l.stream()).forEach(t -> t.stop());
		widgets.values().stream().filter(l -> l != null).flatMap(l -> l.stream())
				.forEach(w -> w.getTile().setForegroundBaseColor(normalTileColor));
		((Stage) b.getScene().getWindow()).close();
	}
	
	private InfoCollector getCollectedInfo(){
		InfoCollector c = new InfoCollector();
		if(!unitfDefaultText.equals(unitField.getText()))
			c.setUnit(unitField.getText());
		List<String> badNames = Arrays.asList(namefDefaultText, "", null);
		tableData.forEach(namedPath -> {
			String actualname = badNames.contains(namedPath.getName())? null : namedPath.getName();
			c.addInfoEntry(namedPath.getPath(), actualname);
		});
		return c;
	}

	//------------------ public methods ----------------------------------------------------------
	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//widgets
		typesComboBox.setItems(FXCollections.observableArrayList(widgets.keySet()));
		typesComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			Optional.ofNullable(timers.get(newValue)).ifPresent(l -> l.stream().forEach(t -> t.start()));
			widgetsHbox.getChildren()
					.setAll(widgets.get(newValue).stream().map(BasicWidget::getTile).collect(Collectors.toList()));
			Optional.ofNullable(timers.get(oldValue)).ifPresent(l -> l.stream().forEach(t -> t.stop()));
			Optional.ofNullable(widgets.get(oldValue))
					.ifPresent(l -> l.stream().forEach(w -> w.getTile().setForegroundBaseColor(normalTileColor)));
		});
		widgetsHbox.setSpacing(5);
		scrollPane.setContent(widgetsHbox);
		scrollPane.setFitToWidth(true);
		
		//path fields
		table.setItems(tableData);
		nameCol.setCellValueFactory(new PropertyValueFactory<NamedPath,String>("name"));
		pathCol.setCellValueFactory(new PropertyValueFactory<NamedPath,String>("path"));
		cancelCol.setCellValueFactory(namedpath -> new SimpleStringProperty(""));
		cancelCol.setCellFactory(namedpath -> new ButtonCell());
		
		sysPathsComboBox.setOnMouseClicked(e -> addPathBtn.setVisible(true));
		setDefaultText(nameField, namefDefaultText );
		nameField.setOnMouseClicked(e -> addPathBtn.setVisible(true));
		addPathBtn.setOnMouseClicked(e -> {
			if(sysPathsComboBox.getSelectionModel().getSelectedIndex() != 0){
				tableData.add(new NamedPath(nameField.getText(), sysPathsComboBox.getSelectionModel().getSelectedItem()));
				addPathBtn.setVisible(false);
			}
		});
		setDefaultText(unitField, unitfDefaultText);
		
		//final buttons
		okBtn.setOnMouseClicked(e ->{
			if (consumer != null)
				Platform.runLater(()->consumer.create(chosenType, getCollectedInfo()));
			shutdownFrom(okBtn);
		});
		
		cancelBtn.setOnMouseClicked(e -> shutdownFrom(cancelBtn));
	}
	
	public void setListenablePaths(String ... paths){
		sysPathsComboBox.setItems(FXCollections.observableArrayList(paths));
		sysPathsComboBox.getItems().add(0, "<choose path>");
		sysPathsComboBox.getSelectionModel().selectFirst();
	}
	
	public void setConfigConsumer(ConfigConsumer cc) {
		consumer = cc;
	}

}

interface ConfigConsumer {
	void create(WidgetType type, InfoCollector data);
}
