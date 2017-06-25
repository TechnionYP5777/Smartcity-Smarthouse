package il.ac.technion.cs.smarthouse.system.dashboard.widget;

import il.ac.technion.cs.smarthouse.system.dashboard.InfoCollector;
import il.ac.technion.cs.smarthouse.system.dashboard.WidgetType;

/**
 * 
 * @author Inbal Zukerman
 * @since 21-06-2017
 */
public class SwitchWidget extends BasicWidget {

    public SwitchWidget(final WidgetType type, final Double tileSize, final InfoCollector data) {
        super(type, tileSize, data);
    }

    @Override
    public void update(final Double value, final String key) {
        getTile().setSelected(value > 0.0);
        getTile().setText(value > 0.0 ? "ON" : "OFF");
    }

    @Override
    public String getTitle() {
        return "Switch Widget";
    }

}
