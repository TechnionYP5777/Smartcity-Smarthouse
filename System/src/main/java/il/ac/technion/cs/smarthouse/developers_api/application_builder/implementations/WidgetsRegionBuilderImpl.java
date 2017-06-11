package il.ac.technion.cs.smarthouse.developers_api.application_builder.implementations;

import il.ac.technion.cs.smarthouse.developers_api.application_builder.WidgetsRegionBuilder;

/**
 * Implementation of {@link WidgetsRegionBuilder}
 * 
 * @author RON
 * @since 10-06-2017
 */
public final class WidgetsRegionBuilderImpl extends AbstractRegionBuilder implements WidgetsRegionBuilder {
    public WidgetsRegionBuilderImpl() {
        super.setTitle("Widgets");
    }

    @Override
    public WidgetsRegionBuilderImpl setTitle(String title) {
        super.setTitle(title);
        return this;
    }

    // TODO: Ron + Elia
}
