package il.ac.technion.cs.smarthouse.system.dashboard.widget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import il.ac.technion.cs.smarthouse.system.dashboard.InfoCollector;
import il.ac.technion.cs.smarthouse.system.dashboard.WidgetType;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;

/**
 * @author Elia Traore
 * @since Jun 2, 2017
 */
@SuppressWarnings("rawtypes")
public abstract class BasicWidget {
    private static Logger log = LoggerFactory.getLogger(BasicWidget.class);

    protected final WidgetType type;
    protected final Double tileSize;
    protected InfoCollector data;
    protected TileBuilder builder;
    protected Tile tile;

    /**
     * [[SuppressWarningsSpartan]]
     */
    public BasicWidget(final WidgetType type, final Double tileSize, final InfoCollector data) {
        this.type = type;
        this.tileSize = tileSize;
        this.data = data;
        builder = type.createTileBuilder(tileSize).title(data.getTitle() != null ? data.getTitle() : getTitle());
    }

    public abstract String getTitle();

    public void updateAutomaticallyFrom(final FileSystem s) {
        data.getInfoEntries().keySet().forEach(path -> updateAutomaticallyFrom(s, path));
    }

    protected static Double cast(final Object dataObj) {
        if (dataObj == null) {
            log.warn("cast method received a null");
            return null;
        }

        if (Integer.class.equals(dataObj.getClass()))
            return (Integer) dataObj + 0.0;
        if (Double.class.equals(dataObj.getClass()))
            return (Double) dataObj;
        if (Boolean.class.equals(dataObj.getClass()))
            return (Boolean) dataObj ? 1.0 : 0.0;
        if (String.class.equals(dataObj.getClass())) {
            final String sdata = (String) dataObj;
            try {
                return Double.valueOf(sdata);
            } catch (NumberFormatException | ClassCastException e) {}
            try {
                return Integer.valueOf(sdata) + 0.0;
            } catch (final NumberFormatException e) {}
            try {
                return Boolean.valueOf(sdata) ? 1.0 : 0.0;
            } catch (final NumberFormatException e) {}
            // return (Double) StringConverter.convert(Double.class,
            // (String)dataObj);
        }

        log.error("Received an object I don't know how to cast! The object is:" + dataObj + ", of "
                        + dataObj.getClass());
        return 42.0;
    }

    protected void updateAutomaticallyFrom(final FileSystem s, final String path) {
        s.subscribeWithNoNulls((rPath, sData) -> update(cast(sData), path),
                        FileSystemEntries.SENSORS_DATA.buildPath(path));
    }

    public void update(final Object value, final String key) {
        update(cast(value), key);
    }

    public void update(final Double value, final String key) {
        getTile().setValue(value);
    }

    public Tile getTile() {
        if (tile != null)
            return tile;
        tile = builder.build();
        setSize(getTileSize());
        return tile;
    }

    public Double getTileSize() {
        return tileSize;
    }

    public WidgetType getType() {
        return type;
    }

    public InfoCollector getInitalInfo() {
        return data;
    }

    public void setSize(final double tileSize2) {
        tile.setMaxSize(tileSize2, tileSize2);
        tile.setMinSize(tileSize2, tileSize2);
    }
}
