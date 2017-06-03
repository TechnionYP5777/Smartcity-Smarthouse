/**
 * 
 */
package il.ac.technion.cs.smarthouse.applications.dashboard.model.widget;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import il.ac.technion.cs.smarthouse.applications.dashboard.model.WidgetType;

/**
 * @author Elia Traore
 * @since Jun 2, 2017
 */
public abstract class BasicWidget {
	private static Integer TILE_SIZE = 150;
	private Integer actualSize;
	
	final protected WidgetType type;
	protected TileBuilder builder;
	protected Tile tile;
	
	public BasicWidget(WidgetType t){
		type = t;
		builder = t.getBuilder(getDefaultTileSize()).title(getTitle());
	}
	
	public abstract String getTitle();
	
	public void updateExisting(Number value, String key){
		addEntry(value, key);
	}
	
	public void addEntry(Number value, String key){
		getTile().setValue((double)value);
	}
	
	public Tile getTile(){
		if(tile == null)
			tile = builder.build();
		return tile;
	}
	
	public static Integer getDefaultTileSize(){
		return TILE_SIZE;
	}
	
	public static void setDefaultTileSize(Integer tileSize){
		TILE_SIZE = tileSize;
	}
	

	public Integer getActualSize(){
		return actualSize == null? TILE_SIZE : actualSize;
	}
	
	/** send null in param size to use the class default size
	 * */
	public void setActualSize(Integer size){
		actualSize = size;
	}
	
	public WidgetType getType(){
		return type;
	}

}
