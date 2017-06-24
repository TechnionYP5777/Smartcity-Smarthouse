package il.ac.technion.cs.smarthouse.system.dashboard;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.gson.annotations.Expose;

import eu.hansolo.tilesfx.Tile;
import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.cores.ChildCore;
import il.ac.technion.cs.smarthouse.system.dashboard.widget.BasicWidget;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;

public class DashboardCore extends ChildCore {
    private class wInfo {
        @Expose WidgetType type;
        @Expose InfoCollector info;
        @Expose Double size;

        public wInfo(final WidgetType type, final InfoCollector info, final Double size) {
            this.type = type;
            this.info = info;
            this.size = size;
        }

        public wInfo(final BasicWidget widget) {
            this(widget.getType(), widget.getInitalInfo(), widget.getTileSize());
        }

        public Widget getRepresentedWidget() {
            return new Widget(type, info, size);
        }
    }

    public class Widget {
        private final BasicWidget widget;
        private String id;

        Widget(final WidgetType type, final InfoCollector pathsInfo, final Double size) {
            widget = type.createWidget(size, pathsInfo);
            widget.updateAutomaticallyFrom(fileSystem);

            setOnMouseClick(widget);
        }
        
        Widget(final BasicWidget w){
            widget = w;
            setOnMouseClick(w);
        }

        private Widget(final Widget other) {
            this(other.widget.getType(), other.widget.getInitalInfo(), other.widget.getTileSize());
        }
        
        private void setOnMouseClick(BasicWidget widget){
            widget.getTile().setOnMouseClicked(e -> {
                if (e.getButton().equals(MouseButton.SECONDARY)) {
                    final MenuItem addOpt = new MenuItem("Add to Dashboard"),
							removeOpt = new MenuItem("Remove from Dashboard");
                    addOpt.setOnAction(e1 -> addToDashboard());
                    removeOpt.setOnAction(e1 -> removeFromDashboard());
                    final ContextMenu popup = new ContextMenu();
                    popup.getItems().addAll(addOpt, removeOpt);
                    popup.show(widget.getTile(), e.getScreenX(), e.getScreenY());
                }
            });
        }

        // TODO: can backend update be removed? may happen throught the front --
        // @addToDashboard,removeFromDashboard
        public void addToDashboard() {
            id = widgetPresenter.apply(new Widget(this).widget); // update front
            registerWidget(id, widget); // update end
        }

        public void removeFromDashboard() {
            Optional.ofNullable(id).ifPresent(i -> {
                widgetRemover.accept(i); // update front
                deregisterWidget(i); // update end
            });
        }

        public Tile get() {
            return widget.getTile();
        }
    }

    FileSystem fileSystem;
    @Expose private final Map<String, wInfo> widgetsInfo = new HashMap<>();
    private Boolean waitingToBePresented;

    // GUI helpers
    Function<BasicWidget, String> widgetPresenter;
    Consumer<String> widgetRemover;

    public DashboardCore(final SystemCore systemCore) {
        super(systemCore);
        fileSystem = systemCore.getFileSystem();
    }

    @Override
    public void populate(final String jsonString) throws Exception {
        super.populate(jsonString);
        waitingToBePresented = true;
    }

    public Widget createWidget(final WidgetType t, final InfoCollector pathsInfo, final Double size) {
        return new Widget(t, pathsInfo, size);
    }
    
    public Widget createWidget(final BasicWidget w){
        return new Widget(w);
    }

    // -------------------front-end setters------------------------------------
    public void setWidgetPresenter(final Function<BasicWidget, String> presenter) {
        widgetPresenter = presenter;
        if (!Boolean.TRUE.equals(waitingToBePresented))
			return;
		widgetsInfo.values().forEach(winfo -> winfo.getRepresentedWidget().addToDashboard());
		waitingToBePresented = false;
    }

    public void setWidgetRemover(final Consumer<String> remover) {
        widgetRemover = remover;
    }

    // -------------------"API" for the gui to inform on front end changes ----
    public void registerWidget(final String id, final BasicWidget w) {
        widgetsInfo.put(id, new wInfo(w));
    }

    public void deregisterWidget(final String id) {
        widgetsInfo.remove(id);
    }

}
