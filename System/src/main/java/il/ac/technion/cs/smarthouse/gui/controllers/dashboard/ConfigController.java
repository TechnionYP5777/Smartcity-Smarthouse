package il.ac.technion.cs.smarthouse.gui.controllers.dashboard;

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

import eu.hansolo.tilesfx.Tile;
import il.ac.technion.cs.smarthouse.gui.controllers.SystemGuiController;
import il.ac.technion.cs.smarthouse.gui_controller.GuiController;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.SystemMode;
import il.ac.technion.cs.smarthouse.system.dashboard.InfoCollector;
import il.ac.technion.cs.smarthouse.system.dashboard.WidgetType;
import il.ac.technion.cs.smarthouse.system.dashboard.widget.BasicWidget;
import il.ac.technion.cs.smarthouse.system.dashboard.widget.GraphWidget;
import il.ac.technion.cs.smarthouse.system.dashboard.widget.ListWidget;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem.ReadOnlyFileNode;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.file_system.PathBuilder;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * @author Elia Traore
 * @since 11-06-2017
 * 
 *        This is the controller for the configuration window in the dashboard
 *        view
 */
public class ConfigController extends SystemGuiController {
    private class ButtonCell extends TableCell<NamedPath, String> {
        final Button cellButton = new Button("X");
        @SuppressWarnings("synthetic-access") final ObservableList<NamedPath> data = tableData;

        ButtonCell() {
            final Font oldFont = cellButton.getFont();
            cellButton.setFont(Font.font(oldFont.getFamily(), FontWeight.BOLD, oldFont.getSize()));
            Tooltip removeTooltip = new Tooltip("Click to remove this path");
            removeTooltip.setStyle("-fx-font: System 12");
            cellButton.setTooltip(removeTooltip);
            cellButton.setOnAction(e -> {
                final NamedPath path = ButtonCell.this.getTableView().getItems().get(ButtonCell.this.getIndex());
                data.remove(path);
            });
        }

        @Override
        protected void updateItem(final String item, final boolean empty) {
            super.updateItem(item, empty);

            // Display button if the row is not empty
            setGraphic(empty ? null : cellButton);
        }
    }

    public class NamedPath {
        private String name, path;

        public NamedPath(final String name, final String path) {
            this.name = name;
            this.path = path;
        }

        public String getName() {
            return name;
        }

        public String getPath() {
            return path;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public void setPath(final String path) {
            this.path = path;
        }

    }

    // ------------------ GUI element in the config window --------------------
    @FXML ScrollPane scrollPane;
    @FXML ComboBox<String> typesComboBox;
    @FXML HBox widgetsHbox;
    @FXML TableView<NamedPath> table;
    @FXML TableColumn<NamedPath, String> nameCol, pathCol, cancelCol;
    @FXML TextField titleField;
    @FXML TextField nameField;
    @FXML ComboBox<String> sysPathsComboBox;
    @FXML TextField unitField;
    @FXML Button addPathBtn;
    @FXML Button okBtn, cancelBtn;

    private final ObservableList<NamedPath> tableData = FXCollections.observableArrayList();

    // ------------------ other members ---------------------------------------

    private static final Color chosenColor = Color.DEEPSKYBLUE, enteredTileColor = Color.BISQUE,
                    normalTileColor = Color.WHITE;

    // timer definition needs to come before the initWidgets() call
    private final Map<String, List<AnimationTimer>> timers = new HashMap<>();
    private final Map<String, List<BasicWidget>> widgets = initWidgets();

    private ConfigConsumer consumer;
    private WidgetType chosenType;
    private final String unitfDefaultText = "(optional)", namefDefaultText = "<name>",
                    pathscbDefaultText = "<choose path>", titlefDefaultText = "(optional)";

    // ------------------ private helper methods ------------------------------

    private Map<String, List<BasicWidget>> initWidgets() {

        final Map<String, List<BasicWidget>> allWidgets = new HashMap<>();
        final InfoCollector info = new InfoCollector().addInfoEntry("path.to.foo", "foo")
                        .addInfoEntry("path.to.bar", "bar").setUnit("m/s");
        final Double tileSize = 150.0;

        Stream.of(WidgetType.values()).forEach(type -> {
            final Optional<BasicWidget> nullableWidget = Optional.ofNullable(type.createWidget(tileSize, info));
            nullableWidget.ifPresent(widget -> {
                final List<BasicWidget> newlist = allWidgets.getOrDefault(widget.getTitle(), new ArrayList<>());
                newlist.add(widget);
                allWidgets.put(widget.getTitle(), newlist);
                addWidgetTimer(widget);
                setWidgetColorListeners(widget);
            });
        });
        return allWidgets;
    }

    private void addWidgetTimer(final BasicWidget w) {
        final String key = w.getTitle();
        final List<AnimationTimer> where = timers.getOrDefault(key, new ArrayList<>());
        where.add(new AnimationTimer() {

            private long lastTimerCall;
            final Random RND = new Random();
            final Boolean isGraph = key.equals(
                            new GraphWidget(WidgetType.AREA_GRAPH, w.getTileSize(), w.getInitalInfo()).getTitle());
            final Boolean isList = key.equals(
                            new ListWidget(WidgetType.BAR_CHART, w.getTileSize(), w.getInitalInfo()).getTitle());

            @Override
            public void handle(final long now) {
                if (now <= lastTimerCall + 500_000_000)
                    return;
                if (isGraph) {
                    final GraphWidget realW = (GraphWidget) w;
                    realW.getUpdateKeys().forEach(updateKey -> realW.update(RND.nextInt(100) + 0.0, updateKey));
                } else if (!isList)
                    w.update(100 * RND.nextDouble(), null);
                else {
                    final ListWidget realW = (ListWidget) w;
                    realW.getUpdateKeys().forEach(updateKey -> realW.update(RND.nextInt(100) + 0.0, updateKey));
                }
                lastTimerCall = now;
            }
        });
        timers.put(key, where);
    }

    private void setWidgetColorListeners(final BasicWidget w) {
        final Tile t = w.getTile();
        t.setOnMouseEntered(e -> {
            if (!chosenColor.equals(t.getForegroundColor()))
                t.setForegroundBaseColor(enteredTileColor);
        });
        t.setOnMouseExited(e -> {
            if (!chosenColor.equals(t.getForegroundColor()))
                t.setForegroundBaseColor(normalTileColor);
        });
        t.setOnMouseClicked(e -> {
            chosenType = w.getType();
            widgets.get(w.getTitle()).stream()
                            .forEach(otherT -> otherT.getTile().setForegroundBaseColor(normalTileColor));
            t.setForegroundBaseColor(chosenColor);
        });
    }

    private static void setDefaultText(final TextField f, final String defaultText) {
        f.focusedProperty().addListener((obsr, oldV, newV) -> {
            if (Arrays.asList(defaultText, "").contains(f.getText()))
                f.setText(newV.equals(true) ? "" : defaultText);
        });
    }

    private void shutdownFrom(final Button b) {
        timers.values().stream().filter(l -> l != null).flatMap(l -> l.stream()).forEach(t -> t.stop());
        widgets.values().stream().filter(l -> l != null).flatMap(l -> l.stream())
                        .forEach(w -> w.getTile().setForegroundBaseColor(normalTileColor));
        ((Stage) b.getScene().getWindow()).close();
    }

    private InfoCollector getCollectedInfo() {
        final InfoCollector c = new InfoCollector();

        if (!titlefDefaultText.equals(titleField.getText()))
            c.setTitle(titleField.getText());
        if (!unitfDefaultText.equals(unitField.getText()))
            c.setUnit(unitField.getText());

        final List<String> badNames = Arrays.asList(namefDefaultText, "", null);
        tableData.forEach(namedPath -> {
            if (!pathscbDefaultText.equals(namedPath.getPath())) {
                final String actualname = badNames.contains(namedPath.getName()) ? null : namedPath.getName();
                c.addInfoEntry(namedPath.getPath(), actualname);
            }
        });

        return c;
    }

    private List<String> getAvailablePaths(final FileSystem s) {
        List<String> $ = getAvailablePathsInner(s.getReadOnlyFileSystem(FileSystemEntries.SENSORS_DATA.buildPath()),
                        new ArrayList<>());
        if ($.isEmpty())
            $.add("");
        return $;
    }

    private List<String> getAvailablePathsInner(final ReadOnlyFileNode n, final List<String> ss) {

        if (n.isLeaf())
            return ss;

        final List<String> l = PathBuilder.decomposePath(n.getFullPath());
        ss.add(PathBuilder.buildPath(l.subList(1, l.size())));

        n.getChildren().forEach(c -> ss.addAll(getAvailablePathsInner(c, new ArrayList<>())));
        return ss;
    }

    // ------------------ GUI elements initializers ---------------------------
    private void initWidgetsRegion() {
        // widgets
        typesComboBox.setItems(FXCollections.observableArrayList(widgets.keySet()));
        typesComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            Optional.ofNullable(timers.get(newValue)).ifPresent(l -> l.stream().forEach(t -> t.start()));
            widgetsHbox.getChildren().setAll(
                            widgets.get(newValue).stream().map(BasicWidget::getTile).collect(Collectors.toList()));
            Optional.ofNullable(timers.get(oldValue)).ifPresent(l -> l.stream().forEach(t -> t.stop()));
            Optional.ofNullable(widgets.get(oldValue)).ifPresent(
                            l -> l.stream().forEach(w -> w.getTile().setForegroundBaseColor(normalTileColor)));
        });
        widgetsHbox.setSpacing(5);
        scrollPane.setContent(widgetsHbox);
        scrollPane.setFitToWidth(true);
    }

    private void initPathDataAddingRegion(final SystemCore model) {

        titleField.setPromptText(titlefDefaultText);
        // path fields
        table.setItems(tableData);
        nameCol.setCellValueFactory(new PropertyValueFactory<NamedPath, String>("name"));
        pathCol.setCellValueFactory(new PropertyValueFactory<NamedPath, String>("path"));
        cancelCol.setCellValueFactory(namedpath -> new SimpleStringProperty(""));
        cancelCol.setCellFactory(namedpath -> new ButtonCell());

        sysPathsComboBox.setItems(FXCollections.observableArrayList(getAvailablePaths(model.getFileSystem())));
        sysPathsComboBox.getItems().set(0, pathscbDefaultText);
        sysPathsComboBox.getSelectionModel().selectFirst();
        sysPathsComboBox.setOnMouseClicked(e -> addPathBtn.setVisible(true));
        setDefaultText(nameField, namefDefaultText);
        nameField.setOnMouseClicked(e -> addPathBtn.setVisible(true));
        addPathBtn.setOnMouseClicked(e -> {
            if (sysPathsComboBox.getSelectionModel().getSelectedIndex() != 0) {
                tableData.add(new NamedPath(nameField.getText(),
                                sysPathsComboBox.getSelectionModel().getSelectedItem()));
                addPathBtn.setVisible(false);
            }
        });
        setDefaultText(unitField, unitfDefaultText);
    }

    private void initExitButtonsRegion() {
        // final buttons
        okBtn.setOnMouseClicked(e -> {
            if (consumer != null)
                Platform.runLater(() -> consumer.create(chosenType, getCollectedInfo()));
            shutdownFrom(okBtn);
        });

        cancelBtn.setOnMouseClicked(e -> shutdownFrom(cancelBtn));
    }

    /*
     * (non-Javadoc)
     * 
     * @see il.ac.technion.cs.smarthouse.gui.controllers.SystemGuiController#
     * initialize(il.ac.technion.cs.smarthouse.system.SystemCore,
     * il.ac.technion.cs.smarthouse.gui_controller.GuiController,
     * il.ac.technion.cs.smarthouse.system.SystemMode, java.net.URL,
     * java.util.ResourceBundle)
     */
    @Override
    protected <T extends GuiController<SystemCore>> void initialize(final SystemCore model1, final T parent1,
                    final SystemMode extraData1, final URL location, final ResourceBundle b) {
        initWidgetsRegion();

        initPathDataAddingRegion(model1);

        initExitButtonsRegion();
    }

    // ------------------ public methods --------------------------------------

    /**
     * This method allows to set a consumer to the configuration
     * 
     * @param c
     *            The consumer
     */
    public void setConfigConsumer(final ConfigConsumer c) {
        consumer = c;
    }

}

interface ConfigConsumer {
    void create(WidgetType t, InfoCollector data);
}
