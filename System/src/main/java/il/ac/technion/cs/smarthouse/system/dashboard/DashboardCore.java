package il.ac.technion.cs.smarthouse.system.dashboard;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import il.ac.technion.cs.smarthouse.system.SystemCore;
import il.ac.technion.cs.smarthouse.system.cores.ChildCore;
import il.ac.technion.cs.smarthouse.system.dashboard.widget.BasicWidget;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;

public class DashboardCore extends ChildCore {
    private class wInfo {
        WidgetType type;
        InfoCollector info;
        Double size;

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
        }

        // TODO: can backend update be removed? may happen throught the front --
        // @addToDashboard,removeFromDashboard
        public void addToDashboard() {
            id = widgetPresenter.apply(widget); // update front
            registerWidget(id, widget); // update end
        }

        public void removeFromDashboard() {
            Optional.ofNullable(id).ifPresent(i -> {
                widgetRemover.accept(i); // update front
                deregisterWidget(i); // update end
            });
        }
    }

    FileSystem fileSystem;
    private final Map<String, wInfo> widgetsInfo = new HashMap<>();

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
        // todo: is gui initialized at this point?
        widgetsInfo.values().forEach(winfo -> winfo.getRepresentedWidget().addToDashboard());
    }

    public Widget createWidget(final WidgetType t, final InfoCollector pathsInfo, final Double size) {
        return new Widget(t, pathsInfo, size);
    }

    // -------------------front-end
    // setters---------------------------------------------
    public void setWidgetPresenter(final Function<BasicWidget, String> presenter) {
        widgetPresenter = presenter;
    }

    public void setWidgetRemover(final Consumer<String> remover) {
        widgetRemover = remover;
    }

    // -------------------"API" for the gui to inform on front end changes
    // -------------
    public void registerWidget(final String id, final BasicWidget w) {
        widgetsInfo.put(id, new wInfo(w));
    }

    public void deregisterWidget(final String id) {
        widgetsInfo.remove(id);
    }

}
