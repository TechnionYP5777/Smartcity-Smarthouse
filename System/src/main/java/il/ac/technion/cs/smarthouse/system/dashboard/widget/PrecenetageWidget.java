package il.ac.technion.cs.smarthouse.system.dashboard.widget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.dashboard.InfoCollector;
import il.ac.technion.cs.smarthouse.system.dashboard.WidgetType;

/**
 * @author Elia Traore
 * @since Jun 2, 2017
 */
public class PrecenetageWidget extends BasicWidget {
    private static Logger log = LoggerFactory.getLogger(PrecenetageWidget.class);
    private String upperTitle, upperTitleKey;

    public PrecenetageWidget(final WidgetType t, final Double tileSize, final InfoCollector data) {
        super(t, tileSize, data);
        if (data.getInfoEntries().isEmpty()) {
            log.warn(t + " widget is not supposed to be initialized with no data entries. The widget will not update.");
            return;
        }
        if (data.getInfoEntries().size() != 1)
            log.warn(t + " widget is not supposed to be initalized with more then 1 data entry. Behaviour undefined.");
        upperTitleKey = (String) data.getInfoEntries().keySet().toArray()[0];
        upperTitle = data.getInfoEntries().get(upperTitleKey);
        builder.description(upperTitle).unit(data.getUnit());
    }

    @Override
    public String getTitle() {
        return "Percentage Widget";
    }

}
