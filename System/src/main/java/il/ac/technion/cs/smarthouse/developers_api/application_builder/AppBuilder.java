package il.ac.technion.cs.smarthouse.developers_api;

import il.ac.technion.cs.smarthouse.javafx_elements.AppGridPane;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

/**
 * Allows the developer to build the GUI's layout
 * 
 * @author RON
 * @since 10-06-2017
 */
public final class AppBuilder {
    private ConfigurationsRegionBuilder configurationsBuilder = new ConfigurationsRegionBuilder();
    private StatusRegionBuilder statusBuilder = new StatusRegionBuilder();
    private WidgetsRegionBuilder widgetBuilder = new WidgetsRegionBuilder();
    private CustomRegionBuilder customBuilder = new CustomRegionBuilder();

    public ConfigurationsRegionBuilder getConfigurationsRegionBuilder() {
        return configurationsBuilder;
    }

    public StatusRegionBuilder getStatusRegionBuilder() {
        return statusBuilder;
    }

    public WidgetsRegionBuilder getWidgetsRegionBuilder() {
        return widgetBuilder;
    }

    public CustomRegionBuilder getCustomRegionBuilder() {
        return customBuilder;
    }

    Parent build() {
        final VBox page = new VBox(15);
        page.setPadding(new Insets(10));
        page.setAlignment(Pos.TOP_CENTER);

        final AbstractRegionBuilder[] rbs = { configurationsBuilder, statusBuilder, widgetBuilder };

        final AppGridPane grid = new AppGridPane();
        for (AbstractRegionBuilder regionBuilder : rbs)
            if (!regionBuilder.isEmpty())
                regionBuilder.build(grid).addRow(new Separator(Orientation.HORIZONTAL));
        
        grid.setMaxWidth(500);
        page.getChildren().add(grid);
        
        if (!customBuilder.isEmpty())
            customBuilder.getAppBuilderItems().forEach(i->page.getChildren().add(i.node));

        return page;
    }
}
