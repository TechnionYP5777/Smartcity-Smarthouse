package il.ac.technion.cs.smarthouse.app_builder;

import il.ac.technion.cs.smarthouse.javafx_elements.AppGridPane;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

/**
 * Allows the developer to build the GUI's layout
 * @author RON
 * @since 10-06-2017
 */
public class AppBuilder {
    private ConfigurationsRegionBuilder configurationsBuilder = new ConfigurationsRegionBuilder();
    private StatusRegionBuilder statusBuilder = new StatusRegionBuilder();
    private WidgetsRegionBuilder widgetBuilder = new WidgetsRegionBuilder();
    
    public ConfigurationsRegionBuilder getConfigurationsRegionBuilder() {
        return configurationsBuilder;
    }

    public StatusRegionBuilder getStatusRegionBuilder() {
        return statusBuilder;
    }

    public WidgetsRegionBuilder getWidgetsRegionBuilder() {
        return widgetBuilder;
    }

    Parent build() {
        final VBox page = new VBox(15);
        page.setPadding(new Insets(10));
        page.setAlignment(Pos.TOP_CENTER);

        final RegionBuilder[] rbs = { configurationsBuilder, statusBuilder, widgetBuilder };

        final AppGridPane grid = new AppGridPane();
        for (RegionBuilder regionBuilder : rbs)
            if (!regionBuilder.isEmpty())
                regionBuilder.build(grid).addRow(new Separator(Orientation.HORIZONTAL));
        
        grid.setMaxWidth(500);
        page.getChildren().add(grid);

        return page;
    }
}
