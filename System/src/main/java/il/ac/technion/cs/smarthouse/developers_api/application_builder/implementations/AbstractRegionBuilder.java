package il.ac.technion.cs.smarthouse.developers_api.application_builder.implementations;

import java.util.ArrayList;
import java.util.List;

import il.ac.technion.cs.smarthouse.javafx_elements.AppGridPane;
import il.ac.technion.cs.smarthouse.javafx_elements.AppLabel;
import javafx.scene.Node;

/**
 * an abstract GUI layout region
 * 
 * @author RON
 * @since 10-06-2017
 */
abstract class AbstractRegionBuilder {
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

    protected AbstractRegionBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    protected void addAppBuilderItem(AppBuilderItem i) {
        appBuilderItems.add(i);
    }

    AppGridPane build(final AppGridPane p) {
        if (getTitle() != null)
            p.addRow(new AppLabel(getTitle(), 20).addShadow());

        for (AppBuilderItem appBuilderItem : appBuilderItems)
            if (appBuilderItem.fieldTitle == null)
                p.addRow(appBuilderItem.node);
            else
                p.addRow(new AppLabel(appBuilderItem.fieldTitle, 14), appBuilderItem.node);

        return p;
    }

    boolean isEmpty() {
        return appBuilderItems.isEmpty();
    }

    List<AppBuilderItem> getAppBuilderItems() {
        return appBuilderItems;
    }

}
