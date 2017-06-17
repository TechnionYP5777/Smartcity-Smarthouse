/**
 * 
 */
package il.ac.technion.cs.smarthouse.system.dashboard.widget;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import il.ac.technion.cs.smarthouse.system.dashboard.InfoCollector;
import il.ac.technion.cs.smarthouse.system.dashboard.WidgetType;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystem;
import il.ac.technion.cs.smarthouse.system.file_system.FileSystemEntries;
import il.ac.technion.cs.smarthouse.system.services.file_system_service.FileSystemService;

/**
 * @author Elia Traore
 * @since Jun 2, 2017
 */
public abstract class BasicWidget {
	private static Logger log = LoggerFactory.getLogger(BasicWidget.class);
	protected final WidgetType type;
	protected final Double tileSize;
	protected InfoCollector data;
	protected TileBuilder builder;
	protected Tile tile;
	
	public BasicWidget(WidgetType type, Double tileSize, InfoCollector data) {
		this.type = type;
		this.tileSize = tileSize;
		this.data = data;
		builder = type.createTileBuilder(tileSize).title(getTitle());
	}

	public abstract String getTitle();
	
	public void updateAutomaticallyFrom(FileSystem fs) {
		data.getInfoEntries().keySet().forEach(path -> updateAutomaticallyFrom(fs, path));
	}
	
	protected void updateAutomaticallyFrom(FileSystem fileSystem, String path){
	      fileSystem.subscribe((rPath, data) -> this.update(Double.valueOf((String) data), path), FileSystemEntries.SENSORS_DATA.buildPath(path));
	}
	
	public void update(Number value, String key){
		getTile().setValue(value.doubleValue());
	}

	public Tile getTile() {
		if (tile == null){
			tile = builder.build();
			tile.setMaxSize(getTileSize(), getTileSize());
			tile.setMinSize(getTileSize(), getTileSize());
		}
		return tile;
	}

	public Double getTileSize() {
		return tileSize;
	}

	public WidgetType getType() {
		return type;
	}
	
	public InfoCollector getInitalInfo(){
		return data;
	}
}
