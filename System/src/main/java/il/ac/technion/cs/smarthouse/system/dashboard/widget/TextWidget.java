package il.ac.technion.cs.smarthouse.system.dashboard.widget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.ac.technion.cs.smarthouse.system.dashboard.InfoCollector;
import il.ac.technion.cs.smarthouse.system.dashboard.WidgetType;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.file_system.PathBuilder;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * 
 * @author Inbal Zukerman
 * @since 23-06-2017
 */
public class TextWidget extends BasicWidget {

    private static Logger log = LoggerFactory.getLogger(TextWidget.class);
    public Label dataText = new Label("Data");

    public TextWidget(final WidgetType type, final Double tileSize, final InfoCollector data) {
        super(type, tileSize, data);

        if (data.getInfoEntries().isEmpty()) {
            //TODO: not always should be logged (i.e. preview)
            log.warn("\n\t" + type + " widget is not supposed to be initialized with no data entries. The widget will not update.");
            return;
        }

        dataText.setTextFill(Color.WHITE);
        dataText.setFont(new Font("Cambria", 32));
        builder.graphic(dataText);

    }

    @Override
    protected void updateAutomaticallyFrom(final FileSystem s, final String path) {
        s.subscribe((rPath, sData) -> update(sData, path), FileSystemEntries.SENSORS_DATA.buildPath(path));
    }

    @Override
    public void update(final Object value, final String key) {

        getTile().setText(key.replace(PathBuilder.DELIMITER, " "));

        if (value != null)
            Platform.runLater(() -> dataText.setText(value.toString()));

    }

    @Override
    public String getTitle() {
        return "Text Widget";
    }

}
