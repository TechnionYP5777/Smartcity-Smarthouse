package il.ac.technion.cs.smarthouse.developers_api.application_builder.implementations;

import il.ac.technion.cs.smarthouse.developers_api.application_builder.AppBuilder;
import il.ac.technion.cs.smarthouse.developers_api.application_builder.ConfigurationsRegionBuilder;
import il.ac.technion.cs.smarthouse.developers_api.application_builder.CustomRegionBuilder;
import il.ac.technion.cs.smarthouse.developers_api.application_builder.StatusRegionBuilder;
import il.ac.technion.cs.smarthouse.developers_api.application_builder.WidgetsRegionBuilder;
import il.ac.technion.cs.smarthouse.javafx_elements.AppGridPane;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

/**
 * Implementation of {@link AppBuilder}
 * 
 * @author RON
 * @since 10-06-2017
 */
public final class AppBuilderImpl implements AppBuilder {
    private final ConfigurationsRegionBuilderImpl configurationsBuilder;
    private final StatusRegionBuilderImpl statusBuilder;
    private final WidgetsRegionBuilderImpl widgetBuilder;
    private final CustomRegionBuilderImpl customBuilder;

    public AppBuilderImpl(final ClassLoader applicationsClassLoader) {
        configurationsBuilder = new ConfigurationsRegionBuilderImpl();
        statusBuilder = new StatusRegionBuilderImpl();
        widgetBuilder = new WidgetsRegionBuilderImpl();
        customBuilder = new CustomRegionBuilderImpl(applicationsClassLoader);
    }

    @Override
    public ConfigurationsRegionBuilder getConfigurationsRegionBuilder() {
        return configurationsBuilder;
    }

    @Override
    public StatusRegionBuilder getStatusRegionBuilder() {
        return statusBuilder;
    }

    @Override
    public WidgetsRegionBuilder getWidgetsRegionBuilder() {
        return widgetBuilder;
    }

    @Override
    public CustomRegionBuilder getCustomRegionBuilder() {
        return customBuilder;
    }

    public Parent build() { // TODO: I don't want this public ):
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
            customBuilder.getAppBuilderItems().forEach(i -> page.getChildren().add(i.node));

        return page;
    }
}
