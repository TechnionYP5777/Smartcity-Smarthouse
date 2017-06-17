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
        
        public wInfo(WidgetType type, InfoCollector info, Double size) {
            this.type = type;
            this.info = info;
            this.size = size;
        }

        public wInfo(BasicWidget widget) {
            this(widget.getType(), widget.getInitalInfo(), widget.getTileSize());
        }

        public Widget getRepresentedWidget(){
            return new Widget(type, info, size);
        }
    }
    
    public class Widget {
        private BasicWidget widget;
        private String id;
        
        Widget(WidgetType type, InfoCollector pathsInfo, Double size) {
            widget = type.createWidget(size, pathsInfo);
            widget.updateAutomaticallyFrom(fileSystem);
        }
        
        // TODO: can backend update be removed? may happen throught the front -- @addToDashboard,removeFromDashboard
        public void addToDashboard(){
            id = DashboardCore.this.widgetPresenter.apply(widget);  //update front
            DashboardCore.this.registerWidget(id, widget);          //update end
        }
        
        public void removeFromDashboard(){
            Optional.ofNullable(id).ifPresent(i -> {
                DashboardCore.this.widgetRemover.accept(i);           //update front
                DashboardCore.this.deregisterWidget(i);             //update end 
            });
        }
    }
    
    FileSystem fileSystem;
    private Map<String, wInfo> widgetsInfo = new HashMap<>();
    
    //GUI helpers 
    Function<BasicWidget,String> widgetPresenter;
    Consumer<String> widgetRemover;
    
    public DashboardCore(SystemCore systemCore) {
        super(systemCore); 
        fileSystem = systemCore.getFileSystem();
    }
    
    @Override
    public void populate(final String jsonString) throws Exception {
        super.populate(jsonString);
        widgetsInfo.values().forEach(winfo -> winfo.getRepresentedWidget().addToDashboard());
    }
    
    public Widget createWidget(WidgetType type, InfoCollector pathsInfo, Double size){
        return new Widget(type, pathsInfo, size);
    }
    
    //-------------------front-end setters---------------------------------------------
    public void setWidgetPresenter(Function<BasicWidget,String> presenter){
        widgetPresenter = presenter;
    }
    
    public void setWidgetRemover(Consumer<String> remover){
        widgetRemover = remover;
    }

    //-------------------"API" for the gui to inform on front end changes -------------
    public void registerWidget(String id, BasicWidget widget){
        widgetsInfo.put(id, new wInfo(widget));
    }
    
    public void deregisterWidget(String id){
        widgetsInfo.remove(id);
    }
    

}
