package il.ac.technion.cs.smarthouse.system.dashboard.widget;

import eu.hansolo.medusa.Gauge;
import il.ac.technion.cs.smarthouse.system.dashboard.InfoCollector;
import il.ac.technion.cs.smarthouse.system.dashboard.WidgetType;

/**
 * @author Elia Traore
 * @since Jun 3, 2017
 */
public class DashboardWidget extends BasicWidget {

    public DashboardWidget(final WidgetType t, final Double tileSize, final InfoCollector data) {
        super(t, tileSize, data);

        if (WidgetType.BASIC_DASHBOARD.equals(type))
            builder.unit(data.getUnit());// .threshold(75.0);
        if (WidgetType.NEEDLE_DASHBOARD.equals(type))
            ((Gauge) getTile().getGraphic()).setUnit(data.getUnit());
    }

    @Override
    public String getTitle() {
        return "Dashboard Widget";
    }

    @Override
    public void update(final Double value, final String key) {
        if (!WidgetType.NEEDLE_DASHBOARD.equals(type))
            super.update(value, key);
        else
            ((Gauge) getTile().getGraphic()).setValue(value);

    }

}
