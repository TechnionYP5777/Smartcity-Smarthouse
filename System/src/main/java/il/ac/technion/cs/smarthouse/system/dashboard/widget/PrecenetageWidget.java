package il.ac.technion.cs.smarthouse.system.dashboard.widget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.dashboard.InfoCollector;
import il.ac.technion.cs.smarthouse.system.dashboard.WidgetType;
import il.ac.technion.cs.smarthouse.system.file_system.PathBuilder;

/**
 * @author Elia Traore
 * @since Jun 2, 2017
 */
public class PrecenetageWidget extends BasicWidget {
    private static Logger log = LoggerFactory.getLogger(PrecenetageWidget.class);

    public PrecenetageWidget(final WidgetType t, final Double tileSize, final InfoCollector data) {
        super(t, tileSize, data);
        if (data.getInfoEntries().isEmpty()) {
            //TODO: not always should be logged (i.e. preview)
            log.warn("\n\t" + t + " widget is not supposed to be initialized with no data entries. The widget will not update.");
            return;
        }
        if (data.getInfoEntries().size() != 1)
            //TODO: not always should be logged (i.e. preview)
            log.warn("\n\t" + t + " widget is not supposed to be initalized with more then 1 data entry. Behaviour undefined.");

        if (data.getUnit() != null)
            builder.unit(data.getUnit());
        builder.text(data.getInfoEntries().keySet().toArray()[0].toString().replace(PathBuilder.DELIMITER, " "));
    }

    @Override
    public String getTitle() {
        return "Percentage Widget";
    }

}
