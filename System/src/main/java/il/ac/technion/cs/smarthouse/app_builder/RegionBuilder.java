package il.ac.technion.cs.smarthouse.app_builder;

import java.util.ArrayList;
import java.util.List;

import il.ac.technion.cs.smarthouse.javafx_elements.AppGridPane;
import il.ac.technion.cs.smarthouse.javafx_elements.AppLabel;
import javafx.scene.Node;

abstract class RegionBuilder {
    static class AppBuilderItem {
        String fieldTitle;
        Node node;
        
        AppBuilderItem(final String title, final Node node) {
            this.fieldTitle = title;
            this.node = node;
        }
    }
    
    private String title;
    private List<AppBuilderItem> appBuilderItems = new ArrayList<>();
    
    String getTitle() {
        return title;
    }
    
    protected RegionBuilder setTitle(String title) {
        this.title = title;
        return this;
    }
    
    protected void addAppBuilderItem(AppBuilderItem i) {
        appBuilderItems.add(i);
    }
    
    AppGridPane build(final AppGridPane p) {
        p.addRow(new AppLabel(getTitle(), 20).addShadow());
        
        for (AppBuilderItem appBuilderItem : appBuilderItems)
            p.addRow(new AppLabel(appBuilderItem.fieldTitle, 14), appBuilderItem.node);
        
        return p;
    }
    
    boolean isEmpty() {
        return appBuilderItems.isEmpty();
    }
    
}
